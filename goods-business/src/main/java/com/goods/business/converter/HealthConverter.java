package com.goods.business.converter;

import com.goods.common.model.business.Consumer;
import com.goods.common.model.business.Health;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.business.HealthVO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author songxin
 * @date 2022/5/31
 **/
public class HealthConverter {
    /**
     * 转为 ConsumerVo
     * @author songxin
     * @date 2022/5/30
     * @param healthList
     */
    public static List<HealthVO> converterToHealthVO(List<Health> healthList) {
        List<HealthVO> healthVOS = healthList.stream().map(health -> {
            HealthVO healthVO = new HealthVO();
            BeanUtils.copyProperties(health, healthVO);
            return healthVO;
        }).collect(Collectors.toList());
        return healthVOS;
    }
    /**
     * 转为 health
     * @author songxin
     * @date 2022/5/30
     * @param healthVO
     */
    public static Health converterToHealth(HealthVO healthVO) {
        Health health = new Health();
        BeanUtils.copyProperties(healthVO, health);
        return health;
    }
}
