package com.neo.dao;

import com.neo.jsonbean.PddOderBean;
import com.neo.model.Userinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoDao {

    Userinfo queryPidUser(Userinfo userinfo);
}
