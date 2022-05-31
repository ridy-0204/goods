package com.goods.controller.business;

import com.goods.business.service.ConsumerService;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author songxin
 * @date 2022/5/30
 **/
@Api(tags = "业务模块-物资来源相关接口")
@RestController
@RequestMapping("/business/consumer")
public class ConsumerController {
    @Autowired
    private ConsumerService consumerService;
    @ApiOperation(value = "分页查找所有物资去处")
    @GetMapping("/findConsumerList")
    public ResponseBean<PageVO<ConsumerVO>> findConsumerList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                             @RequestParam(value = "pageSize") Integer pageSize,
                                                             ConsumerVO consumerVO){
        PageVO<ConsumerVO>consumerVOPageVO = consumerService.findConsumerList(pageNum,pageSize,consumerVO);
        return ResponseBean.success(consumerVOPageVO);
    }
    @ApiOperation(value = "查找所有物资去处")
    @GetMapping("/findAll")
    public ResponseBean<List<ConsumerVO>> findAll(){
        List<ConsumerVO> consumerVOS = consumerService.findAll();
        return ResponseBean.success(consumerVOS);
    }
    @ApiOperation(value = "添加物资去处")
    @PostMapping("/add")
    public ResponseBean add(@RequestBody ConsumerVO consumerVO){
        consumerService.add(consumerVO);
        return ResponseBean.success();
    }

    @ApiOperation(value = "编辑物资去处")
    @GetMapping("/edit/{id}")
    public ResponseBean edit(@PathVariable Long id)  {
        ConsumerVO consumerVO = consumerService.edit(id);
        return ResponseBean.success(consumerVO);
    }
    @ApiOperation(value = "更新物资去处")
    @PutMapping ("/update/{id}")
    public ResponseBean update(@PathVariable Long id,
                               @RequestBody ConsumerVO consumerVO)  {
        consumerService.update(id,consumerVO);
        return ResponseBean.success();
    }
    @ApiOperation(value = "删除物资去处")
    @DeleteMapping ("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id)  {
        consumerService.delete(id);
        return ResponseBean.success();
    }
}
