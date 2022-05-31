package com.goods.business.mapper;

import com.goods.common.model.business.Health;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author songxin
 * @date 2022/5/31
 **/
public interface HealthMapper extends Mapper<Health> {
    /**
     * 查询今日是否打卡
     * @author songxin
     * @date 2022/5/31
     * @param userId
     * @return java.util.List<com.goods.common.model.business.Health>
     *  @Select("select * from biz_health where create_time < (CURDATE()+1) " +" and create_time>CURDATE() and user_id=#{id}")
     *   SELECT * FROM t_student WHERE time BETWEEN CONCAT( CURDATE(), ' 00:00:00' ) AND CONCAT( CURDATE(), ' 23:59:59')
     */
    @Select("select * from biz_health where user_id = #{userId} and DATE_FORMAT(create_time,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')")
    //@Select("select * from biz_health where user_id = #{userId} and DATE_FORMAT(create_time,'%Y-%m-%d') = CURDATE()")
    List<Health> isReport(Long userId);
}
