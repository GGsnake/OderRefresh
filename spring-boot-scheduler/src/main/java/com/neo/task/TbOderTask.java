package com.neo.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neo.Utils.EveryUtils;
import com.neo.Utils.NetUtils;
import com.neo.Utils.StatusUtils;
import com.neo.dao.PddOderDao;
import com.neo.dao.TbOderDao;
import com.neo.dao.UserInfoDao;
import com.neo.model.SysJhAdviceOder;
import com.neo.model.TboderBean;
import com.neo.model.Userinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sun.rmi.runtime.Log;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liujupeng on 2019/1/11.
 */
@lombok.extern.java.Log
@Component
public class TbOderTask {
    @Autowired
    private TbOderDao tbOderDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PddOderDao pddOderDao;
    @Value("${miao.apkey}")
    private String apkey;
    @Value("${miao.tburl}")
    private String url;
    private static final String SPAN = "1200";
    private static Long start_sys = System.currentTimeMillis() / 1000 - 1200;
    //订单落库
    @Scheduled(fixedRate = 25000)
    public void taobaoOder() {
        Long tempTime = start_sys;
        start_sys += 1200;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String day = dateFormat.format(tempTime * 1000);
        String urlEncoderString = EveryUtils.getURLEncoderString(day);
        Integer PAGE_SIZE = 5;
        Integer PAGE_NO = 1;
        while (true) {
            String tburl = url + "gettkorder?";
            Map<String, String> urlSign = new HashMap<>();
            urlSign.put("apkey", apkey);
//            urlSign.put("starttime", urlEncoderString);
            urlSign.put("starttime", "2019-01-22+15%3a48%3a22");
            urlSign.put("span", SPAN);
            urlSign.put("ordertype", "create_time");
            urlSign.put("tbname", "华众有个店");
            urlSign.put("tkstatus", "1");
            urlSign.put("page", PAGE_NO.toString());
            urlSign.put("pagesize", PAGE_SIZE.toString());
            String linkStringByGet = null;
            try {
                linkStringByGet = NetUtils.createLinkStringByGet(urlSign);
                String res = restTemplate.getForObject(tburl + linkStringByGet, String.class);
                JSONObject resJson = JSON.parseObject(res);
                if (resJson.getInteger("code")==200){
                    JSONArray data = resJson.getJSONObject("data").getJSONArray("n_tbk_order");
                    log.warning(resJson.toJSONString());
                    if (data == null || data.size() == 0) {
                        break;
                    }
                    PAGE_NO++;
                    for (int i = 0; i < data.size(); i++) {
                        JSONObject temp = (JSONObject) data.get(i);
                        TboderBean tboder = new TboderBean();
                        tboder.setAdzone_id(Long.valueOf(temp.getString("adzone_id")));
                        tboder.setAdzoneName(temp.getString("adzone_name"));
                        tboder.setAlipayTotalPrice(temp.getString("alipay_total_price"));
                        tboder.setNumIid(temp.getLong("num_iid"));
                        tboder.setCommissionRate(temp.getString("commission_rate"));
                        tboder.setCommission(Double.valueOf(temp.getString("commission")));
                        tboder.setItemNum(temp.getLong("item_num"));
                        tboder.setItemTitle(temp.getString("item_title"));
                        tboder.setOrderType(temp.getString("order_type"));
                        tboder.setPrice(temp.getString("price"));
                        tboder.setPayPrice(temp.getString("pay_price"));
                        tboder.setTkStatus(temp.getInteger("tk_status"));
                        tboder.setSiteId(temp.getString("site_id"));
                        tboder.setSiteName(temp.getString("site_name"));
                        tboder.setTotalCommissionRate(temp.getString("total_commission_rate"));
                        tboder.setTrade_id(temp.getLong("trade_id"));
                        tboder.setTradeParentId(temp.getLong("trade_parent_id"));
                        tboder.setOdercreateTime(temp.getDate("create_time"));
                        Integer is = tbOderDao.findIs(tboder.getTrade_id());
                        if (is == null || is == 0) {
                            Userinfo var1 = new Userinfo();
                            var1.setTbPid(tboder.getAdzone_id());
                            Userinfo userinfo = userInfoDao.queryPidUser(var1);
                            if (userinfo==null){
                                log.warning("淘宝订单id:"+tboder.getTrade_id()+"--推广位："+tboder.getAdzone_id()+"同步失败--订单同步时间="+new Date().toString());
                                continue;
                            }
                            SysJhAdviceOder var = new SysJhAdviceOder();
                            var.setName(tboder.getItemTitle());
                            var.setUserName(userinfo.getUsername());
                            var.setOdersn(tboder.getTrade_id().toString());
                            var.setPid(tboder.getAdzone_id().toString());
                            var.setOrderStatus(StatusUtils.getStatus(tboder.getTkStatus(), 0));
                            var.setSrc(1);
                            var.setUserid(userinfo.getId().intValue());
                            var.setSrcName("淘宝");
                            var.setOderCreatetime(tboder.getOdercreateTime());
                            var.setOrderStatusDesc(StatusUtils.getStatusDesc(tboder.getTkStatus(), 0));
                            tbOderDao.insert(tboder);
                            tbOderDao.saveTbOderAdvice(var);
                        }
                        if (is != null && is >= 1) {
                            tbOderDao.oderUpdate(tboder);
                        }

                    }
                    pddOderDao.scanLog(day, "淘宝");
                    break;
                }
               else {
                   break;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

    }

}
