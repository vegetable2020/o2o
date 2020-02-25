package com.xjh.o2o.entity;

import java.util.Date;

public class WechatAuth {//微信账号
	private Long wechatAuthId;//微信ID
	private String openId;//与公众号绑定唯一表识
	private Date createTime;//创建时间
	private PersonInfo persionInfo;//对应用户
	
	public Long getWechatAuthId() {
		return wechatAuthId;
	}
	public void setWechatAuthId(Long wechatAuthId) {
		this.wechatAuthId = wechatAuthId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public PersonInfo getPersionInfo() {
		return persionInfo;
	}
	public void setPersionInfo(PersonInfo persionInfo) {
		this.persionInfo = persionInfo;
	}
}
