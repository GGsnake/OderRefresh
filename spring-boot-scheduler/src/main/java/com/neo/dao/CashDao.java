package com.neo.dao;

import com.neo.model.SysJhCashLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CashDao {
    /**
     * 增加结算记录
     * @param sysJhCashLog
     * @return
     */
    Integer addCashLog(SysJhCashLog sysJhCashLog);
}
