package com.goods.business.service;

import com.goods.common.error.BusinessException;
import com.goods.common.vo.business.InStockDetailVO;
import com.goods.common.vo.business.InStockVO;
import com.goods.common.vo.system.PageVO;

/**
 * @author songxin
 * @date 2022/5/28
 **/
public interface InStockService {
    /**
     * 查找所有入库记录
     * @author songxin
     * @date 2022/5/29
     * @param pageNum
     * @param pageSize
     * @param inStockVO
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.InStockVO>
     */
    PageVO<InStockVO> findInStockList(Integer pageNum, Integer pageSize,InStockVO inStockVO);
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
    void publish(Long id) throws BusinessException;
    /**
     * 逻辑删除
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    void delete(Long id) throws BusinessException;
    /**
     * 添加入库记录
     * @author songxin
     * @date 2022/5/29
     * @param inStockVO
     */
    void addIntoStock(InStockVO inStockVO)throws BusinessException;
    /**
     * 查找入库记录详情
     * @author songxin
     * @date 2022/5/30
     * @param id
     * @param pageNum
     * @return com.goods.common.vo.business.InStockDetailVO
     */
    InStockDetailVO detail(Long id, Integer pageNum);
}
