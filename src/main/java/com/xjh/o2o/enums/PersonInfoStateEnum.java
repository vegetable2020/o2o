package com.xjh.o2o.enums;

public enum PersonInfoStateEnum {
    OFFLINE(-1, "非法用户"), SUCCESS(0, "操作成功"), PASS(2, "通过认证"), INNER_ERROR(
            -1001, "操作失败"),EMPTY(-1002, "用户信息为空"),EXIST(-1003,"电子邮箱已被使用");

    private int state;

    private String stateInfo;

    PersonInfoStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }
    //返回相应的enum值
    public static PersonInfoStateEnum stateOf(int index) {
        for (PersonInfoStateEnum stateEnum : values()) {
            if (stateEnum.getState() == index) {
                return stateEnum;
            }
        }
        return null;
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
}
