package com.goods.business.service;

import com.goods.common.error.BusinessException;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.ProductCategoryVO;
import com.goods.common.vo.system.PageVO;

import java.util.List;

/**
 * @author songxin
 * @date 2022/5/28
 **/
public interface ProductCategoryService {
    /**
     * 物资分类树状查询
     * @author songxin
     * @date 2022/5/28
     * @param pageNum
     * @param pageSize
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.ProductCategoryVO>
     */
    PageVO<ProductCategoryTreeNodeVO> categoryTree(Integer pageNum, Integer pageSize);

    /**
     * 查询父类列表
     * @author songxin
     * @date 2022/5/29
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.ProductCategoryTreeNodeVO>
     */
    List<ProductCategoryTreeNodeVO> getParentCategoryTree();
    /**
     * 添加分类
     * @author songxin
     * @date 2022/5/29
     * @param productCategoryVO
     */
    void add(ProductCategoryVO productCategoryVO);
    /**
     * 回显分类数据
     * @author songxin
     * @date 2022/5/29
     * @param id
     * @return com.goods.common.vo.business.ProductCategoryVO
     */
    ProductCategoryVO selectOne(Long id);
    /**
     * 更新分类id
     * @author songxin
     * @date 2022/5/29
     * @param productCategory
     */
    void update(ProductCategory productCategory);
    /**
     * 删除分类
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    void delete(Long id) throws BusinessException;

}
