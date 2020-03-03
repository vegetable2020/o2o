package com.xjh.o2o.dto;

import com.xjh.o2o.entity.PersonInfo;
import com.xjh.o2o.enums.PersonInfoStateEnum;

public class PersonInfoExecution {
    // 结果状态
    private int state;

    // 状态标识
    private String stateInfo;

    // 操作的personInfo（增改用户信息的时候用）
    private PersonInfo personInfo;


    // 失败的构造器
    public PersonInfoExecution(PersonInfoStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    // 成功的构造器
    public PersonInfoExecution(PersonInfoStateEnum stateEnum, PersonInfo personInfo) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.personInfo=personInfo;

    }

    public PersonInfoExecution() {
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}