package com.neo.dao;

import com.neo.model.PddOderBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/16.
 */
@Mapper
public interface PddOderDao {
    Integer addOder(@Param("oder") PddOderBean pddOderBean);

    @Insert("insert into scanlog(lastTime,createTime,devName) values(#{date},now(),#{dev})")
    void scanLog(@Param("date") String date, @Param("dev") String dev);


    @Select("select id from oder where order_sn=#{sn} limit 1")
    PddOderBean findIs(String sn);

    /**
     * 扫描拼多多已结算订单
     * @return
     */
    @Select("select * from oder where order_status=5 and settle=0")
    List<PddOderBean> scanPddOder();

    /**
     * 更新拼多多的订单
     *
     * @param pddOderBean
     * @return
     */
    Integer pddOderUpdate(PddOderBean pddOderBean);
}
