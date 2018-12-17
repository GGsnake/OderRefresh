package com.neo.dao;

import com.neo.jsonbean.PddOderBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by liujupeng on 2018/11/16.
 */
@Mapper
public interface PddOderDao {
   void addOder(@Param("oder") PddOderBean pddOderBean);
   @Insert("insert into scanlog(lastTime,createTime,devName) values(#{date},now(),#{dev})")
   void scanLog(@Param("date") String date,@Param("dev") String dev);
}
