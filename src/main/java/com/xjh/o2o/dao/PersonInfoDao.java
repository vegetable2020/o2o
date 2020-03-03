package com.xjh.o2o.dao;

import com.xjh.o2o.entity.PersonInfo;
import org.apache.ibatis.annotations.Param;

public interface PersonInfoDao {
    /**
     * 根据Id查询用户
     * @param userId
     * @return
     */
    PersonInfo queryPersonInfoByUserId(long userId);

    /**
     * 注册用户querById
     * @param personInfo
     * @return
     */
    int insertPersonInfo(PersonInfo personInfo);

    /**
     * 修改用户信息
     * @param personInfo
     * @return
     */
    int updatePersonInfo(PersonInfo personInfo);

    /**
     * 查询用户（用于登录、注册）
     * @param personInfoCondition
     * @return
     */
    PersonInfo queryPersonInfo(@Param("personInfoCondition")PersonInfo personInfoCondition);
}
