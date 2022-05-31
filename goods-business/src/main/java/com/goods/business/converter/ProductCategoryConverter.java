package com.goods.business.converter;

import com.goods.common.model.business.ProductCategory;
import com.goods.common.vo.business.ProductCategoryVO;
import org.springframework.beans.BeanUtils;

/**
 * @author songxin
 * @date 2022/5/29
 **/
public class ProductCategoryConverter {
    /**
     * 转vo
     * @return
     */
    public static ProductCategoryVO converterToDepartmentVO(ProductCategory productCategory){
        ProductCategoryVO productCategoryVO = new ProductCategoryVO();
        BeanUtils.copyProperties(productCategory,productCategoryVO);
        return productCategoryVO;
    }
}
