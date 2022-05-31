package com.goods.business.converter;

import com.goods.common.model.business.Product;
import com.goods.common.vo.business.ProductVO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author songxin
 * @date 2022/5/29
 **/
public class ProductConverter {
    /**
     * 转vo
     * @return
     */
    public static ProductVO converterToProductVO(Product product){
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product,productVO);
        return productVO;
    }
    /**
     * 转model
     * @return
     */
    public static Product converterToProduct(ProductVO productVO){
        Product product = new Product();
        BeanUtils.copyProperties(productVO,product);
        return product;
    }
    /**
     * 转为 ProductVOList
     * @author songxin
     * @date 2022/5/29
     * @param products
     * @return java.util.List<com.goods.common.vo.business.ProductVO>
     */
    public static List<ProductVO> converterToVOList(List<Product> products) {
        List<ProductVO> productVOList = products.stream().map(product -> {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(product, productVO);
            return productVO;
        }).collect(Collectors.toList());
        return productVOList;
    }
}
