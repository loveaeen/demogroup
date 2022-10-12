package com.sentinel.demo.Controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

    public static List<FlowRule> rules = new ArrayList<>();

    @RequestMapping("/hello")
    public String hello(){

        Entry entry = null;
        try{
            entry = SphU.entry("hello",EntryType.IN);
        }catch(BlockException e){
            return "QPS满了！";
        }finally {
            if(entry!=null){
                entry.exit();
            }
        }
        return "hello sentinel";
    }

    @RequestMapping("/hi")
    public String hi(){
        Entry entry = null;
        try{
            entry = SphU.entry("hi",EntryType.IN);
        }catch(BlockException e){
            return "QPS满了！";
        }finally {
            if(entry!=null){
                entry.exit();
            }
        }
        return "hello sentinel";
    }

    @RequestMapping("change")
    public String changeRule(String resource,long count){
        createRule(resource,count);
        return "修改好了";
    }

    /**
     * 动态规则
     * @param resource 为 hello  hi
     * @param count
     */
    public void createRule(String resource,long count){
        // 注意每次的的规则集合 必须从FlowRuleManager.getRules()中获取
        // 否则无法将规则存入sentinel
        List<FlowRule> rules = FlowRuleManager.getRules();
        FlowRule rule1  = new FlowRule();
        rule1.setResource(resource);
        rule1.setCount(count);
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setLimitApp("default");
        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }
}
