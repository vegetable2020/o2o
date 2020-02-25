package com.xjh.o2o.service;

import com.xjh.o2o.entity.HeadLine;

import java.io.IOException;
import java.util.List;

public interface HeadLineService {

	/**
	 * 
	 * @param headLineCondition
	 * @return
	 * @throws IOException
	 */
	List<HeadLine> getHeadLineList(HeadLine headLineCondition)
			throws IOException;

}
