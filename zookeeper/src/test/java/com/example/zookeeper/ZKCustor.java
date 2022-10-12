package com.example.zookeeper;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;


public class ZKCustor {
    private CuratorFramework client = null;

    final static Logger log = LoggerFactory.getLogger(ZKCustor.class);

    public static final  String ZOOKEEPER_SERVER = "127.0.0.1:2181";

    public void init(){
        if (client!=null){
            return;
        }

        //创建重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,5);

        //创建zookeeper客户端
        client = CuratorFrameworkFactory.builder().connectString(ZOOKEEPER_SERVER)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .namespace("form")
                .build();

        client.start();

    }

    /**
     * 创建某个节点
     * @param node
     */
    public void createNode(String node){
        try {
            if (client.checkExists().forPath(node)==null){
                /**
                 * @author employeeeee
                 * @Description: zk有两种节点
                 * @date 2019/1/18 14:07
                 * @params  * @param 持久节点,创建之后 节点会永远存在 除非你手动删除
                 *                   临时节点 会话断开 自动删除
                 */

                DruidDataSource ds = new DruidDataSource();
                ds.setUrl("jdbc:oracle:thin:@127.0.0.1:1521:helowin");
                ds.setUsername("jdzfgl");
                ds.setPassword("pass");
                ds.setInitialSize(20);
                ds.setMaxActive(50);
                ds.setValidationQuery("SELECT COUNT(*) FROM DUAL");
                JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
                Map<String, Object> stringObjectMap = jdbcTemplate.queryForMap("select formhtml from form_def where defid = 1031");
                client.create().creatingParentContainersIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(node,String.valueOf(stringObjectMap.get("formhtml")).getBytes());
                log.info("zookeeper初始化成功");
            }
        } catch (Exception e) {
            log.error("zookeeper初始化失败");
            e.printStackTrace();
        }
    }

    /**
     * 监听节点变化
     * @param node
     * @throws Exception
     */
    public void watchNode(String node) throws Exception {
        NodeCache nodeCache = new NodeCache(client,node);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                String nodePath = nodeCache.getCurrentData().getPath();
                String data = new String(nodeCache.getCurrentData().getData());
                System.out.println("nodeChanged,nodePath:" + nodePath + " data:" + data);
            }
        });
        nodeCache.start();
    }
}
