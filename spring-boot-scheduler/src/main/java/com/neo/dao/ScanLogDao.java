package com.neo.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

import com.neo.model.ScanLog;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ScanLogDao {

    @Insert("insert into jh_oder_scan(lastTime,src,devName) values(#{lastTime} ,#{src},#{devName})")
    Integer scanLog(ScanLog scanLog);

    @Select("select MAX(lastTime) from jh_oder_scan where src=#{src} ")
    Date getLastTime(ScanLog scanLog);
}
