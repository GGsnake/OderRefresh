//package com.neo.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//
///**
// * Created by liujupeng on 2018/11/19.
// */
//@EnableScheduling
//@Configuration
//public class SchedulingConfiguration implements SchedulingConfigurer {
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
//        scheduledTaskRegistrar.setScheduler(setTaskExecutors());
//    }
//    @Bean(destroyMethod="shutdown")
//    public Executor setTaskExecutors(){
//        return Executors.newScheduledThreadPool(3); // 3个线程来处理。
//    }
//}
