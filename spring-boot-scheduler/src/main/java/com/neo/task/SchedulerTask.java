package com.neo.task;

import com.neo.Utils.EveryUtils;
import com.neo.Utils.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by summer on 2016/12/1.
 */

@Component
public class SchedulerTask {
    @Value("${pdd_pro.pdd-key}")
    private String KEY;
    @Value("${pdd_pro.pdd-secret}")
    private String SECRET;
    @Value("${pdd_pro.pdd-access_token}")
    private String ACCESS_TOKEN;
    @Value("${pdd_pro.pdd-router-url}")
    private String PDD_URL;
    @Value("${juanhuang.range}")
    private Integer RANGE;
    private int count = 0;
    private final static Logger logger = LoggerFactory.getLogger(SchedulerTask.class);
    static  Long sy=1542360350l;

    //订单通知
    @Scheduled(fixedDelay = 10000)
    private void process() {
        Long sum=sy+10000;
        String res = null;
        String type = "pdd.ddk.order.list.increment.get";
        Long time =System.currentTimeMillis() / 1000;
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        SortedMap<String, String> urlSign = new TreeMap<>();
        urlSign.put("client_id", KEY);
        urlSign.put("type", type);
        urlSign.put("timestamp", timestamp);
        urlSign.put("end_update_time", timestamp);
        urlSign.put("start_update_time", sy.toString());
        urlSign.put("data_type", "JSON");
        urlSign.put("sign", EveryUtils.pddSign(urlSign, SECRET));
        //        urlSign.put("access_token", ACCESS_TOKEN);
        try {
            res = HttpRequest.sendPost("https://gw-api.pinduoduo.com/api/router", urlSign);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.warn(res);
        logger.warn(String.valueOf(sy));
        logger.warn(String.valueOf(sum));

        sy=sy+10000;
    }

//    //订单落库
//    @Scheduled(fixedRate = 1800000)
//    public void reportCurrentTime() {
//        String res = null;
//        String stattimestamp = "1542335895";
//        String type = "pdd.ddk.order.list.increment.get";
//        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
//        SortedMap<String, String> urlSign = new TreeMap<>();
//        urlSign.put("client_id", KEY);
//        urlSign.put("type", type);
//        urlSign.put("timestamp", timestamp);
//        urlSign.put("end_update_time", timestamp);
//        urlSign.put("start_update_time", stattimestamp);
//        urlSign.put("data_type", "JSON");
//        urlSign.put("sign", EveryUtils.pddSign(urlSign, SECRET));
//        //        urlSign.put("access_token", ACCESS_TOKEN);
//        try {
//            res = HttpRequest.sendPost("https://gw-api.pinduoduo.com/api/router", urlSign);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        logger.warn(res);
//    }

}
