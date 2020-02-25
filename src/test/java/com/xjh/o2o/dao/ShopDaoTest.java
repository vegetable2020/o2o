package com.xjh.o2o.dao;

import com.xjh.o2o.BaseTest;
import com.xjh.o2o.entity.Area;
import com.xjh.o2o.entity.PersonInfo;
import com.xjh.o2o.entity.Shop;
import com.xjh.o2o.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopDaoTest extends BaseTest{
	@Autowired
	private ShopDao shopDao;
	
	@Test
	public void testQueryShopListAndCount() {
		Shop shopCondition = new Shop();
		ShopCategory childCategory=new ShopCategory();
		ShopCategory parentCategory=new ShopCategory();
		parentCategory.setShopCategoryId(4L);
		childCategory.setParent(parentCategory);
		shopCondition.setShopCategory(childCategory);
		shopCondition.setShopName("正式");
		List<Shop> shopList=shopDao.queryShopList(shopCondition,0,6);
		int count=shopDao.queryShopCount(shopCondition);
		System.out.println("店铺列表的大小："+shopList.size());
		System.out.println("店铺总数："+count);
	}
	
	@Test
	@Ignore
	public void testQueryByShopId() {
		long shopId=1;
		Shop shop=shopDao.queryByShopId(shopId);
		System.out.println("areaId:"+shop.getArea().getAreaId());
		System.out.println("areaName:"+shop.getArea().getAreaName());
	}
	@Test
	public void testInsertShop() {
		Shop shop=new Shop();
		Area area=new Area();
		PersonInfo owner=new PersonInfo();
		ShopCategory shopCategory=new ShopCategory();
		area.setAreaId(2);
		owner.setUserId(1L);
		shopCategory.setShopCategoryId(1L);
		shop.setArea(area);
		shop.setOwner(owner);
		shop.setShopCategory(shopCategory);
		shop.setShopName("再测试");
		shop.setShopAddr("test");
		shop.setShopDesc("test");
		shop.setPhone("test");
		shop.setShopImg("test");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(1);
		shop.setAdvice("test");
		int effectedNum=shopDao.insertShop(shop);
		assertEquals(1,effectedNum);
	}
	
	@Test
	public void testUpdateShop() {
		Shop shop=new Shop();
		shop.setShopId(19L);
		shop.setShopAddr("最终测试地址");
		shop.setShopDesc("最终测试描述");
		shop.setLastEditTime(new Date());
		int effectedNum=shopDao.updateShop(shop);
		assertEquals(1,effectedNum);
	}
}
