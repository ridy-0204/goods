package com.goods.business.converter;

import com.goods.common.model.business.Consumer;
import com.goods.common.vo.business.ConsumerVO;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author songxin
 * @date 2022/5/30
 **/
public class ConsumerConverter {
    /**
     * 转为 ConsumerVOList
     *
     * @param consumers
     * @return java.util.List<com.goods.common.vo.business.ConsumerVO>
     * @author songxin
     * @date 2022/5/30
     */
    public static List<ConsumerVO> converterToConsumerVOList(List<Consumer> consumers) {
        List<ConsumerVO> consumerVOS = consumers.stream().map(consumer -> {
            ConsumerVO consumerVO = new ConsumerVO();
            BeanUtils.copyProperties(consumer, consumerVO);
            return consumerVO;
        }).collect(Collectors.toList());
        return consumerVOS;
    }
    /**
     * 转为 Consumer
     * @author songxin
     * @date 2022/5/30
     * @param consumerVO
     * @return com.goods.common.model.business.Consumer
     */
    public static Consumer converterToConsumer(ConsumerVO consumerVO) {
        Consumer consumer = new Consumer();
        BeanUtils.copyProperties(consumerVO, consumer);
        return consumer;
    }
    /**
     * 转为 ConsumerVo
     * @author songxin
     * @date 2022/5/30
     * @param consumer
     * @return com.goods.common.model.business.Consumer
     */
    public static ConsumerVO converterToConsumerVO(Consumer consumer) {
        ConsumerVO consumerVO = new ConsumerVO();
        BeanUtils.copyProperties(consumer, consumerVO);
        return consumerVO;
    }
}
