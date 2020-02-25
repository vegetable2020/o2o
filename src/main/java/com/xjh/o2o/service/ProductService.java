package com.xjh.o2o.service;

import com.xjh.o2o.dto.ImageHolder;
import com.xjh.o2o.dto.ProductExecution;
import com.xjh.o2o.entity.Product;
import com.xjh.o2o.exceptions.ProductOperationException;

import java.util.List;


public interface ProductService {
    /**
     * 查询商品列表并分页，可输入的条件有：商品名（模糊），商品状态，商铺Id，商品类别
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

    Product getProductById(long productId);

    /**
     * 添加商品
     *
     * @param product
     * @param thumbnail
     * @param productImgList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
            throws ProductOperationException;

    /**
     * 修改商品信息
     *
     * @param product
     * @param thumbnail
     * @param productImgList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException;

}
