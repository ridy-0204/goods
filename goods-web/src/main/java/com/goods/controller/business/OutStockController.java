package com.goods.controller.business;

import com.goods.business.service.ConsumerService;
import com.goods.business.service.OutStockService;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.Consumer;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.*;
import com.goods.common.vo.system.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author songxin
 * @date 2022/5/30
 **/
@Api(tags = "业务模块-入库相关接口")
@RestController
@RequestMapping("/business/outStock")
public class OutStockController {
    @Autowired
    private OutStockService outStockService;
    @Autowired
    private ConsumerService consumerService;

    /**
     * 部门列表
     *
     * @return
     */
    @ApiOperation(value = "出库查询", notes = "发放记录,")
    @GetMapping("/findOutStockList")
    public ResponseBean<PageVO> findOutStockList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                 @RequestParam(value = "pageSize") Integer pageSize,
                                                 OutStockVO outStockVO) {

        PageVO<OutStockVO> pageVO = outStockService.findOutStockList(pageNum, pageSize, outStockVO);

        return ResponseBean.success(pageVO);
    }

    @ApiOperation(value = "添加出库信息")
    @PostMapping("/addOutStock")
    public ResponseBean addOutStock(@RequestBody OutStockVO outStockVO) throws BusinessException {
        if (outStockVO.getConsumerId() == null) {
            ConsumerVO consumerVO = new ConsumerVO();
            BeanUtils.copyProperties(outStockVO, consumerVO);
            Consumer consumer1 = consumerService.add(consumerVO);
            outStockVO.setConsumerId(consumer1.getId());
        }
        outStockService.addOutStock(outStockVO);
        return ResponseBean.success();
    }

    @ApiOperation(value = "移入回收站")
    @PutMapping("/remove/{id}")
    public ResponseBean remove(@PathVariable Long id) throws BusinessException {
        outStockService.remove(id);
        return ResponseBean.success();
    }

    @ApiOperation(value = "从回收站恢复")
    @PutMapping("/back/{id}")
    public ResponseBean back(@PathVariable Long id) throws BusinessException {
        outStockService.back(id);
        return ResponseBean.success();
    }

    @ApiOperation(value = "审核通过")
    @PutMapping("/publish/{id}")
    public ResponseBean publish(@PathVariable Long id) throws BusinessException {
        outStockService.publish(id);
        return ResponseBean.success();
    }

    @ApiOperation(value = "逻辑删除")
    @GetMapping("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id) throws BusinessException {
        outStockService.delete(id);
        return ResponseBean.success();
    }

    @ApiOperation(value = "查询入库明细")
    @GetMapping("/detail/{id}")
    public ResponseBean detail(@PathVariable Long id,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        OutStockDetailVO outStockDetailVO = outStockService.detail(id, pageNum);
        return ResponseBean.success(outStockDetailVO);
    }
}
