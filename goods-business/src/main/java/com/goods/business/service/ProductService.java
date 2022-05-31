package com.goods.business.service;

import com.goods.common.error.BusinessException;
import com.goods.common.vo.business.ProductStockVO;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;

import java.util.List;

/**
 * @author songxin
 * @date 2022/5/29
 **/
public interface ProductService {
    /**
     * 查找所有物资资料
     * @author songxin
     * @date 2022/5/29
     * @param pageNum
     * @param pageSize
     * @param productVO
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.ProductVO>
     */
    PageVO<ProductVO> findProductList(Integer pageNum, Integer pageSize, ProductVO productVO);
    /**
     * 添加物资资料
     * @author songxin
     * @date 2022/5/29
     * @param productVO
     */
    void add(ProductVO productVO);
    /**
     * 编辑物资资料
     * @author songxin
     * @date 2022/5/29
     * @param id
     * @return com.goods.common.vo.business.ProductVO
     */
    ProductVO edit(Long id);
    /**
     * 更新物资资料
     * @author songxin
     * @date 2022/5/29
     * @param id
     * @param productVO
     */
    void update(Long id, ProductVO productVO);
    /**
     * 移入回收站
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    void remove(Long id);
    /**
     * 从回收站恢复
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    void back(Long id);
    /**
     * 审核通过
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    void publish(Long id);
    /**
     * 逻辑删除
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    void delete(Long id) throws BusinessException;
    /**
     * 查找所有
     * @author songxin
     * @date 2022/5/30
     * @return java.util.List<com.goods.common.vo.business.ProductVO>
     * @param pageNum
     * @param pageSize
     */
    List<ProductStockVO> findAllStocks(Integer pageNum, Integer pageSize);

    /**
     * 加载库存信息
     * @author songxin
     * @date 2022/5/30
     * @param pageNum
     * @param pageSize
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.ProductStockVO>
     */
    PageVO<ProductStockVO> findProductStocks(Integer pageNum, Integer pageSize,ProductVO productVO);

}
