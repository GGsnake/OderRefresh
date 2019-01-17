//package com.neo;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
////@RunWith(SpringRunner.class)
////@SpringBootTest
//public class ApplicationTests {
//    public static void main(String[] args) {
//        String datetime =  "20190117111012";
//
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        LocalDateTime ldt = LocalDateTime.parse(datetime,dtf);
//        System.out.println(ldt);
//
//
//        DateTimeFormatter fa = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String datetime2 = ldt.format(fa);
//        System.out.println(datetime2);
//    }
//	public void contextLoads() {
//
//	}
//
//}
