package com.neo.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neo.Utils.EveryUtils;
import com.neo.Utils.HttpRequest;
import com.neo.dao.PddOderDao;
import com.neo.jsonbean.PddOderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
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
    static Long sy = 1542297601l;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PddOderDao pddOderDao;
//    //订单通知
//    @Scheduled(fixedDelay = 2000)
//    private void process() {
//        Long sum = sy + 86400;
//        String res = null;
//        String type = "pdd.ddk.order.list.increment.get";
//        Long time = System.currentTimeMillis() / 1000;
//        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
//
//        SortedMap<String, String> urlSign = new TreeMap<>();
//        urlSign.put("client_id", KEY);
//        urlSign.put("type", type);
//        urlSign.put("timestamp", timestamp);
//        urlSign.put("end_update_time", timestamp.toString());
//        urlSign.put("start_update_time","0");
//        urlSign.put("data_type", "JSON");
//        urlSign.put("sign", EveryUtils.pddSign(urlSign, SECRET));
//        //        urlSign.put("access_token", ACCESS_TOKEN);
//        try {
//            res = HttpRequest.sendPost("https://gw-api.pinduoduo.com/api/router", urlSign);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        logger.warn(res);
//        logger.warn(String.valueOf(sy));
//        logger.warn(String.valueOf(sum));
//        JSONArray jsonObject = JSONObject.parseObject(res).getJSONObject("order_list_get_response").getJSONArray("order_list");
//        HashOperations hashOperations = redisTemplate.opsForHash();
//        for (int i = 0; i < jsonObject.size(); i++) {
//            JSONObject o = (JSONObject) jsonObject.get(i);
//            String p_id = o.getString("p_id");
//            String order_sn = o.getString("order_sn");
//            String redis_key = "pdd_pid:" + p_id;
//            hashOperations.put(redis_key, p_id + ":" + order_sn, o.toJSONString());
//        }
////        List values = redisTemplate.opsForHash().size();
////        logger.warn(values.toString());
//
//        sy = sy + 10000;
//        }

    //订单落库
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        String res = null;
        String stattimestamp = "1542335895";
        String type = "pdd.ddk.order.list.increment.get";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        SortedMap<String, String> urlSign = new TreeMap<>();
        urlSign.put("client_id", KEY);
        urlSign.put("type", type);
        urlSign.put("timestamp", timestamp);
        urlSign.put("end_update_time", timestamp);
        urlSign.put("start_update_time", stattimestamp);
        urlSign.put("page_size", "10");
        urlSign.put("page", "1");
        urlSign.put("data_type", "JSON");
        urlSign.put("sign", EveryUtils.pddSign(urlSign, SECRET));
        //        urlSign.put("access_token", ACCESS_TOKEN);
        try {
            Integer pagesize=10;
            res = HttpRequest.sendPost("https://gw-api.pinduoduo.com/api/router", urlSign);
            JSONArray jsonObject = JSONObject.parseObject(res).getJSONObject("order_list_get_response").getJSONArray("order_list");
            Integer total_count = Integer.valueOf(JSONObject.parseObject(res).getJSONObject("order_list_get_response").getString("total_count"));
            if (total_count==0){
                return;
            }
            if (total_count<10){
                for (int i = 0; i < jsonObject.size(); i++) {
                JSONObject o = (JSONObject) jsonObject.get(i);
//                pddOderDao.addOder(o.toJavaObject(PddOderBean.class));
                }
                return;
            }
            logger.warn(String.valueOf(total_count));

            int totalPages;//总页数
            totalPages = total_count / pagesize;
            if (total_count % pagesize != 0){
                totalPages ++;
            }
            for (int i = totalPages; i >0; i--) {
                urlSign.put("page_size", "10");
                urlSign.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
                urlSign.remove("sign");
                urlSign.put("page", String.valueOf(i));
                urlSign.put("sign", EveryUtils.pddSign(urlSign, SECRET));

                res = HttpRequest.sendPost("https://gw-api.pinduoduo.com/api/router", urlSign);
                JSONArray jsb = JSONObject.parseObject(res).getJSONObject("order_list_get_response").getJSONArray("order_list");
                for (int j = 0; i < jsb.size(); j++) {
                 JSONObject o = (JSONObject) jsb.get(i);
                pddOderDao.addOder(o.toJavaObject(PddOderBean.class));
                }
//                pddOderDao.addOder(o.toJavaObject(PddOderBean.class));
            }
            logger.warn(String.valueOf(totalPages));
//            for ()
//                urlSign.put("sign", EveryUtils.pddSign(urlSign, SECRET));
//
//            for (int i = 0; i < jsonObject.size(); i++) {
//                JSONObject o = (JSONObject) jsonObject.get(i);
////                pddOderDao.addOder(o.toJavaObject(PddOderBean.class));
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }
