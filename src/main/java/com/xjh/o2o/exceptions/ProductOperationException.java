package com.xjh.o2o.exceptions;

public class ProductOperationException extends RuntimeException{
	/**
	 * 封装店铺创建或修改的异常
	 */

	public ProductOperationException(String msg) {
		super(msg);
	}
}
