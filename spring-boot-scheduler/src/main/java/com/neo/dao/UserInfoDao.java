package com.neo.dao;

import com.neo.model.Userinfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoDao {

    Userinfo queryPidUser(Userinfo userinfo);
}
