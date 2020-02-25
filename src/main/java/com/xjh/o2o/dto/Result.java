package com.xjh.o2o.dto;

public class Result<T> {
    private boolean success;//是否成功标志
    private T data;//成功时返回数据类型
    private String errorMsg;//错误信息
    private int errorCode;
    public Result(){

    }
    public Result(boolean success,T data){
        this.success=success;
        this.data=data;
    }
    public Result(boolean success,int errorCode,String errorMsg){
        this.success=success;
        this.errorCode=errorCode;
        this.errorMsg=errorMsg;
    }
    public boolean isSuccess(){
        return success;
    }
    public void setSuccess(boolean success){
        this.success=success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

