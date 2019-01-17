package com.neo.dao;

import com.neo.jsonbean.PddOderBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by liujupeng on 2018/11/16.
 */
@Mapper
public interface JdOderDao {
    Integer addOder(@Param("oder") PddOderBean pddOderBean);

    @Insert("insert into scanlog(lastTime,createTime,devName) values(#{date},now(),#{dev})")
    void scanLog(@Param("date") String date, @Param("dev") String dev);

//
//    @Select("select id from oder where order_sn=#{sn} limit 1")
//     findIs(String sn);

    /**
     * 更新拼多多的订单
     *
     * @param pddOderBean
     * @return
     */
    Integer pddOderUpdate(PddOderBean pddOderBean);
}
