package com.goods.controller.business;

import com.goods.business.service.SupplierService;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.SupplierVO;

import com.goods.common.vo.system.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author songxin
 * @date 2022/5/29
 **/
@Api(tags = "业务模块-物资来源相关接口")
@RestController
@RequestMapping("/business/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;
    @ApiOperation(value = "查找所有物资来源")
    @GetMapping("/findSupplierList")
    public ResponseBean<PageVO<SupplierVO>> findSupplierList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                            @RequestParam(value = "pageSize") Integer pageSize,
                                                                            SupplierVO supplierVO){
        PageVO<SupplierVO>supplierVOPageVO = supplierService.findSupplierList(pageNum,pageSize,supplierVO);
        return ResponseBean.success(supplierVOPageVO);
    }
    @ApiOperation(value = "添加物资来源")
    @PostMapping ("/add")
    public ResponseBean<PageVO<SupplierVO>> add(@RequestBody SupplierVO supplierVO){
        supplierService.add(supplierVO);
        return ResponseBean.success();
    }

    @ApiOperation(value = "编辑物资来源")
    @GetMapping("/edit/{id}")
    public ResponseBean edit(@PathVariable Long id)  {
        SupplierVO supplierVO = supplierService.edit(id);
        return ResponseBean.success(supplierVO);
    }
    @ApiOperation(value = "更新物资来源")
    @PutMapping ("/update/{id}")
    public ResponseBean update(@PathVariable Long id,
                               @RequestBody SupplierVO supplierVO)  {
        supplierService.update(id,supplierVO);
        return ResponseBean.success();
    }
    @ApiOperation(value = "删除物资来源")
    @DeleteMapping ("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id)  {
        supplierService.delete(id);
        return ResponseBean.success();
    }
    @ApiOperation(value = "查找所有")
    @GetMapping ("/findAll")
    public ResponseBean<List<SupplierVO>> findAll(){
        List<SupplierVO> supplierVOList = supplierService.findAll();
        return ResponseBean.success(supplierVOList);
    }
}
