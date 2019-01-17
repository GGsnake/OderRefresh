package com.neo.task;

import com.jd.open.api.sdk.JdException;
import com.neo.dao.AgentDao;
import com.neo.dao.PddOderDao;
import com.neo.dao.UserInfoDao;
import com.neo.jsonbean.PddOderBean;
import com.neo.model.Agent;
import com.neo.model.Userinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SettlePddTask {
    @Autowired
    private PddOderDao pddOderDao;
    @Autowired
    private AgentDao agentDao;
    @Autowired
    private UserInfoDao userInfoDao;
    //订单落库
    @Scheduled(fixedRate = 10000)
    public void settle() {
        List<PddOderBean> data = pddOderDao.scanPddOder();
        for (int i = 0; i < data.size(); i++) {
            Userinfo userinfo=new Userinfo();
            PddOderBean pddOderBean = data.get(i);
            String p_id =pddOderBean.getP_id();
            userinfo.setPddpid(p_id);
            Userinfo var  = userInfoDao.queryPidUser(userinfo);
            Integer roleId = var.getRoleId();
            Integer uid = var.getId().intValue();
            if (roleId==1){

            }

            if (roleId==2){

            }

            if (roleId==3){
                //查询是否有上级
                String agId = agentDao.queryForAgentIdString(uid);
                if (agId==null){
                    continue;
                }
                Userinfo temp=new Userinfo();
                temp.setId(Long.valueOf(agId));
                Userinfo var1  = userInfoDao.queryPidUser(temp);
                Integer roleId1 = var1.getRoleId();
                if (roleId1==1){

                }
                if (roleId1==2){
                    //分成比例
                    Double score = Double.valueOf(var1.getScore()/100);
                    //佣金金额
                    Integer promotion_amount = pddOderBean.getPromotion_amount();

                    String godId = agentDao.queryForAgentIdString(uid);
                    Userinfo temp1=new Userinfo();
                    temp1.setId(Long.valueOf(agId));
                    Userinfo var2  = userInfoDao.queryPidUser(temp1);

                }

            }

        }
    }
}
