//package com.neo.task;
//
//import com.alibaba.fastjson.JSONObject;
//import com.neo.Utils.EveryUtils;
//import com.neo.Utils.StatusUtils;
//import com.neo.dao.PddOderDao;
//import com.neo.dao.TbOderDao;
//import com.neo.dao.UserInfoDao;
//import com.neo.model.PddOderBean;
//import com.neo.model.SysJhAdviceOder;
//import com.neo.model.TboderBean;
//import com.neo.model.Userinfo;
//import com.pdd.pop.sdk.http.PopClient;
//import com.pdd.pop.sdk.http.PopHttpClient;
//import com.pdd.pop.sdk.http.api.request.PddDdkAllOrderListIncrementGetRequest;
//import com.pdd.pop.sdk.http.api.response.PddDdkAllOrderListIncrementGetResponse;
//import lombok.extern.java.Log;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.List;
//
///**
// * Created by liujupeng on 2019/1/11.
// */
//@Log
//@Component
//public class PddOderTask {
//    @Value("${pdd_pro.pdd-key}")
//    private String PDD_KEY;
//    @Value("${pdd_pro.pdd-secret}")
//    private String PDD_SECRET;
//    @Value("${pdd_pro.pdd-access_token}")
//    private String PDD_ACCESS_TOKEN;
//    @Value("${pdd_pro.pdd-router-url}")
//    private String PDD_URL;
//
//    @Autowired
//    private PddOderDao pddOderDao;
//    @Autowired
//    private TbOderDao tbOderDao;
//    @Autowired
//    private UserInfoDao userInfoDao;
//    //扫描间隔时间
//    private static final Long SPAN = 1200000l;
//    //扫描开始时间
//    private static Long start_sys = System.currentTimeMillis() - 1200000;
//    //订单落库
//    @Scheduled(fixedRate = 19000)
//    public void SynchronOder()  {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM- dd HH:mm:ss");
//        Integer PAGE_SIZE = 10;
//        Integer PAGE_NO = 1;
//        Long tempTime = start_sys;
//        String day = dateFormat.format(tempTime);
//        start_sys += SPAN;
//        while (true) {
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
//                List<PddDdkAllOrderListIncrementGetResponse.OrderListItem> orderList = response.getOrderListGetResponse().getOrderList();
//                for (int i = 0; i < orderList.size(); i++) {
//                    PddDdkAllOrderListIncrementGetResponse.OrderListItem orderListItem = orderList.get(i);
//                    PddOderBean data = new PddOderBean();
//                    String orderSn = orderListItem.getOrderSn();
//                    PddOderBean pddOderBean = pddOderDao.findIs(orderSn);
//                    if (pddOderBean == null) {
//                        data.setOrder_sn(orderListItem.getOrderSn());
//                        data.setGoods_id(orderListItem.getGoodsId());
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
//                        us.setPddPid(data.getP_id());
//                        Userinfo userinfo = userInfoDao.queryPidUser(us);
//                        SysJhAdviceOder var = new SysJhAdviceOder().setName(data.getGoods_name()).setUserName(userinfo.getUsername());
//                        var.setOdersn(data.getOrder_sn()).setPid(data.getP_id()).setOrderStatus(StatusUtils.getStatus(data.getOrder_status(), 1));
//                        var.setSrc(2).setUserid(userinfo.getId().intValue()).setSrcName("拼多多");
//                        Integer order_create_time = data.getOrder_create_time();
//                        var.setOderCreatetime(EveryUtils.timeStampDate(Long.valueOf(order_create_time)));
//                        var.setOrderStatusDesc(data.getOrder_status_desc());
//                        Integer integer = pddOderDao.addOder(data);
//                        if (integer == 0) {
//                            log.warning("注意 拼多多订单同步时候新增订单失败 订单ID为=" + data.getOrder_sn());
//                        }
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
//                        Integer upFlag = pddOderDao.pddOderUpdate(data);
//                        if (upFlag == 0) {
//                            log.warning("注意 拼多多订单同步时候新增订单失败 订单ID为=" + data.getOrder_sn());
//                        }
//                    }
//                }
//                pddOderDao.scanLog(day, "拼多多");
//                PAGE_NO++;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//}
