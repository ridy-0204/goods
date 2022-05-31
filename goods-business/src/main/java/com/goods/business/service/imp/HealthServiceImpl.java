package com.goods.business.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.converter.HealthConverter;
import com.goods.business.mapper.HealthMapper;
import com.goods.business.service.HealthService;
import com.goods.common.model.business.Health;
import com.goods.common.response.ActiveUser;
import com.goods.common.vo.business.HealthVO;
import com.goods.common.vo.system.PageVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author songxin
 * @date 2022/5/31
 **/
@Service
public class HealthServiceImpl implements HealthService {
    @Autowired
    private HealthMapper healthMapper;

    /**
     * 查询是否健康打卡
     *
     * @return com.goods.common.vo.business.HealthVO
     * @author songxin
     * @date 2022/5/31
     */
    @Override
    public HealthVO isReport() {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        List<Health> health = healthMapper.isReport(activeUser.getUser().getId());
        if (health.size() > 0) {
            List<HealthVO> healthVOS = HealthConverter.converterToHealthVO(health);
            return healthVOS.get(0);
        }
        return null;
    }
    /**
     * 打卡
     * @author songxin
     * @date 2022/5/31
     * @param healthVO
     * @return int
     */
    @Override
    public int report(HealthVO healthVO) {
        Health health = HealthConverter.converterToHealth(healthVO);
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        health.setUserId(Long.valueOf(activeUser.getUser().getId()));
        health.setCreateTime(new Date());
        int insert = healthMapper.insert(health);
        return insert;
    }
    /**
     * 分页查询打卡记录
     * @author songxin
     * @date 2022/5/31
     * @param pageNum
     * @param pageSize
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.HealthVO>
     */
    @Override
    public PageVO<HealthVO> history(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Health> health = healthMapper.selectAll();
        List<HealthVO> healthVOS = HealthConverter.converterToHealthVO(health);
        PageInfo<Health> pageInfo = new PageInfo<>(health);
        return new PageVO<>(pageInfo.getTotal(),healthVOS);
    }
}
