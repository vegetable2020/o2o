package com.xjh.o2o.dao;


import com.xjh.o2o.BaseTest;
import com.xjh.o2o.entity.PersonInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class PersonInfoDaoTest extends BaseTest {
    @Autowired
    private PersonInfoDao personInfoDao;

    @Test
    public void testQueryPersonInfoByUserId(){
        PersonInfo personInfo;
        personInfo=personInfoDao.queryPersonInfoByUserId(2);
        System.out.println("name:"+personInfo.getName()+"\nemail:"+personInfo.getEmail()+"\npassword:"+personInfo.getPassword());
    }

    @Test
    public void testInsertPersonInfo(){
        PersonInfo personInfo=new PersonInfo();
        personInfo.setName("test1");
        personInfo.setEmail("1999");
        personInfo.setPassword("1999");
        personInfo.setEnableStatus(0);
        personInfo.setUserType(1);
        int effectNum=personInfoDao.insertPersonInfo(personInfo);
        assertEquals(1, effectNum);
    }

    @Test
    public void testUpdatePersonInfo(){
        PersonInfo personInfo=new PersonInfo();
        personInfo.setUserId(3L);
        personInfo.setName("test2");
        personInfo.setEmail("2000");
        personInfo.setPassword("2000");
        personInfo.setEnableStatus(1);
        int effectNum=personInfoDao.updatePersonInfo(personInfo);
        assertEquals(1, effectNum);
    }

    @Test
    public void testQueryPersonInfo(){
        PersonInfo personInfoCondition=new PersonInfo();
        personInfoCondition.setEmail("2000");
        personInfoCondition.setPassword("2000");
        PersonInfo personInfo=personInfoDao.queryPersonInfo(personInfoCondition);
        System.out.println("查询的用户名："+personInfo.getName());
    }
}
