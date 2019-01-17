package com.neo.dao;

import com.neo.model.Agent;
import com.neo.model.Userinfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/23.
 */
public interface AgentDao {
    @Select("SELECT * FROM agent WHERE userId = #{id} and status=0")
    List<Agent> queryForUserId(Integer id);
    @Select("SELECT agentId FROM agent WHERE userId = #{id} and status=0")
    String queryForAgentIdString(Integer id);
    @Select("SELECT * FROM agent WHERE userId = #{id} and status=0")
    Agent queryForUserIdSimple(Integer id);

    @Select("SELECT userId FROM agent WHERE agentId= #{id} and status=0 order by createTime limit #{star},#{end} ")
    List<Long> queryForUserIdLimt(@Param("id") Long id, @Param("star") Integer star, @Param("end") Integer end);

    @Select("SELECT userId,createTime FROM agent WHERE agentId= #{id} and status=0 order by createTime limit #{star},#{end} ")
    List<Agent> queryForUserIdAgentLimt(@Param("id") Long id, @Param("star") Integer star, @Param("end") Integer end);

    /**
     * 统计我的一级粉丝或者代理
     * @param id
     * @return
     */
    @Select("SELECT ifnull(count(userId),0) FROM agent WHERE agentId= #{id} and status=0")
    Integer queryForUserIdCount(@Param("id") Long id);

    /**
     * 统计我的一级粉丝或者代理
     * @param id
     * @return
     */
    @Select("SELECT ifnull(count(userId),0) FROM agent WHERE agentId= #{id} and status=0 and  to_days(createTime) = to_days(now())")
    Integer queryForUserIdCountToday(@Param("id") Long id);


    @Select("SELECT count(agentId) FROM agent WHERE agentId= #{id}")
    Integer countRecommd(@Param("id") Long id);

    @Select("SELECT userId,agentId,agentName,userName FROM agent WHERE agentId in (SELECT userId FROM agent WHERE agentId= #{id}) order by createTime limit #{star},#{end}")
    List<Agent> countRecommdToSum(@Param("id") Long id, @Param("star") Integer star, @Param("end") Integer end);


    @Select("SELECT userId,agentId FROM agent WHERE agentId in (SELECT userId FROM agent WHERE agentId= #{id}) order by createTime limit #{star},#{end}")
    List<Agent> countNoMyFans(@Param("id") Long id, @Param("star") Integer star, @Param("end") Integer end);

    /**
     * 统计我的非直属粉丝
     * @param id
     * @return
     */
    @Select("SELECT IFNULL(COUNT(userId),0) FROM agent WHERE agentId in (SELECT userId FROM agent WHERE agentId= #{id})  ")
    Integer countNoMyFansSum(@Param("id") Long id);
    /**
     * 统计我的非直属粉丝(按时间)
     * @param id
     * @return
     */
    @Select("SELECT IFNULL(COUNT(userId),0) FROM agent WHERE agentId in (SELECT userId FROM agent WHERE agentId= #{id}) and  to_days(createTime) = to_days(now())")
    Integer countNoMyFansSumToday(@Param("id") Long id);


    @Select("SELECT count(userId)FROM agent WHERE agentId in (SELECT userId FROM agent WHERE agentId= #{id}) order by createTime limit #{star},#{end}")
    Integer countRecommdToIntCount(@Param("id") Long id);

    @Select("SELECT * FROM agent WHERE agentId = #{id} and status=0")
    List<Agent> queryForAgentList(Integer id);

    @Select("SELECT * FROM agent WHERE userId = #{id} and status=0")
    List<Agent> queryForUserList(Integer id);

    @Select("SELECT userId FROM agent a,userinfo u WHERE a.userId = #{id} and status=0")
    List<Long> queryForAgentId(Integer id);

    @Select("Select u.Id,u.roleId,u.score from agent a left join userinfo u on a.userId=u.id and a.status=0 WHERE a.agentId=#{id}  and a.status=0")
    List<Userinfo> superQueryFansUserInfo(Integer id);

    @Select("SELECT userId FROM agent WHERE agentId = #{id} and status=0")
    List<Long> queryForAgentIdNew(Integer id);

    @Select("SELECT score FROM userinfo WHERE pddPid=#{id}")
    Integer queryUserScore(String id);

    @Select("SELECT score FROM userinfo WHERE tbPid=#{id}")
    Integer queryUserScoreTb(Long id);


    @Insert("INSERT INTO agent(agentId, userId,createTime) VALUES(#{agentId}, #{userId},now())")
    int insert(Agent agent);
    @Insert("update userinfo set roleId=2 ,score=#{score},updateTime=now() where id=#{uid}")
    Integer upAgent(@Param("score") Integer score, @Param("uid") Integer uid);
    @Insert("update agent set updateTime=now() where userId=#{uid}")
    Integer upAgentTime(@Param("uid") Integer uid);
    @Insert("INSERT INTO agent(agentId, userId,createTime) VALUES(#{agentId}, #{userId},now())")
    int insertAgLog(Agent agent);
}
