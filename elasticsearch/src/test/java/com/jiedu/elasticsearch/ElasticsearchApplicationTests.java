package com.jiedu.elasticsearch;

import com.jiedu.elasticsearch.domain.LogInfo;
import net.minidev.json.JSONValue;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.WildcardQuery;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Stream;

@SpringBootTest
class ElasticsearchApplicationTests {

    @Autowired
    @Qualifier("restHighLevelClientConfiguration")
    private RestHighLevelClient client;


    @Autowired
    private RestClient restClient;

    public final static ExecutorService executorService = Executors.newFixedThreadPool(50);

    @Test
    void contextLoads() throws IOException {
        CreateIndexResponse createIndexResponse = null;
        CreateIndexRequest request = new CreateIndexRequest("jd_log");
        request.source("{\n" +
                "    \"settings\" : {\n" +
                "        \"number_of_shards\" : 1,\n" +
                "        \"number_of_replicas\" : 0\n" +
                "    },\n" +
                "    \"mappings\" : {\n" +
                "       \"_doc\" : {\n" +
                "            \"properties\" : {\n" +
                "                \"operId\" : { \"type\" : \"text\" },\n" +
                "                \"title\" : { \"type\" : \"text\" },\n" +
                "                \"businessType\" : { \"type\" : \"integer\" },\n" +
                "                \"method\" : { \"type\" : \"text\" },\n" +
                "                \"requestMethod\" : { \"type\" : \"text\" },\n" +
                "                \"operatorType\" : { \"type\" : \"integer\" },\n" +
                "                \"operName\" : { \"type\" : \"text\" },\n" +
                "                \"deptName\" : { \"type\" : \"text\" },\n" +
                "                \"operUrl\" : { \"type\" : \"text\" },\n" +
                "                \"operIp\" : { \"type\" : \"text\" },\n" +
                "                \"operLocation\" : { \"type\" : \"text\" },\n" +
                "                \"operParam\" : { \"type\" : \"text\" },\n" +
                "                \"opersql\" : { \"type\" : \"text\" },\n" +
                "                \"status\" : { \"type\" : \"integer\" },\n" +
                "                \"errorMsg\" : { \"type\" : \"text\" },\n" +
                "                \"operTime\" : { \"type\" : \"date\" }\n" +
                "            }\n" +
                "       }\n" +
                "    }\n" +
                "}",XContentType.JSON);
        //设置创建索引超时2分钟
        request.timeout(TimeValue.timeValueMinutes(2));
        createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }

    @Test
    void existsIndex() throws IOException {
        //查询索引请求
        GetIndexRequest index = new GetIndexRequest();
        //判断索引是否存在
        boolean exists = client.indices().exists(index, RequestOptions.DEFAULT);
        System.out.println(exists);
        //
    }

    @Test
    void deleteIndex() throws IOException {
        String index = "jd_log";
        String type = "_doc";
        String endPoint = "/" + index + "/" + type +"/_delete_by_query";
        String source = genereateQueryString();
        HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);
        try {
            restClient.performRequest("POST", endPoint, Collections.<String, String> emptyMap(),
                    entity);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public String genereateQueryString(){
        IndexRequest indexRequest = new IndexRequest();
        XContentBuilder builder;
        try {
            builder = JsonXContent.contentBuilder()
                    .startObject()
                    .startObject("query")
                    .startObject("match_all")
                    .endObject()
                    .endObject()
                    .endObject();
            indexRequest.source(builder);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String source = indexRequest.source().utf8ToString();
        return source;
    }


    @Test
    void createDocument() throws IOException {
        /*LogInfo log = new LogInfo();
        log.setTitle("工具借用");
        log.setInfo("select tb.* from l_scldgl_gjjy_n tb,sys_unit un ,sys_user us where tb.cjdw = un.id and tb.cjr = us.loginname  and (tb.status is null or tb.status!=-1)  AND un.ID like '0002000200110002%' order by tb.cjsj desc");
        log.setDate(LocalDateTime.now().toString());*/

        IndexRequest request = new IndexRequest("jd_log","_doc");
        //规则
        /*request.id("13  ");
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");*/

        String jsonStr = null;

        jsonStr ="\n" +
                "\n" +
                "{\"businessType\":10,\"deptName\":\"研发部门\",\"method\":\"com.jiedu.system.mapper.SysUserMapper.selectUserList()\",\"operIp\":\"127.0.0.1\",\"operLocation\":\"内网IP\",\"operName\":\"admin\",\"operParam\":\"{\\\"pageSize\\\":[\\\"10\\\"],\\\"pageNum\\\":[\\\"1\\\"],\\\"orderByColumn\\\":[\\\"createTime\\\"],\\\"isAsc\\\":[\\\"desc\\\"],\\\"deptId\\\":[\\\"\\\"],\\\"parentId\\\":[\\\"\\\"],\\\"loginName\\\":[\\\"\\\"],\\\"phonenumber\\\":[\\\"\\\"],\\\"status\\\":[\\\"\\\"],\\\"params[beginTime]\\\":[\\\"\\\"],\\\"params[endTime]\\\":[\\\"\\\"]}\",\"operTime\":\"2020-09-08T08:36:48.376\",\"operUrl\":\"/system/user/list\",\"operatorType\":1,\"opersql\":\"select u.user_id, u.dept_id, u.login_name, u.user_name, u.user_type, u.email, u.avatar, u.phonenumber, u.password, u.sex, u.salt, u.status, u.del_flag, u.login_ip, u.login_date, u.create_by, u.create_time, u.remark, d.dept_name, d.leader from sys_user u left join sys_dept d on u.dept_id = d.dept_id where u.del_flag = '0'\",\"params\":{},\"requestMethod\":\"POST\",\"status\":0,\"title\":\"用户查询\"}\n" +
                "\n";

        //存入请求 json
        request.source(jsonStr, XContentType.JSON);

        //发送请求 获取响应结果
        for (int i = 0; i < 100; i++) {
            IndexResponse index = client.index(request, RequestOptions.DEFAULT);
        }


    }

    @Test
    void getDocument() throws IOException {
        GetRequest request = new GetRequest();

        //不获取返回的上下文
        //request.fetchSourceContext(new FetchSourceContext(false));
        //request.storedFields("_none_");

        GetResponse documentFields = client.get(request, RequestOptions.DEFAULT);
        Map<String, Object> source = documentFields.getSource();
        System.out.println(source.get("info"));
    }

    @Test
    void batchInsert() throws IOException {
        //批量插入. 还是用for循环而已

        for( int k = 0 ; k < 50 ; k ++){
            executorService.execute(()->{
                BulkRequest bulkRequest = new BulkRequest();
                Stream.iterate(0,i -> i+1).limit(10000).forEach((i)->{
                    String jsonStr ="\n" +
                            "\n" +
                            "{\"businessType\":10,\"deptName\":\"研发部门\",\"method\":\"com.jiedu.system.mapper.SysUserMapper.selectUserList()\",\"operIp\":\"127.0.0.1\",\"operLocation\":\"内网IP\",\"operName\":\"admin\",\"operParam\":\"{\\\"pageSize\\\":[\\\"10\\\"],\\\"pageNum\\\":[\\\"1\\\"],\\\"orderByColumn\\\":[\\\"createTime\\\"],\\\"isAsc\\\":[\\\"desc\\\"],\\\"deptId\\\":[\\\"\\\"],\\\"parentId\\\":[\\\"\\\"],\\\"loginName\\\":[\\\"\\\"],\\\"phonenumber\\\":[\\\"\\\"],\\\"status\\\":[\\\"\\\"],\\\"params[beginTime]\\\":[\\\"\\\"],\\\"params[endTime]\\\":[\\\"\\\"]}\",\"operTime\":\"2020-09-08T08:36:48.376\",\"operUrl\":\"/system/user/list\",\"operatorType\":1,\"opersql\":\"select u.user_id, u.dept_id, u.login_name, u.user_name, u.user_type, u.email, u.avatar, u.phonenumber, u.password, u.sex, u.salt, u.status, u.del_flag, u.login_ip, u.login_date, u.create_by, u.create_time, u.remark, d.dept_name, d.leader from sys_user u left join sys_dept d on u.dept_id = d.dept_id where u.del_flag = '0'\",\"params\":{},\"requestMethod\":\"POST\",\"status\":0,\"title\":\"用户查询\"}\n" +
                            "\n";
                    bulkRequest.add(new IndexRequest("jd_log","_doc").source(jsonStr,XContentType.JSON));
                });
                try {
                    BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        while(!executorService.isTerminated()){}

    }

    @Test
    void searchDoc() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("jd_log","_doc","GtqObHQBD-XIaKF_PEhE");

        client.delete(deleteRequest,RequestOptions.DEFAULT);


        SearchRequest han_index = new SearchRequest("jd_log");
        //构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(5));
        /*MatchQueryBuilder info = QueryBuilders.matchQuery("info", "if");
        //构建查询
        searchSourceBuilder.query(info);*/
        searchSourceBuilder.sort("operTime", SortOrder.DESC);

        han_index.source(searchSourceBuilder);

        SearchResponse search = null;

        search = client.search(han_index, RequestOptions.DEFAULT);
        System.out.println(search.getHits().getHits().length);
        for (SearchHit hit : search.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

}
