//package com.neo.task;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.jd.open.api.sdk.DefaultJdClient;
//import com.jd.open.api.sdk.JdClient;
//import com.jd.open.api.sdk.JdException;
//import com.neo.Utils.HttpDeal;
//import com.neo.Utils.NetUtils;
//import com.neo.Utils.StatusUtils;
//import com.neo.dao.JdOderDao;
//import com.neo.dao.PddOderDao;
//import com.neo.dao.TbOderDao;
//import com.neo.model.SysJhAdviceOder;
//import com.neo.model.Tboder;
//import com.neo.model.Userinfo;
//import jd.union.open.order.query.request.OrderReq;
//import jd.union.open.order.query.request.UnionOpenOrderQueryRequest;
//import jd.union.open.order.query.response.OrderResp;
//import jd.union.open.order.query.response.UnionOpenOrderQueryResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.UnsupportedEncodingException;
//import java.net.MalformedURLException;
//import java.net.URISyntaxException;
//import java.text.SimpleDateFormat;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by liujupeng on 2019/1/11.
// */
//
//@Component
//public class JdOderTask {
//    @Autowired
//    private JdOderDao jdOderDao;
//    @Autowired
//    private PddOderDao pddOderDao;
//    @Value("${domain.jdimageurl}")
//    private String jdimageurl;
//    @Value("${domain.jdsecret}")
//    private String jdsecret;
//    @Value("${domain.jdkey}")
//    private String jdkey;
//    @Value("${domain.jdUrl}")
//    private String jdurl;
//    @Value("${domain.jduid}")
//    private String jduid;
//    private static final String SPAN = "1200";
//    private static Long start_sys = System.currentTimeMillis() - 1200000;
//
//    private final static Logger logger = LoggerFactory.getLogger(JdOderTask.class);
//
//    //订单落库
//    @Scheduled(fixedRate = 19000)
//    public void taobaoOder() throws JdException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
//        String accessToken = "";
//        Long tempTime = start_sys;
//        start_sys += 1200;
//        String day = dateFormat.format(tempTime);
//        Integer PAGE_SIZE = 5;
//        Integer PAGE_NO = 1;
//        while (true) {
//            JdClient client = new DefaultJdClient(jdurl, accessToken, jdkey, jdsecret);
//            UnionOpenOrderQueryRequest unionOpenOrderQueryRequest = new UnionOpenOrderQueryRequest();
//            OrderReq orderReq = new OrderReq();
//            orderReq.setPageNo(1);
//            orderReq.setPageSize(3);
//            orderReq.setType(1);
//            orderReq.setTime(day);
//            unionOpenOrderQueryRequest.setOrderReq(orderReq);
//            UnionOpenOrderQueryResponse response = client.execute(unionOpenOrderQueryRequest);
//            OrderResp[] oderData = response.getData();
//            if (response.getCode() != 200 || oderData.length == 0) {
//                break;
//            }
//
//            for (int i = 0; i < oderData.length; i++) {
//                OrderResp temp = oderData[i];
//                Tboder tboder = new Tboder();
//                tboder.setAdzoneId(Long.valueOf(temp.getString("adzone_id")));
//                tboder.setAdzoneName(temp.getString("adzone_name"));
//                tboder.setAlipayTotalPrice(temp.getString("alipay_total_price"));
//                tboder.setNumIid(temp.getLong("num_iid"));
//                tboder.setCommissionRate(temp.getString("commission_rate"));
//                tboder.setCommission(Double.valueOf(temp.getString("commission")));
//                tboder.setItemNum(temp.getLong("item_num"));
//                tboder.setItemTitle(temp.getString("item_title"));
//                tboder.setOrderType(temp.getString("order_type"));
//                tboder.setPrice(temp.getString("price"));
//                tboder.setPayPrice(temp.getString("pay_price"));
//                tboder.setTkStatus(temp.getInteger("tk_status"));
//                tboder.setSiteId(temp.getString("site_id"));
//                tboder.setSiteName(temp.getString("site_name"));
//                tboder.setTotalCommissionRate(temp.getString("total_commission_rate"));
//                tboder.setTradeId(temp.getLong("trade_id"));
//                tboder.setTradeParentId(temp.getLong("trade_parent_id"));
//                tboder.setOdercreateTime(temp.getDate("create_time"));
//                Integer is = tbOderDao.findIs(tboder.getTradeId());
//                if (is == null || is == 0) {
//                    Userinfo var1 = new Userinfo();
//                    var1.setTbpid(tboder.getAdzoneId());
//                    Userinfo userinfo = tbOderDao.queryUserForPid(var1);
//                    SysJhAdviceOder var = new SysJhAdviceOder();
//                    var.setName(tboder.getItemTitle());
//                    var.setUserName(userinfo.getUsername());
//                    var.setOdersn(tboder.getTradeId());
//                    var.setPid(tboder.getAdzoneId().toString());
//                    var.setOrderStatus(StatusUtils.getStatus(tboder.getTkStatus(), 0));
//                    var.setSrc(1);
//                    var.setUserid(userinfo.getId().intValue());
//                    var.setSrcName("淘宝");
//                    var.setOderCreatetime(tboder.getOdercreateTime());
//                    var.setOrderStatusDesc(StatusUtils.getStatusDesc(tboder.getTkStatus(), 0));
//                    tbOderDao.insert(tboder);
//                    tbOderDao.saveTbOderAdvice(var);
//                }
//                if (is != null && is >= 1) {
//                    tbOderDao.oderUpdate(tboder);
//                }
//
//            }
//            PAGE_NO++;
//            PAGE_SIZE += 3;
//            pddOderDao.scanLog(day, "淘宝");
//        }
//
//    }
//
//}
