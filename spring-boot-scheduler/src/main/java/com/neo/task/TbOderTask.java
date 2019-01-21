//package com.neo.task;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.neo.Utils.HttpDeal;
//import com.neo.Utils.NetUtils;
//import com.neo.Utils.StatusUtils;
//import com.neo.dao.PddOderDao;
//import com.neo.dao.TbOderDao;
//import com.neo.dao.UserInfoDao;
//import com.neo.model.SysJhAdviceOder;
//import com.neo.model.TboderBean;
//import com.neo.model.Userinfo;
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
//public class TbOderTask {
//    @Autowired
//    private TbOderDao tbOderDao;
//    @Autowired
//    private UserInfoDao userInfoDao;
//    @Autowired
//    private PddOderDao pddOderDao;
//    @Value("${miao.apkey}")
//    private String apkey;
//    @Value("${miao.url}")
//    private String url;
//    private static final String SPAN = "1200";
//    private static Long start_sys = System.currentTimeMillis() - 1200000;
//
//    private final static Logger logger = LoggerFactory.getLogger(TbOderTask.class);
//
//    //订单落库
//    @Scheduled(fixedRate = 19000)
//    public void taobaoOder() throws MalformedURLException, URISyntaxException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM- dd HH:mm:ss");
//        Long tempTime = start_sys;
//        start_sys += 1200;
//        String day = dateFormat.format(tempTime);
//        Integer PAGE_SIZE = 5;
//        Integer PAGE_NO = 1;
//        while (true) {
//            String jdurl = url + "getjdunionorders?";
//            Map<String, String> urlSign = new HashMap<>();
//            urlSign.put("apkey", apkey);
//            urlSign.put("time", yyyyMMddHHmm);
////            urlSign.put("time", "201901181015");
//            urlSign.put("type", "3");
//            urlSign.put("pageNo", PAGE_NO.toString());
//            urlSign.put("pageSize", PAGE_SIZE.toString());
//            String linkStringByGet = null;
//
//
//            Map<String, String> urlSign = new HashMap<>();
//            urlSign.put("apkey", TAOBAOAPPKEY);
//            urlSign.put("appsecret", TAOBAOAPPSECRET);
////        urlSign.put("start_time", day);
//            urlSign.put("start_time", "2018-12-15 18:50:16");
//            urlSign.put("span", SPAN);
//            try {
//                linkStringByGet = NetUtils.createLinkStringByGet(urlSign);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//
//            String res = HttpDeal.get(TAOBAOURL + linkStringByGet);
//            JSONArray data = JSON.parseObject(res).getJSONArray("data");
//            if (data == null || data.size() == 0) {
//                break;
//            }
//            PAGE_NO++;
//            PAGE_SIZE += 3;
//            for (int i = 0; i < data.size(); i++) {
//                JSONObject temp = (JSONObject) data.get(i);
//                TboderBean tboder = new TboderBean();
//                tboder.setAdzone_id(Long.valueOf(temp.getString("adzone_id")));
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
//                tboder.setTrade_id(temp.getLong("trade_id"));
//                tboder.setTradeParentId(temp.getLong("trade_parent_id"));
//                tboder.setOdercreateTime(temp.getDate("create_time"));
//                Integer is = tbOderDao.findIs(tboder.getTrade_id());
//                if (is==null||is == 0) {
//                    Userinfo var1=new Userinfo();
//                    var1.setTbPid(tboder.getAdzone_id());
//                    Userinfo userinfo = userInfoDao.queryPidUser(var1);
//                    SysJhAdviceOder var = new SysJhAdviceOder();
//                    var.setName(tboder.getItemTitle());
//                    var.setUserName(userinfo.getUsername());
//                    var.setOdersn(tboder.getTrade_id().toString());
//                    var.setPid(tboder.getAdzone_id().toString());
//                    var.setOrderStatus(StatusUtils.getStatus(tboder.getTkStatus(),0));
//                    var.setSrc(1);
//                    var.setUserid(userinfo.getId().intValue());
//                    var.setSrcName("淘宝");
//                    var.setOderCreatetime(tboder.getOdercreateTime());
//                    var.setOrderStatusDesc(StatusUtils.getStatusDesc(tboder.getTkStatus(),0));
//                    tbOderDao.insert(tboder);
//                    tbOderDao.saveTbOderAdvice(var);
//                }
//                if (is!=null&&is >= 1) {
//                    tbOderDao.oderUpdate(tboder);
//                }
//
//            }
//            pddOderDao.scanLog(day, "淘宝");
//        }
//
//    }
//
//}
