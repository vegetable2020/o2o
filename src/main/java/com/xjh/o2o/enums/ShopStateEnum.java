package com.xjh.o2o.enums;

public enum ShopStateEnum {
	CHECK(0, "审核中"), OFFLINE(-1, "非法商铺"), SUCCESS(1, "操作成功"), PASS(2, "通过认证"), INNER_ERROR(
			-1001, "操作失败"),NULL_SHOPID(-1002, "ShopId为空"),NULL_SHOP(-1003,"shop信息为空");
	private int state;//枚举类型的错误代码
	private String stateInfo;//枚举类型的注解
	
	private ShopStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	//返回相应的enum值
	public static ShopStateEnum stateOf(int index) {
		for (ShopStateEnum stateEnum : values()) {
			if (stateEnum.getState() == index) {
				return stateEnum;
			}
		}
		return null;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}
	
	
}
