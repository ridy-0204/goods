package com.goods.business.service;

import com.goods.common.vo.business.HealthVO;
import com.goods.common.vo.system.PageVO;

/**
 * @author songxin
 * @date 2022/5/31
 **/
public interface HealthService {
    /**
     * 查询健康是否打卡
     * @author songxin
     * @date 2022/5/31
     * @return com.goods.common.vo.business.HealthVO
     */
    HealthVO isReport();
    /**
     * 打卡
     * @author songxin
     * @date 2022/5/31
     * @param healthVO
     * @return int
     */
    int report(HealthVO healthVO);
    /**
     * 分页查询打卡记录
     * @author songxin
     * @date 2022/5/31
     * @param pageNum
     * @param pageSize
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.HealthVO>
     */
    PageVO<HealthVO> history(Integer pageNum, Integer pageSize);
}
