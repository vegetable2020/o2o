package com.xjh.o2o.service.impl;

import com.xjh.o2o.dao.PersonInfoDao;
import com.xjh.o2o.dto.PersonInfoExecution;
import com.xjh.o2o.entity.PersonInfo;
import com.xjh.o2o.enums.PersonInfoStateEnum;
import com.xjh.o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {
    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public PersonInfo getPersonInfoByUserId(long userId) {
        return personInfoDao.queryPersonInfoByUserId(userId);
    }

    @Override
    @Transactional
    public PersonInfoExecution addPersonInfo(PersonInfo personInfo) {
        if (personInfo != null && personInfo.getEmail() != null && personInfo.getPassword() != null) {
            PersonInfo personInfoCondition = new PersonInfo();
            personInfoCondition.setEmail(personInfo.getEmail());
            if (personInfoDao.queryPersonInfo(personInfoCondition) != null) {
                return new PersonInfoExecution(PersonInfoStateEnum.EXIST);
            } else {
                personInfo.setCreateTime(new Date());
                personInfo.setLastEditTime(new Date());
                personInfo.setEnableStatus(1);
                personInfo.setUserType(2);
                try {
                    int effectedNum = personInfoDao.insertPersonInfo(personInfo);
                    if (effectedNum < 0) {
                        throw new RuntimeException("用户创建失败");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("用户创建失败" + e.toString());
                }
                return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS, personInfo);
            }
        } else {
            return new PersonInfoExecution(PersonInfoStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public PersonInfoExecution modifyPersonInfo(PersonInfo personInfo) {
        if (personInfo != null && personInfo.getEmail() != null && personInfo.getPassword() != null) {
            PersonInfo personInfoCondition = new PersonInfo();
            personInfoCondition.setEmail(personInfo.getEmail());
            personInfoCondition = personInfoDao.queryPersonInfo(personInfoCondition);
            if (personInfoCondition != null && personInfoCondition.getUserId() != personInfo.getUserId()) {
                return new PersonInfoExecution(PersonInfoStateEnum.EXIST);
            } else {
                personInfo.setLastEditTime(new Date());
                try {
                    int effectedNum = personInfoDao.updatePersonInfo(personInfo);
                    if (effectedNum < 0) {
                        throw new RuntimeException("用户信息修改失败");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("用户信息修改失败" + e.toString());
                }
                return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS, personInfo);
            }
        } else {
            return new PersonInfoExecution(PersonInfoStateEnum.EMPTY);
        }
    }

    @Override
    public PersonInfoExecution getPersonInfo(PersonInfo personInfoCondition) {
        PersonInfo personInfo = personInfoDao.queryPersonInfo(personInfoCondition);
        PersonInfoExecution personInfoExecution = new PersonInfoExecution();
        if (personInfo != null) {
            personInfoExecution.setPersonInfo(personInfo);
        } else {
            personInfoExecution.setState(PersonInfoStateEnum.INNER_ERROR.getState());
        }
        return personInfoExecution;
    }
}
