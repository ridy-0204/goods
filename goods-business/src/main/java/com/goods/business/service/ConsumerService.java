package com.goods.business.service;

import com.goods.common.model.business.Consumer;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;

import java.util.List;

/**
 * @author songxin
 * @date 2022/5/30
 **/
public interface ConsumerService {
    /**
     * 查询所有消费者
     * @author songxin
     * @date 2022/5/30
     * @return java.util.List<com.goods.common.vo.business.ConsumerVO>
     */
    List<ConsumerVO> findAll();
    /**
     * 添加消费者
     * @author songxin
     * @date 2022/5/30
     * @param consumerVO
     * @return com.goods.common.model.business.Consumer
     */
    Consumer add(ConsumerVO consumerVO);
    /**
     * 查找所有物资去处
     * @author songxin
     * @date 2022/5/31
     * @param pageNum
     * @param pageSize
     * @param consumerVO
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.ConsumerVO>
     */
    PageVO<ConsumerVO> findConsumerList(Integer pageNum, Integer pageSize, ConsumerVO consumerVO);
    /**
     * 根据id查找消费者
     * @author songxin
     * @date 2022/5/31
     * @param id
     * @return com.goods.common.vo.business.SupplierVO
     */
    ConsumerVO edit(Long id);
    /**
     * 修改消费者
     * @author songxin
     * @date 2022/5/31
     * @param id
     * @param consumerVO
     */
    void update(Long id, ConsumerVO consumerVO);
    /**
     * 删除消费者
     * @author songxin
     * @date 2022/5/31
     * @param id
     */
    void delete(Long id);
}
