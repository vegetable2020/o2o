package com.xjh.o2o.dao;

import com.xjh.o2o.BaseTest;
import com.xjh.o2o.entity.ProductCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductCategoryDaoTest extends BaseTest{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test
	public void testQueryByShopId() {
		long shopId=1L;
		List<ProductCategory> productCategoryList=productCategoryDao.queryProductCategoryList(shopId);
		System.out.println("该店铺自定义类别数为："+productCategoryList.size());
	}

	@Test
	public void testBatchInsertProductCategory() {
		ProductCategory p1=new ProductCategory();
		p1.setProductCategoryName("商品分类4");
		p1.setCreateTime(new Date());
		p1.setPriority(1);
		p1.setShopId(1L);
		ProductCategory p2=new ProductCategory();
		p2.setProductCategoryName("商品分类5");
		p2.setCreateTime(new Date());
		p2.setPriority(2);
		p2.setShopId(1L);
		List<ProductCategory> productCategoryList=new ArrayList<ProductCategory>();
		productCategoryList.add(p1);
		productCategoryList.add(p2);
		int num=productCategoryDao.batchInsertProductCategory(productCategoryList);
		assertEquals(2,num);
	}

	@Test
	public void testDeleteProductCategory() {
		int num=productCategoryDao.deleteProductCategory(8,1);
		assertEquals(1,num);
	}
}
