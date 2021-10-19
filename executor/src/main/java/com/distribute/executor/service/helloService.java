package com.distribute.executor.service;

import com.distribute.executor.annotation.scheduleJob;
import com.distribute.executor.bean.threadContext;
//import com.distribute.remoting.bean.job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class helloService  {

    @scheduleJob(name = "hello")
    public void hello(String name,Integer id){
        int shardIndex = threadContext.getExecutorContext().getShardIndex();
        int total = threadContext.getExecutorContext().getShardTotal();
        log.info("任务开始 hello==============");
        try {
            Thread.sleep(10000);
            log.info("sleep over!");
            log.info("任务结束 hello0==============");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @scheduleJob(name = "hello_passive")
    public void hello_passive(String name,Integer id){
        int shardIndex = threadContext.getExecutorContext().getShardIndex();
        int total = threadContext.getExecutorContext().getShardTotal();
        log.info("任务开始 hello_passive==============");
        log.info("任务结束 hello_passive==============");
    }
}
