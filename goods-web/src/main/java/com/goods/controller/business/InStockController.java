package com.goods.controller.business;

import com.goods.business.service.InStockService;
import com.goods.business.service.SupplierService;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.Supplier;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.InStockDetailVO;
import com.goods.common.vo.business.InStockVO;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 业务模块-入库相关接口
 * @author songxin
 * @date 2022/5/28
 **/
@Api(tags = "业务模块-入库相关接口")
@RestController
@RequestMapping("/business/inStock")
public class InStockController {
    @Autowired
    private InStockService inStockService;
    @Autowired
    private SupplierService supplierService;
    /**
     * 部门列表
     * @return
     */
    @ApiOperation(value = "入库查询")
    @GetMapping("/findInStockList")
    //@GetMapping("/findInStockList/{pageNum}/{pageSize}/{status}")
    public ResponseBean<PageVO> findInStockList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                @RequestParam(value = "pageSize") Integer pageSize,
                                                InStockVO inStockVO){

       PageVO<InStockVO> pageVO =inStockService.findInStockList(pageNum,pageSize,inStockVO);

        return ResponseBean.success(pageVO);
    }
    @ApiOperation(value = "添加入库信息")
    @PostMapping ("/addIntoStock")
    public ResponseBean addIntoStock(@RequestBody InStockVO inStockVO)throws BusinessException {
        if (inStockVO.getSupplierId()==null) {
            SupplierVO supplierVO = new SupplierVO();
            BeanUtils.copyProperties(inStockVO,supplierVO);
            Supplier supplier = supplierService.add(supplierVO);
            inStockVO.setSupplierId(supplier.getId());
        }

        inStockService.addIntoStock(inStockVO);
        return ResponseBean.success();
    }
    @ApiOperation(value = "移入回收站")
    @PutMapping ("/remove/{id}")
    public ResponseBean remove(@PathVariable Long id)  {
        inStockService.remove(id);
        return ResponseBean.success();
    }
    @ApiOperation(value = "从回收站恢复")
    @PutMapping ("/back/{id}")
    public ResponseBean back(@PathVariable Long id)  {
        inStockService.back(id);
        return ResponseBean.success();
    }
    @ApiOperation(value = "审核通过")
    @PutMapping ("/publish/{id}")
    public ResponseBean publish(@PathVariable Long id) throws BusinessException {
        inStockService.publish(id);
        return ResponseBean.success();
    }
    @ApiOperation(value = "逻辑删除")
    @GetMapping ("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id) throws BusinessException {
        inStockService.delete(id);
        return ResponseBean.success();
    }
    @ApiOperation(value = "查询入库明细")
    @GetMapping ("/detail/{id}")
    public ResponseBean detail(@PathVariable Long id,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",required = false) Integer pageSize)  {
        InStockDetailVO inStockDetailVOList=inStockService.detail(id,pageNum);
        return ResponseBean.success(inStockDetailVOList);
    }
}
