package com.neo.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liujupeng on 2018/11/16.
 */
public class EveryUtils {
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
    public  static String pddSign(SortedMap urlSign, String SECRET){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SECRET);
        Set<Map.Entry<String, String>> entry = urlSign.entrySet();
        //通过迭代器取出map中的键值关系，迭代器接收的泛型参数应和Set接收的一致
        Iterator<Map.Entry<String, String>> it = entry.iterator();
        while (it.hasNext()) {
            //将键值关系取出存入Map.Entry这个映射关系集合接口中
            Map.Entry<String, String> me = it.next();
            //使用Map.Entry中的方法获取键和值
            String param = me.getKey() + me.getValue();
            stringBuilder.append(param);
        }
        stringBuilder.append(SECRET);
        String hex = DigestUtils.md5DigestAsHex(stringBuilder.toString().getBytes());
        return hex.toUpperCase();
    }

    //时间戳转日期
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }
    //时间戳转Date
    public static Date timeStampDate(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = sdf.format(time);
        try {
            Date date=sdf.parse(d);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

     public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String URLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
