package com.neo.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neo.Utils.EveryUtils;
import com.neo.Utils.NetUtils;
import com.neo.Utils.StatusUtils;
import com.neo.dao.*;
import com.neo.jsonbean.JdJson.Jdoder;
import com.neo.jsonbean.JdJson.Sku;
import com.neo.model.*;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.PddDdkAllOrderListIncrementGetRequest;
import com.pdd.pop.sdk.http.api.response.PddDdkAllOrderListIncrementGetResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liujupeng on 2019/1/11.
 */
@Log
@Component
public class JdOderTask {
    @Value("${miao.apkey}")
    private String apkey;
    @Value("${miao.jdurl}")
    private String url;
    @Value("${domain.jduid}")
    private String jduid;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private JdOderDao jdOderDao;
    @Autowired
    private ScanLogDao scanLogDao;
    @Autowired
    private TbOderDao tbOderDao;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SettingDao settingDao;

    //订单落库
    @Scheduled(fixedRate = 60000)
    public void SynchronOder() {
        Integer PAGE_SIZE = 10;
        Integer PAGE_NO = 1;
        Config jdAuthKey = settingDao.querySetting("JdAuthKey");
        ScanLog scanLog = new ScanLog();
        scanLog.setSrc(1);
        Date lastTime = scanLogDao.getLastTime(scanLog);
        if (lastTime == null) {
            log.warning("京东订单扫描失败时间为======" + EveryUtils.dateToString(new Date()));
            return;
        }
        Long tempTime = lastTime.getTime();
        Long start_sys = tempTime + 60000;
        Long ts = tempTime/1000;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String day = dateFormat.format(tempTime);
        String yyyyMMddHHmm = EveryUtils.timeStamp2Date(ts.toString(), "yyyyMMddHHmm");
        ScanLog scan = new ScanLog();
        scan.setDevName("京东");
        scan.setSrc(1);
        scan.setLastTime(new Date(start_sys));
        scanLogDao.scanLog(scan);
        log.warning("京东" + day);
        while (true) {
            String jdurl = url + "getjdunionorders?";
            Map<String, String> urlSign = new HashMap<>();
            urlSign.put("apkey", apkey);
            urlSign.put("time", yyyyMMddHHmm);
//            urlSign.put("time", "201903261708");
            urlSign.put("type", "1");
            urlSign.put("pageNo", PAGE_NO.toString());
            urlSign.put("pageSize", PAGE_SIZE.toString());
            urlSign.put("key", jdAuthKey.getConfigValue());
            String linkStringByGet = null;
            try {
                linkStringByGet = NetUtils.createLinkStringByGet(urlSign);
                String res = restTemplate.getForObject(jdurl + linkStringByGet, String.class);
                JSONObject resJson = JSON.parseObject(res);
                if (resJson.getInteger("code") == 200) {
                    JSONObject jdData = resJson.getJSONObject("data");


                    JSONArray lists = jdData.getJSONArray("lists");
                    if (lists == null || lists.size() == 0) {
                        break;
                    }
                    for (int i = 0; i < lists.size(); i++) {
                        JSONObject data = (JSONObject) lists.get(i);
                        JSONObject skuList = (JSONObject) data.getJSONArray("skuList").get(0);
                        Sku sku = skuList.toJavaObject(Sku.class);
                        Integer validCode = data.getInteger("validCode");
                        Long orderTime = data.getLong("orderTime");
                        Long orderId = data.getLong("orderId");
                        Long finishTime = data.getLong("finishTime");
                        Long payMonth = data.getLong("payMonth");
                        Integer is = jdOderDao.findIs(orderId);
                        if (is == null || is == 0) {
                            JdOderBean jdOderBean = JdOderBean.builder().validCode(validCode)
                                    .orderTime(orderTime).orderId(orderId)
                                    .finishTime(finishTime).payMonth(payMonth).actualCosPrice(sku.getActualCosPrice())
                                    .positionId(String.valueOf(sku.getPositionId())).actualFee(sku.getActualFee()).commissionRate(Long.valueOf(sku.getCommissionRate())).estimateCosPrice(sku.getEstimateCosPrice())
                                    .estimateFee(sku.getEstimateFee()).finalRate(sku.getFinalRate()).price(sku.getPrice())
                                    .skuId(sku.getSkuId()).skuName(sku.getSkuName()).build();
                            Boolean flag = jdOderDao.addOder(jdOderBean) == 1;
                            if (!flag) {
                                log.warning("京东订单同步失败 ID 为" + jdOderBean.getOrderId() + "=====订单更新时间为" + yyyyMMddHHmm);
                            }
                            Userinfo a = new Userinfo();
                            a.setJdPid(String.valueOf(sku.getPositionId()));
                            Userinfo userinfo = userInfoDao.queryPidUser(a);
                            SysJhAdviceOder var = new SysJhAdviceOder().setName(jdOderBean.getSkuName()).setUserName(userinfo.getUsername());
                            var.setOdersn(jdOderBean.getOrderId().toString()).setPid(String.valueOf(sku.getPositionId())).setOrderStatus(StatusUtils.getStatus(jdOderBean.getValidCode(), 2));
                            var.setSrc(2).setUserid(userinfo.getId().intValue()).setSrcName("京东");
                            var.setOderCreatetime(EveryUtils.timeStampDate(Long.valueOf(orderTime)));
                            var.setOrderStatusDesc(StatusUtils.getStatusDesc(validCode,2 ));
                            tbOderDao.saveTbOderAdvice(var);
                        } else {
                            Jdoder jdoder = new Jdoder();
                            jdoder.setId(is).setActualcosprice(BigDecimal.valueOf(sku.getActualCosPrice()));
                            jdoder.setActualfee(BigDecimal.valueOf(sku.getActualFee())).setCommissionrate(BigDecimal.valueOf(sku.getCommissionRate()));
                            jdoder.setEstimatecosprice(BigDecimal.valueOf(sku.getEstimateCosPrice())).setEstimatefee(BigDecimal.valueOf(sku.getEstimateFee()));
                            jdoder.setFinalrate(BigDecimal.valueOf(sku.getFinalRate())).setPaymonth(payMonth);
                            jdoder.setFinishtime(finishTime).setOrdertime(orderTime).setValidcode(validCode);
                            jdOderDao.jdOderUpdate(jdoder);
                        }

                    }
                    if (!jdData.getBoolean("hasMore")) {
                        break;
                    }

                } else {
                    break;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

    }

}
