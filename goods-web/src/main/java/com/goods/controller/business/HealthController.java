package com.goods.controller.business;

import com.goods.business.service.HealthService;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.HealthVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author songxin
 * @date 2022/5/31
 **/
@RestController
@RequestMapping("/business/health")
public class HealthController {
    @Autowired
    private HealthService healthService;

    @RequestMapping("/isReport")
    public ResponseBean<HealthVO> isReport() {
        HealthVO healthVO = healthService.isReport();
        return ResponseBean.success(healthVO);
    }

    @RequestMapping("/report")
    public ResponseBean report(@RequestBody HealthVO healthVO) {
        int report = healthService.report(healthVO);
        if(report == 1) {
            return ResponseBean.success();
        }
        return ResponseBean.error("打卡失败");
    }
    @RequestMapping("/history")
    public ResponseBean<PageVO<HealthVO>> history(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize") Integer pageSize) {
        PageVO<HealthVO>pageVO = healthService.history(pageNum,pageSize);
        return ResponseBean.success(pageVO);
    }
}
