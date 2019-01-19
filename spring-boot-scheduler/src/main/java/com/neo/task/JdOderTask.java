package com.neo.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neo.Utils.EveryUtils;
import com.neo.Utils.NetUtils;
import com.neo.Utils.StatusUtils;
import com.neo.dao.PddOderDao;
import com.neo.dao.TbOderDao;
import com.neo.jsonbean.JdJson.Sku;
import com.neo.model.PddOderBean;
import com.neo.model.SysJhAdviceOder;
import com.neo.model.TboderBean;
import com.neo.model.Userinfo;
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
    @Value("${miao.url}")
    private String url;
    @Value("${domain.jduid}")
    private String jduid;
    @Autowired
    private PddOderDao pddOderDao;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TbOderDao tbOderDao;
    //扫描间隔时间
    private static final Long SPAN = 1200000l;
    //扫描开始时间
    private static Long start_sys = System.currentTimeMillis() - 1200000;

    //订单落库
    @Scheduled(fixedRate = 19000)
    public void SynchronOder()  {
        Integer PAGE_SIZE = 10;
        Integer PAGE_NO = 1;
        Long tempTime = start_sys;
        start_sys += SPAN;
        while (true) {

            String jdurl = url + "getjdunionorders?";
            Map<String, String> urlSign = new HashMap<>();
            urlSign.put("apkey", apkey);
            urlSign.put("time", "201901181015");
            urlSign.put("type", "3");
            urlSign.put("pageNo", PAGE_NO.toString());
            urlSign.put("pageSize", PAGE_SIZE.toString());
            String linkStringByGet = null;
            try {
                linkStringByGet = NetUtils.createLinkStringByGet(urlSign);
                String res = restTemplate.getForObject(jdurl + linkStringByGet, String.class);
                JSONObject resJson = JSON.parseObject(res);


                if (resJson.getInteger("code") == 200) {
                    JSONObject jdData = resJson.getJSONObject("data");

                    JSONArray lists = jdData.getJSONArray("lists");
                    if (lists==null||lists.size()==0){
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
                        Double payMonth = data.getDouble("payMonth");


                        log.warning(sku.getSkuName());

                }
                    if (!jdData.getBoolean("hasMore")){
                        break;
                    }
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


//            try {
//                PopClient client = new PopHttpClient(PDD_KEY, PDD_SECRET);
//                PddDdkAllOrderListIncrementGetRequest request = new PddDdkAllOrderListIncrementGetRequest();
//                request.setStartUpdateTime(System.currentTimeMillis()/1000-1200);
//                request.setEndUpdateTime(System.currentTimeMillis()/1000);
//                request.setPageSize(PAGE_SIZE);
//                request.setPage(PAGE_NO);
//                PddDdkAllOrderListIncrementGetResponse response = null;
//                response = client.syncInvoke(request);
//                if (response.getOrderListGetResponse().getTotalCount() == 0) {
//                    break;
//                }
//
//                List<PddDdkAllOrderListIncrementGetResponse.OrderListItem> orderList = response.getOrderListGetResponse().getOrderList();
//                for (int i = 0; i < orderList.size(); i++) {
//                    PddDdkAllOrderListIncrementGetResponse.OrderListItem orderListItem = orderList.get(i);
//                    PddOderBean data = new PddOderBean();
//                    String orderSn = orderListItem.getOrderSn();
//                    PddOderBean pddOderBean = pddOderDao.findIs(orderSn);
//                    if (pddOderBean == null) {
//                        data.setOrder_sn(orderListItem.getOrderSn());
//                        data.setGoods_id(orderListItem.getGoodsId().intValue());
//                        data.setGoods_name(orderListItem.getGoodsName());
//                        data.setGoods_thumbnail_url(orderListItem.getGoodsThumbnailUrl());
//                        data.setGoods_quantity(orderListItem.getGoodsQuantity().intValue());
//                        data.setGoods_price(orderListItem.getGoodsPrice().intValue());
//                        data.setOrder_amount(orderListItem.getOrderAmount().intValue());
//                        data.setOrder_create_time(orderListItem.getOrderCreateTime().intValue());
//                        data.setOrder_verify_time(orderListItem.getOrderVerifyTime().intValue());
//                        data.setOrder_pay_time(orderListItem.getOrderPayTime().intValue());
//                        data.setPromotion_rate(orderListItem.getPromotionRate().intValue());
//                        data.setPromotion_amount(orderListItem.getPromotionAmount().intValue());
//                        data.setOrder_status(orderListItem.getOrderStatus());
//                        data.setOrder_status_desc(orderListItem.getOrderStatusDesc());
//                        data.setOrder_group_success_time(orderListItem.getOrderGroupSuccessTime().intValue());
//                        data.setOrder_modify_at(orderListItem.getOrderModifyAt().intValue());
//                        data.setP_id(orderListItem.getPId());
//                        Userinfo us = new Userinfo();
//                        us.setPddpid(data.getP_id());
//
//                        Userinfo userinfo = tbOderDao.queryUserForPid(us);
//                        SysJhAdviceOder var = new SysJhAdviceOder();
//                        var.setName(data.getGoods_name());
//                        var.setUserName(userinfo.getUsername());
//                        var.setOdersn(data.getOrder_sn());
//                        var.setPid(data.getP_id());
//                        var.setOrderStatus(StatusUtils.getStatus(data.getOrder_status(), 1));
//                        var.setSrc(2);
//                        var.setUserid(userinfo.getId().intValue());
//                        var.setSrcName("拼多多");
//                        Integer order_create_time = data.getOrder_create_time();
//                        var.setOderCreatetime(EveryUtils.timeStampDate(Long.valueOf(order_create_time)));
//                        var.setOrderStatusDesc(data.getOrder_status_desc());
//                        pddOderDao.addOder(data);
//                        Integer adviceSave = tbOderDao.saveTbOderAdvice(var);
//                        if (adviceSave == 0) {
//                            log.warning("注意 拼多多订单同步时候新增订单失败 订单ID为=" + data.getOrder_sn());
//                        }
//
//                    } else {
//                        Integer id = pddOderBean.getId();
//                        data.setOrder_verify_time(orderListItem.getOrderVerifyTime().intValue());
//                        data.setOrder_pay_time(orderListItem.getOrderPayTime().intValue());
//                        data.setOrder_status(orderListItem.getOrderStatus());
//                        data.setOrder_status_desc(orderListItem.getOrderStatusDesc());
//                        data.setOrder_group_success_time(orderListItem.getOrderGroupSuccessTime().intValue());
//                        data.setOrder_modify_at(orderListItem.getOrderModifyAt().intValue());
//                        data.setId(id);
//                        pddOderDao.pddOderUpdate(data);
//
//                    }
//
//                }
////                pddOderDao.scanLog(day, "淘宝");
//                PAGE_NO++;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

    }

}
