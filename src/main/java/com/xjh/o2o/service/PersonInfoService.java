package com.xjh.o2o.service;

import com.xjh.o2o.dto.PersonInfoExecution;
import com.xjh.o2o.entity.PersonInfo;

public interface PersonInfoService {
    PersonInfo getPersonInfoByUserId(long userId);

    PersonInfoExecution addPersonInfo(PersonInfo personInfo);

    PersonInfoExecution modifyPersonInfo(PersonInfo personInfo);

    PersonInfoExecution getPersonInfo(PersonInfo personInfoCondition);
}
