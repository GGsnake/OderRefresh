//package com.neo.task;
//
//import com.neo.dao.PddOderDao;
//import com.neo.dao.TbOderDao;
//import com.neo.model.PddOderBean;
//import com.neo.model.TboderBean;
//import com.neo.service.SettleService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class SettlePddTask {
//    @Autowired
//    private  PddOderDao pddOderDao;
//    @Autowired
//    private TbOderDao tbOderDao;
//    @Autowired
//    private SettleService settleService;
//    //拼多多订单结算
//    @Scheduled(fixedRate = 5000)
//    public void settlePdd() {
//        List<PddOderBean> data = pddOderDao.scanPddOder();
//        for (int i = 0; i < data.size(); i++) {
//            settleService.pddSettle( data.get(i));
//
//        }
//    }
//
//
//    //淘宝订单结算
//    @Scheduled(fixedRate = 5000)
//    public void settleTaoBao() {
//        List<TboderBean> data = tbOderDao.scanTaoBaoOder();
//        for (int i = 0; i < data.size(); i++) {
//            settleService.taoBaoSettle(data.get(i));
//        }
//    }
//
//    //京东订单结算
//    @Scheduled(fixedRate = 5000)
//    public void settleJd() {
//        List<TboderBean> data = tbOderDao.scanTaoBaoOder();
//        for (int i = 0; i < data.size(); i++) {
//            settleService.taoBaoSettle(data.get(i));
//        }
//    }
//}
