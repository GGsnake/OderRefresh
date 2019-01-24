package com.neo.service;

import java.util.List;
import com.neo.model.ScanLog;
public interface ScanLogService{

    int insert(ScanLog scanLog);

    int insertSelective(ScanLog scanLog);

    int insertList(List<ScanLog> scanLogs);

    int updateByPrimaryKeySelective(ScanLog scanLog);
}
