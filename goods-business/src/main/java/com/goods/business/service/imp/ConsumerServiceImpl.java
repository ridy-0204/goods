package com.goods.business.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.converter.ConsumerConverter;
import com.goods.business.mapper.ConsumerMapper;
import com.goods.business.service.ConsumerService;
import com.goods.common.model.business.Consumer;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author songxin
 * @date 2022/5/30
 **/
@Service
public class ConsumerServiceImpl implements ConsumerService {
    @Autowired
    private ConsumerMapper consumerMapper;
    /**
     * 查询所有消费者
     * @author songxin
     * @date 2022/5/30
     * @return java.util.List<com.goods.common.vo.business.ConsumerVO>
     */
    @Override
    public List<ConsumerVO> findAll() {
        List<Consumer> consumers = consumerMapper.selectAll();
        List<ConsumerVO> consumerVOS = ConsumerConverter.converterToConsumerVOList(consumers);
        return consumerVOS;
    }
    /**
     * 添加消费者
     * @author songxin
     * @date 2022/5/30
     * @param consumerVO
     * @return com.goods.common.model.business.Consumer
     */
    @Override
    public Consumer add(ConsumerVO consumerVO) {
        Consumer consumer = ConsumerConverter.converterToConsumer(consumerVO);
        consumer.setCreateTime(new Date());
        consumer.setModifiedTime(new Date());
        consumerMapper.insert(consumer);
        return consumer;
    }
    /**
     * 查找所有物资去处
     * @author songxin
     * @date 2022/5/31
     * @param pageNum
     * @param pageSize
     * @param consumerVO
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.ConsumerVO>
     */
    @Override
    public PageVO<ConsumerVO> findConsumerList(Integer pageNum, Integer pageSize, ConsumerVO consumerVO) {
        PageHelper.startPage(pageNum,pageSize);
        Example example = new Example(Consumer.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("sort asc");
        if (consumerVO.getName() != null && !"".equals(consumerVO.getName())) {
            criteria.andLike("name", "%" + consumerVO.getName() + "%");
        }
        if (consumerVO.getContact() != null && !"".equals(consumerVO.getContact())) {
            criteria.andLike("contact", "%" + consumerVO.getContact() + "%");
        }
        if (consumerVO.getAddress() != null && !"".equals(consumerVO.getAddress())) {
            criteria.andLike("address", "%" + consumerVO.getAddress() + "%");
        }
        List<Consumer> consumers = consumerMapper.selectByExample(example);
        List<ConsumerVO> consumerVOS = ConsumerConverter.converterToConsumerVOList(consumers);
        PageInfo<Consumer> pageInfo = new PageInfo<>(consumers);
        return new PageVO<>(pageInfo.getTotal(),consumerVOS);
    }
    /**
     * 根据id查找消费者
     * @author songxin
     * @date 2022/5/31
     * @param id
     * @return com.goods.common.vo.business.SupplierVO
     */
    @Override
    public ConsumerVO edit(Long id) {
        Consumer consumer = consumerMapper.selectByPrimaryKey(id);
        ConsumerVO consumerVO = ConsumerConverter.converterToConsumerVO(consumer);
        return consumerVO;
    }
       /**
     * 修改消费者
     * @author songxin
     * @date 2022/5/31
     * @param consumerVO
     * @return void
     */
    @Override
    public void update(Long id, ConsumerVO consumerVO) {
        Consumer consumer = ConsumerConverter.converterToConsumer(consumerVO);
        consumer.setModifiedTime(new Date());
        consumerMapper.updateByPrimaryKeySelective(consumer);
    }

    /**
     * 删除消费者
     * @author songxin
     * @date 2022/5/31
     * @param id
     * @return void
     */
    @Override
    public void delete(Long id) {
        consumerMapper.deleteByPrimaryKey(id);
    }
}
