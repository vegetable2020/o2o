package com.xjh.o2o.service;

import com.xjh.o2o.BaseTest;
import com.xjh.o2o.dto.ImageHolder;
import com.xjh.o2o.dto.ShopExecution;
import com.xjh.o2o.entity.Area;
import com.xjh.o2o.entity.PersonInfo;
import com.xjh.o2o.entity.Shop;
import com.xjh.o2o.entity.ShopCategory;
import com.xjh.o2o.enums.ShopStateEnum;
import com.xjh.o2o.exceptions.ShopOperationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest {
    @Autowired
    private ShopService shopService;

    @Test
    public void testQueryShopListAndCount() {
        Shop shopCondition = new Shop();
        ShopExecution se = shopService.getShopList(shopCondition, 1, 2);
        System.out.println("店铺列表数为：" + se.getShopList().size());
        System.out.println("店铺总数为：" + se.getCount());
    }

    @Test
    public void testModifyShop() throws ShopOperationException, FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(19L);
        shop.setShopName("再修改后的店铺");
        File shopImg = new File("D://BiZhi/辉夜.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder(shopImg.getName(), is);
        ShopExecution se = shopService.modifyShop(shop, imageHolder);
        System.out.println("新的图片地址：" + se.getShop().getShopImg() + "\n状态：" + se.getStateInfo());
    }

    @Test
    public void testAddShop() throws FileNotFoundException {
        Shop shop = new Shop();
        Area area = new Area();
        PersonInfo owner = new PersonInfo();
        ShopCategory shopCategory = new ShopCategory();
        area.setAreaId(2);
        owner.setUserId(1L);
        shopCategory.setShopCategoryId(1L);
        shop.setArea(area);
        shop.setOwner(owner);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试店铺11");
        shop.setShopAddr("test10");
        shop.setShopDesc("test10");
        shop.setPhone("test10");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");
        File shopImg = new File("D://Users/XJH/image/pika.jpg");
        InputStream i = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder(shopImg.getName(), i);
        ShopExecution se = shopService.addShop(shop, imageHolder);
        assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
    }
}
