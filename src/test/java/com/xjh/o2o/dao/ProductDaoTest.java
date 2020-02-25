package com.xjh.o2o.dao;


import com.xjh.o2o.BaseTest;
import com.xjh.o2o.entity.Product;
import com.xjh.o2o.entity.ProductCategory;
import com.xjh.o2o.entity.ProductImg;
import com.xjh.o2o.entity.Shop;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductDaoTest extends BaseTest {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void TestInsertProduct() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        Shop shop = new Shop();
        shop.setShopId(1L);
        Product product1 = new Product();
        product1.setProductName("测试商品3");
        product1.setProductDesc("描述1");
        product1.setImgAddr("Test1");
        product1.setNormalPrice("8元");
        product1.setPromotionPrice("6元");
        product1.setPriority(5);
        product1.setEnableStatus(1);
        product1.setCreateTime(new Date());
        product1.setLastEditTime(new Date());
        product1.setShop(shop);
        product1.setProductCategory(productCategory);

        Product product2 = new Product();
        product2.setProductName("测试商品4");
        product2.setProductDesc("描述2");
        product2.setImgAddr("Test2");
        product2.setNormalPrice("10元");
        product2.setPriority(10);
        product2.setEnableStatus(0);
        product2.setCreateTime(new Date());
        product2.setLastEditTime(new Date());
        product2.setShop(shop);
        product2.setProductCategory(productCategory);

        int effectNum = productDao.insertProduct(product1);
        assertEquals(1, effectNum);
        effectNum = productDao.insertProduct(product2);
        assertEquals(1, effectNum);
    }

    @Test
    public void testBQueryProductList() throws Exception {
        Product product = new Product();
        List<Product> productList = productDao.queryProductList(product, 0, 3);
        assertEquals(3, productList.size());
        int count = productDao.queryProductCount(product);
        assertEquals(5, count);
        product.setProductName("测试");
        productList = productDao.queryProductList(product, 0, 3);
        assertEquals(3, productList.size());
        count = productDao.queryProductCount(product);
        assertEquals(3, count);
        Shop shop = new Shop();
        shop.setShopId(2L);
        product.setShop(shop);
        productList = productDao.queryProductList(product, 0, 3);
        assertEquals(1, productList.size());
        count = productDao.queryProductCount(product);
        assertEquals(1, count);
    }

    @Test
    public void testCQueryProductByProductId() throws Exception {
        long productId = 1;
        ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddr("图片1");
        productImg1.setImgDesc("测试图片1");
        productImg1.setPriority(1);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(productId);
        ProductImg productImg2 = new ProductImg();
        productImg2.setImgAddr("图片2");
        productImg2.setPriority(1);
        productImg2.setCreateTime(new Date());
        productImg2.setProductId(productId);
        List<ProductImg> productImgList = new ArrayList<ProductImg>();
        productImgList.add(productImg1);
        productImgList.add(productImg2);
        int effectedNum = productImgDao.batchInsertProductImg(productImgList);
        assertEquals(2, effectedNum);
        Product product = productDao.queryProductByProductId(productId);
        assertEquals(2, product.getProductImgList().size());
        effectedNum = productImgDao.deleteProductImgByProductId(productId);
        assertEquals(2, effectedNum);
    }

    @Test
    public void testDUpdateProduct() throws Exception {
        Product product = new Product();
        ProductCategory productCategory = new ProductCategory();
        Shop shop = new Shop();
        shop.setShopId(1L);
        productCategory.setProductCategoryId(2L);
        product.setProductId(1L);
        product.setShop(shop);
        product.setProductName("第一个产品");
        product.setProductCategory(productCategory);
        int effectedNum = productDao.updateProduct(product);
        assertEquals(1, effectedNum);
    }
    @Test
    public void testUpdateProductCategoryToNull(){
        int effectedNull=productDao.updateProductCategoryToNull(1L);
        assertEquals(3,effectedNull);
    }
}
