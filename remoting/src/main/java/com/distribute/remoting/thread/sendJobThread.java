package com.distribute.remoting.thread;

import com.distribute.remoting.Message.CallBackMessage;
import com.distribute.remoting.Message.SendJobMessage;
import com.distribute.remoting.bean.jobFinishDetail;
import com.distribute.remoting.bean.jobSendDetail;
import com.distribute.remoting.netty_server.NettyServer;
import com.distribute.remoting.utils.Context;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class sendJobThread extends Thread{
    private LinkedBlockingQueue<jobSendDetail> jobToBeTransferQueue;
    public static void pushSendJob(jobSendDetail detail){
        getInstance().jobToBeTransferQueue.add(detail);
    }


    public sendJobThread(){
        jobToBeTransferQueue=new LinkedBlockingQueue<>();
        this.setName("sendJobThread:"+System.currentTimeMillis());
        this.setDaemon(true);
        this.start();
    }
    private volatile static sendJobThread instance;

    public static sendJobThread getInstance() {
        if (instance == null) {
            synchronized (sendJobThread.class) {
                if (instance == null) {
                    instance = new sendJobThread();
                }
            }
        }
        return instance;
    }
    @Override
    public void run() {
        while (true){
            try {
                jobSendDetail msg = this.jobToBeTransferQueue.take();
                // 拿到要发送的msg
                List<jobSendDetail> callbackParamList = new ArrayList<>();
                this.jobToBeTransferQueue.drainTo(callbackParamList);
                callbackParamList.add(msg);

                NettyServer server= (NettyServer)Context.getBean(NettyServer.class);
                // 发送msg
                for(jobSendDetail message:callbackParamList){
                    server.sendMessage(new SendJobMessage(message.getJob(), message.getIndex(),message.getTotal(),message.getExecId(),msg.getContents(),msg.getIsCompresses()),0,message.getChannel());
                }
                log.info("sendJobThread success");

            } catch (InterruptedException e) {
                e.printStackTrace();
                log.info("sendJobThread error");
            }
        }

    }
}