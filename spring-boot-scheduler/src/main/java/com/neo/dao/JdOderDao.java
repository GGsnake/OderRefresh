package com.neo.dao;

import com.neo.jsonbean.JdJson.Jdoder;
import com.neo.model.JdOderBean;
import com.neo.model.PddOderBean;
import com.neo.model.TboderBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/16.
 */
@Mapper
public interface JdOderDao {
    /**
     * 更新京东的订单
     *
     * @param jdOderBean
     * @return
     */
    Integer addOder(JdOderBean jdOderBean);

    @Insert("insert into scanlog(lastTime,createTime,devName) values(#{date},now(),#{dev})")
    void scanLog(@Param("date") String date, @Param("dev") String dev);


    /**
     * 扫描京东已结算订单
     *
     * @param
     * @return
     */
    @Select("select * from jdoder where validCode=18 and settle=0")
    List<Jdoder> scanJdOder();



    @Select("select id from jdoder where orderId=#{id}")
    Integer findIs(Long id);
//
//    @Select("select id from oder where order_sn=#{sn} limit 1")
//     findIs(String sn);

    /**
     * 更新京东的订单
     *
     * @param jdOderUpdate
     * @return
     */
    Integer jdOderUpdate(Jdoder jdOderUpdate);
}
