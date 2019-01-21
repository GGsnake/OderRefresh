package com.neo.service.impl;

import com.neo.Utils.CommissionUtils;
import com.neo.dao.*;
//import com.neo.dao.PddOderDao;
import com.neo.jsonbean.JdJson.Jdoder;
import com.neo.model.PddOderBean;
import com.neo.model.SysJhCashLog;
import com.neo.model.TboderBean;
import com.neo.model.Userinfo;
import com.neo.service.SettleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("settleService")
public class SettleServiceImpl implements SettleService {
    @Autowired
    private PddOderDao pddOderDao;
    @Autowired
    private TbOderDao tbOderDao;
    @Autowired
    private JdOderDao jdOderDao;
    @Autowired
    private AgentDao agentDao;
    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private CashDao cashDao;

    @Value("${juanhuang.range}")
    private Integer RANGE;

    @Transactional
    public Boolean pddSettle(PddOderBean pddOderBean) {
        Long nowTime = System.currentTimeMillis() / 1000;
        String p_id = pddOderBean.getP_id();
        Userinfo userinfo = new Userinfo();
        userinfo.setPddPid(p_id);
        Userinfo var = userInfoDao.queryPidUser(userinfo);
        Integer roleId = var.getRoleId();
        Integer uid = var.getId().intValue();
        if (roleId == 1) {
            Double pddCommission = CommissionUtils.pddCommission(100, RANGE, pddOderBean.getPromotion_amount());
            SysJhCashLog agent = new SysJhCashLog();
            agent.setUserid(uid).setSrc("拼多多").setAmount(pddCommission).setUsername(var.getUsername()).setRole("运营商");
            agent.setOdersn(pddOderBean.getOrder_sn());
            agent.setScore(100).setSettletime(nowTime);
            Integer isCash = cashDao.addCashLog(agent);
            if (isCash == 0) {
                throw new RuntimeException("订单结算==" + pddOderBean.getOrder_sn() + "-结算失败");
            }
            PddOderBean pd = new PddOderBean();
            pd.setSettle(1);
            pd.setId(pddOderBean.getId());
            Integer fa = pddOderDao.pddOderUpdate(pd);
            if (fa == 0) {
                throw new RuntimeException("订单结算==" + pddOderBean.getOrder_sn() + "-结算失败");
            }
        }

        if (roleId == 2) {
            //上级代理的佣金
            Double pddCommission = CommissionUtils.pddCommission(var.getScore(), RANGE, pddOderBean.getPromotion_amount());
            SysJhCashLog agent = new SysJhCashLog().setSrc("拼多多").setAmount(pddCommission).setScore(var.getScore());
            agent.setUsername(var.getUsername()).setUserid(uid).setRole("代理");
            agent.setOdersn(pddOderBean.getOrder_sn());
            agent.setSettletime(nowTime);
            Integer isCash = cashDao.addCashLog(agent);
            if (isCash == 0) {
                throw new RuntimeException("订单结算==" + pddOderBean.getOrder_sn() + "-结算失败");
            }
            //运营商Id
            Integer godId = agentDao.queryForAgentIdString(uid);
            Userinfo temp1 = new Userinfo();
            temp1.setId(Long.valueOf(godId));
            Userinfo var2 = userInfoDao.queryPidUser(temp1);
            Double pddGodCommission = CommissionUtils.pddCommission(100 - var.getScore(), RANGE, pddOderBean.getPromotion_amount());
            SysJhCashLog godUser = new SysJhCashLog();
            godUser.setUserid(var2.getId().intValue()).setSrc("拼多多").setAmount(pddGodCommission).setUsername(var2.getUsername()).setRole("运营商");
            godUser.setOdersn(pddOderBean.getOrder_sn());
            godUser.setScore(100 - var.getScore()).setSettletime(nowTime);
            Integer isCash1 = cashDao.addCashLog(godUser);
            if (isCash1 == 0) {
                throw new RuntimeException("订单结算==" + pddOderBean.getOrder_sn() + "-结算失败");
            }
            PddOderBean pd = new PddOderBean();
            pd.setSettle(1);
            pd.setId(pddOderBean.getId());
            Integer fa = pddOderDao.pddOderUpdate(pd);
            if (fa == 0) {
                throw new RuntimeException("订单结算==" + pddOderBean.getOrder_sn() + "-结算失败");
            }
            return true;
        }

        if (roleId == 3) {
            //查询是否有上级
            Integer agId = agentDao.queryForAgentIdString(uid);
            if (agId == null) {
                return false;
            }
            Userinfo temp = new Userinfo();
            temp.setId(Long.valueOf(agId));
            //上级的用户信息
            Userinfo var1 = userInfoDao.queryPidUser(temp);
            Integer roleId1 = var1.getRoleId();
            if (roleId1 == 1) {
                //上级代理的佣金
                Double pddCommission = CommissionUtils.pddCommission(100, RANGE, pddOderBean.getPromotion_amount());
                SysJhCashLog agent = new SysJhCashLog();
                agent.setUserid(agId).setSrc("拼多多").setAmount(pddCommission).setUsername(var1.getUsername()).setRole("运营商");
                agent.setOdersn(pddOderBean.getOrder_sn());
                agent.setScore(100).setSettletime(nowTime);
                Integer isCash = cashDao.addCashLog(agent);
                if (isCash == 0) {
                    throw new RuntimeException("订单结算==" + pddOderBean.getOrder_sn() + "-结算失败");
                }
                PddOderBean pd = new PddOderBean();
                pd.setSettle(1);
                pd.setId(pddOderBean.getId());
                Integer fa = pddOderDao.pddOderUpdate(pd);
                if (fa == 0) {
                    throw new RuntimeException("订单结算==" + pddOderBean.getOrder_sn() + "-结算失败");
                }
                return true;
            }
            if (roleId1 == 2) {
                //上级代理的佣金
                Double pddCommission = CommissionUtils.pddCommission(var1.getScore(), RANGE, pddOderBean.getPromotion_amount());
                SysJhCashLog agent = new SysJhCashLog();
                agent.setUserid(agId);
                agent.setSrc("拼多多");
                agent.setAmount(pddCommission);
                agent.setUsername(var1.getUsername());
                agent.setRole("代理");
                agent.setOdersn(pddOderBean.getOrder_sn());
                agent.setScore(var1.getScore());
                agent.setSettletime(nowTime);
                Integer isCash = cashDao.addCashLog(agent);
                if (isCash == 0) {
                    throw new RuntimeException("订单结算==" + pddOderBean.getOrder_sn() + "-结算失败");
                }
                //运营商Id
                Integer godId = agentDao.queryForAgentIdString(var1.getId().intValue());
                Userinfo temp1 = new Userinfo();
                temp1.setId(Long.valueOf(godId));
                Userinfo var2 = userInfoDao.queryPidUser(temp1);
                Double pddGodCommission = CommissionUtils.pddCommission(100 - var1.getScore(), RANGE, pddOderBean.getPromotion_amount());
                SysJhCashLog godUser = new SysJhCashLog().setSrc("拼多多");
                godUser.setAmount(pddGodCommission).setUsername(var2.getUsername()).setRole("运营商");
                godUser.setOdersn(pddOderBean.getOrder_sn());
                godUser.setScore(100 - var1.getScore()).setUserid(godId);
                godUser.setSettletime(nowTime);
                Integer isCash1 = cashDao.addCashLog(godUser);
                if (isCash1 == 0) {
                    throw new RuntimeException("订单结算==" + pddOderBean.getOrder_sn() + "-结算失败");
                }
                PddOderBean pd = new PddOderBean();
                pd.setSettle(1);
                pd.setId(pddOderBean.getId());
                Integer fa = pddOderDao.pddOderUpdate(pd);
                if (fa == 0) {
                    throw new RuntimeException("订单结算==" + pddOderBean.getOrder_sn() + "-结算失败");
                }
                return true;

            }

        }
        return null;
    }

    @Transactional
    public Boolean taoBaoSettle(TboderBean tboderBean) {
        Long nowTime = System.currentTimeMillis() / 1000;
        Long p_id = tboderBean.getAdzone_id();
        Userinfo userinfo = new Userinfo();
        userinfo.setTbPid(p_id);
        Userinfo var = userInfoDao.queryPidUser(userinfo);
        Integer roleId = var.getRoleId();
        Integer uid = var.getId().intValue();
        if (roleId == 1) {
            Double pddCommission = CommissionUtils.taoBaoCommission(100, RANGE,tboderBean.getCommission());
            SysJhCashLog agent = new SysJhCashLog();
            agent.setUserid(uid).setSrc("淘宝").setAmount(pddCommission).setUsername(var.getUsername()).setRole("运营商");
            agent.setOdersn(tboderBean.getTrade_id().toString());
            agent.setScore(100).setSettletime(nowTime);
            Integer isCash = cashDao.addCashLog(agent);
            if (isCash == 0) {
                throw new RuntimeException("订单结算==" + tboderBean.getTrade_id() + "-结算失败");
            }
            TboderBean pd = new TboderBean();
            pd.setSettle(1);
            pd.setId(tboderBean.getId());
            Integer fa = tbOderDao.taoBaoUpdate(pd);
            if (fa == 0) {
                throw new RuntimeException("订单结算==" + tboderBean.getTrade_id() + "-结算失败");
            }
        }

        if (roleId == 2) {
            //上级代理的佣金
            Double pddCommission = CommissionUtils.taoBaoCommission(var.getScore(), RANGE, tboderBean.getCommission());
            SysJhCashLog agent = new SysJhCashLog().setSrc("淘宝").setAmount(pddCommission).setScore(var.getScore());
            agent.setUsername(var.getUsername()).setUserid(uid).setRole("代理");
            agent.setOdersn(tboderBean.getTrade_id().toString());
            agent.setSettletime(nowTime);
            Integer isCash = cashDao.addCashLog(agent);
            if (isCash == 0) {
                throw new RuntimeException("订单结算==" + tboderBean.getTrade_id()+ "-结算失败");
            }
            //运营商Id
            Integer godId = agentDao.queryForAgentIdString(uid);
            Userinfo temp1 = new Userinfo();
            temp1.setId(Long.valueOf(godId));
            Userinfo var2 = userInfoDao.queryPidUser(temp1);
            Double pddGodCommission = CommissionUtils.taoBaoCommission(100 - var.getScore(), RANGE, tboderBean.getCommission());
            SysJhCashLog godUser = new SysJhCashLog();
            godUser.setUserid(var2.getId().intValue()).setSrc("淘宝").setAmount(pddGodCommission).setUsername(var2.getUsername()).setRole("运营商");
            godUser.setOdersn(tboderBean.getTrade_id().toString());
            godUser.setScore(100 - var.getScore()).setSettletime(nowTime);
            Integer isCash1 = cashDao.addCashLog(godUser);
            if (isCash1 == 0) {
                throw new RuntimeException("订单结算==" + tboderBean.getTrade_id() + "-结算失败");
            }
            TboderBean pd = new TboderBean();
            pd.setSettle(1);
            pd.setId(tboderBean.getId());
            Integer fa = tbOderDao.taoBaoUpdate(pd);
            if (fa == 0) {
                throw new RuntimeException("订单结算==" + tboderBean.getTrade_id()+ "-结算失败");
            }
            return true;
        }

        if (roleId == 3) {
            //查询是否有上级
            Integer agId = agentDao.queryForAgentIdString(uid);
            if (agId == null) {
                return false;
            }
            Userinfo temp = new Userinfo();
            temp.setId(Long.valueOf(agId));
            //上级的用户信息
            Userinfo var1 = userInfoDao.queryPidUser(temp);
            Integer roleId1 = var1.getRoleId();
            if (roleId1 == 1) {
                //上级代理的佣金
                Double pddCommission = CommissionUtils.taoBaoCommission(100, RANGE, tboderBean.getCommission());
                SysJhCashLog agent = new SysJhCashLog();
                agent.setUserid(agId).setSrc("淘宝").setAmount(pddCommission).setUsername(var1.getUsername()).setRole("运营商");
                agent.setOdersn(tboderBean.getTrade_id().toString());
                agent.setScore(100).setSettletime(nowTime);
                Integer isCash = cashDao.addCashLog(agent);
                if (isCash == 0) {
                    throw new RuntimeException("订单结算==" + tboderBean.getTrade_id() + "-结算失败");
                }
                TboderBean pd = new TboderBean();
                pd.setSettle(1);
                pd.setId(tboderBean.getId());
                Integer fa = tbOderDao.taoBaoUpdate(pd);
                if (fa == 0) {
                    throw new RuntimeException("订单结算==" + tboderBean.getTrade_id() + "-结算失败");
                }
                return true;
            }
            if (roleId1 == 2) {
                //上级代理的佣金
                Double pddCommission = CommissionUtils.taoBaoCommission(var1.getScore(), RANGE, tboderBean.getCommission());
                SysJhCashLog agent = new SysJhCashLog();
                agent.setUserid(agId);
                agent.setSrc("淘宝");
                agent.setAmount(pddCommission);
                agent.setUsername(var1.getUsername());
                agent.setRole("代理");
                agent.setOdersn(tboderBean.getTrade_id().toString());
                agent.setScore(var1.getScore());
                agent.setSettletime(nowTime);
                Integer isCash = cashDao.addCashLog(agent);
                if (isCash == 0) {
                    throw new RuntimeException("订单结算==" + tboderBean.getTrade_id() + "-结算失败");
                }
                //运营商Id
                Integer godId = agentDao.queryForAgentIdString(var1.getId().intValue());
                Userinfo temp1 = new Userinfo();
                temp1.setId(Long.valueOf(godId));
                Userinfo var2 = userInfoDao.queryPidUser(temp1);
                Double pddGodCommission = CommissionUtils.taoBaoCommission(100 - var1.getScore(), RANGE, tboderBean.getCommission());
                SysJhCashLog godUser = new SysJhCashLog().setSrc("淘宝");
                godUser.setAmount(pddGodCommission).setUsername(var2.getUsername()).setRole("运营商");
                godUser.setOdersn(tboderBean.getTrade_id().toString());
                godUser.setScore(100 - var1.getScore()).setUserid(godId);
                godUser.setSettletime(nowTime);
                Integer isCash1 = cashDao.addCashLog(godUser);
                if (isCash1 == 0) {
                    throw new RuntimeException("订单结算==" + tboderBean.getTrade_id().toString() + "-结算失败");
                }
                TboderBean pd = new TboderBean();
                pd.setSettle(1);
                pd.setId(tboderBean.getId());
                Integer fa = tbOderDao.taoBaoUpdate(pd);
                if (fa == 0) {
                    throw new RuntimeException("订单结算==" + tboderBean.getTrade_id().toString() + "-结算失败");
                }
                return true;

            }

        }
        return null;
    }
    @Transactional
    public Boolean jdSettle(Jdoder jdoder)  {
        Long nowTime = System.currentTimeMillis() / 1000;
        String p_id = jdoder.getPositionid();
        Userinfo userinfo = new Userinfo();
        userinfo.setJdPid(p_id);
        Userinfo var = userInfoDao.queryPidUser(userinfo);
        Integer roleId = var.getRoleId();
        Integer uid = var.getId().intValue();
        Double  commission = jdoder.getActualcosprice().doubleValue();
        Long orderid = jdoder.getOrderid();
        if (roleId == 1) {
            Double pddCommission = CommissionUtils.taoBaoCommission(100, RANGE,commission);
            SysJhCashLog agent = new SysJhCashLog();
            agent.setUserid(uid).setSrc("京东").setAmount(pddCommission).setUsername(var.getUsername()).setRole("运营商");
            agent.setOdersn(orderid.toString());
            agent.setScore(100).setSettletime(nowTime);
            Integer isCash = cashDao.addCashLog(agent);
            if (isCash == 0) {
                throw new RuntimeException("订单结算==" +orderid + "-结算失败");
            }
            Jdoder pd = new Jdoder();
            pd.setSettle(1);
            pd.setId(jdoder.getId());
            Integer fa = jdOderDao.jdOderUpdate(pd);
            if (fa == 0) {
                throw new RuntimeException("订单结算==" +orderid + "-结算失败");
            }
        }

        if (roleId == 2) {
            //上级代理的佣金
            Double pddCommission = CommissionUtils.taoBaoCommission(var.getScore(), RANGE,commission);
            SysJhCashLog agent = new SysJhCashLog().setSrc("京东").setAmount(pddCommission).setScore(var.getScore());
            agent.setUsername(var.getUsername()).setUserid(uid).setRole("代理");
            agent.setOdersn(orderid.toString());
            agent.setSettletime(nowTime);
            Integer isCash = cashDao.addCashLog(agent);
            if (isCash == 0) {
                throw new RuntimeException("订单结算==" + orderid+ "-结算失败");
            }
            //运营商Id
            Integer godId = agentDao.queryForAgentIdString(uid);
            Userinfo temp1 = new Userinfo();
            temp1.setId(Long.valueOf(godId));
            Userinfo var2 = userInfoDao.queryPidUser(temp1);
            Double pddGodCommission = CommissionUtils.taoBaoCommission(100 - var.getScore(), RANGE,commission);
            SysJhCashLog godUser = new SysJhCashLog();
            godUser.setUserid(var2.getId().intValue()).setSrc("京东").setAmount(pddGodCommission).setUsername(var2.getUsername()).setRole("运营商");
            godUser.setOdersn(orderid.toString());
            godUser.setScore(100 - var.getScore()).setSettletime(nowTime);
            Integer isCash1 = cashDao.addCashLog(godUser);
            if (isCash1 == 0) {
                throw new RuntimeException("订单结算==" + orderid + "-结算失败");
            }
            Jdoder pd = new Jdoder();
            pd.setSettle(1);
            pd.setId(jdoder.getId());
            Integer fa = jdOderDao.jdOderUpdate(pd);
            if (fa == 0) {
                throw new RuntimeException("订单结算==" +orderid+ "-结算失败");
            }
            return true;
        }

        if (roleId == 3) {
            //查询是否有上级
            Integer agId = agentDao.queryForAgentIdString(uid);
            if (agId == null) {
                return false;
            }
            Userinfo temp = new Userinfo();
            temp.setId(Long.valueOf(agId));
            //上级的用户信息
            Userinfo var1 = userInfoDao.queryPidUser(temp);
            Integer roleId1 = var1.getRoleId();
            if (roleId1 == 1) {
                //上级代理的佣金
                Double pddCommission = CommissionUtils.taoBaoCommission(100, RANGE,commission);
                SysJhCashLog agent = new SysJhCashLog();
                agent.setUserid(agId).setSrc("京东").setAmount(pddCommission).setUsername(var1.getUsername()).setRole("运营商");
                agent.setOdersn(orderid.toString());
                agent.setScore(100).setSettletime(nowTime);
                Integer isCash = cashDao.addCashLog(agent);
                if (isCash == 0) {
                    throw new RuntimeException("订单结算==" +orderid + "-结算失败");
                }
                Jdoder pd = new Jdoder();
                pd.setSettle(1);
                pd.setId(jdoder.getId());
                Integer fa = jdOderDao.jdOderUpdate(pd);
                if (fa == 0) {
                    throw new RuntimeException("订单结算==" + orderid + "-结算失败");
                }
                return true;
            }
            if (roleId1 == 2) {
                //上级代理的佣金
                Double pddCommission = CommissionUtils.taoBaoCommission(var1.getScore(), RANGE, commission);
                SysJhCashLog agent = new SysJhCashLog();
                agent.setUserid(agId);
                agent.setSrc("京东");
                agent.setAmount(pddCommission);
                agent.setUsername(var1.getUsername());
                agent.setRole("代理");
                agent.setOdersn(orderid.toString());
                agent.setScore(var1.getScore());
                agent.setSettletime(nowTime);
                Integer isCash = cashDao.addCashLog(agent);
                if (isCash == 0) {
                    throw new RuntimeException("订单结算==" + orderid + "-结算失败");
                }
                //运营商Id
                Integer godId = agentDao.queryForAgentIdString(var1.getId().intValue());
                Userinfo temp1 = new Userinfo();
                temp1.setId(Long.valueOf(godId));
                Userinfo var2 = userInfoDao.queryPidUser(temp1);
                Double pddGodCommission = CommissionUtils.taoBaoCommission(100 - var1.getScore(), RANGE,commission);
                SysJhCashLog godUser = new SysJhCashLog().setSrc("京东");
                godUser.setAmount(pddGodCommission).setUsername(var2.getUsername()).setRole("运营商");
                godUser.setOdersn(orderid.toString());
                godUser.setScore(100 - var1.getScore()).setUserid(godId);
                godUser.setSettletime(nowTime);
                Integer isCash1 = cashDao.addCashLog(godUser);
                if (isCash1 == 0) {
                    throw new RuntimeException("订单结算==" +orderid+ "-结算失败");
                }
                Jdoder pd = new Jdoder();
                pd.setSettle(1);
                pd.setId(jdoder.getId());
                Integer fa = jdOderDao.jdOderUpdate(pd);
                if (fa == 0) {
                    throw new RuntimeException("订单结算==" + orderid + "-结算失败");
                }
                return true;

            }

        }
        return null;
    }
}
