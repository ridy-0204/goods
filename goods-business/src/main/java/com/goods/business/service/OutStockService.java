package com.goods.business.service;

import com.goods.common.error.BusinessException;
import com.goods.common.vo.business.OutStockDetailVO;
import com.goods.common.vo.business.OutStockVO;
import com.goods.common.vo.system.PageVO;

/**
 * @author songxin
 * @date 2022/5/30
 **/
public interface OutStockService {
    /**
     * 查询所有出库
     * @author songxin
     * @date 2022/5/30
     * @param pageNum
     * @param pageSize
     * @param outStockVO
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.OutStockVO>
     */
    PageVO<OutStockVO> findOutStockList(Integer pageNum, Integer pageSize, OutStockVO outStockVO);
    /**
     * 新增出库
     * @author songxin
     * @date 2022/5/30
     * @param outStockVO
     */
    void addOutStock(OutStockVO outStockVO) throws BusinessException;
    /**
     * 移入回收站
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    void remove(Long id) throws BusinessException;
    /**
     * 从回收站恢复
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    void back(Long id) throws BusinessException;
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
     * 出库明细
     * @author songxin
     * @date 2022/5/31
     * @param id
     * @param pageNum
     * @return com.goods.common.vo.business.OutStockDetailVO
     */
    OutStockDetailVO detail(Long id, Integer pageNum);
}
