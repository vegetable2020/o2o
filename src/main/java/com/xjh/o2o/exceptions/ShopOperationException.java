package com.xjh.o2o.exceptions;

public class ShopOperationException extends RuntimeException{
	/**
	 * 封装店铺创建或修改的异常
	 */
	private static final long serialVersionUID = 1892926805712108378L;

	public ShopOperationException(String msg) {
		super(msg);
	}
}
