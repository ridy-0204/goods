package com.goods.controller.business;

import com.goods.business.service.ProductService;
import com.goods.common.error.BusinessException;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ProductStockVO;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author songxin
 * @date 2022/5/29
 **/
@ApiOperation("物资资料相关接口")
@RestController
@RequestMapping("/business/product")
public class ProductController {
    @Autowired
    private ProductService productService  ;

    @ApiOperation(value = "查找所有物资资料")
    @GetMapping("/findProductList")
    public ResponseBean<PageVO<ProductVO>> findProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                           @RequestParam(value = "pageSize") Integer pageSize,
                                                            ProductVO productVO){

        PageVO<ProductVO>productVOPageVO = productService.findProductList(pageNum,pageSize,productVO);
        return ResponseBean.success(productVOPageVO);
    }
    @ApiOperation(value = "查找所有物资资料")
    @GetMapping("/findProducts")
    public ResponseBean<PageVO<ProductVO>> findProducts(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                           @RequestParam(value = "pageSize") Integer pageSize,
                                                            ProductVO productVO){

        PageVO<ProductVO>productVOPageVO = productService.findProductList(pageNum,pageSize,productVO);
        return ResponseBean.success(productVOPageVO);
    }
    @ApiOperation(value = "添加物资资料")
    @PostMapping ("/add")
    public ResponseBean add(@RequestBody ProductVO productVO){

         productService.add(productVO);
        return ResponseBean.success();
    }
    @ApiOperation(value = "编辑物资资料")
    @GetMapping("/edit/{id}")
    public ResponseBean edit(@PathVariable Long id)  {
        ProductVO productVO= productService.edit(id);
        return ResponseBean.success(productVO);
    }
    @ApiOperation(value = "更新物资资料")
    @PutMapping ("/update/{id}")
    public ResponseBean update(@PathVariable Long id,
                               @RequestBody ProductVO productVO)  {
        productService.update(id,productVO);
        return ResponseBean.success();
    }
    @ApiOperation(value = "移入回收站")
    @PutMapping ("/remove/{id}")
    public ResponseBean remove(@PathVariable Long id)  {
        productService.remove(id);
        return ResponseBean.success();
    }
    @ApiOperation(value = "从回收站恢复")
    @PutMapping ("/back/{id}")
    public ResponseBean back(@PathVariable Long id)  {
        productService.back(id);
        return ResponseBean.success();
    }
    @ApiOperation(value = "审核通过")
    @PutMapping ("/publish/{id}")
    public ResponseBean publish(@PathVariable Long id)  {
        productService.publish(id);
        return ResponseBean.success();
    }
    @ApiOperation(value = "逻辑删除")
    @DeleteMapping ("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id) throws BusinessException {
        // TODO: 2022/5/30 查看入库明细是否有关联，如果有不能删除
        productService.delete(id);
        return ResponseBean.success();
    }
    @ApiOperation(value = "查找所有库存")
    @GetMapping ("/findAllStocks")
    public ResponseBean<List<ProductStockVO>> findAllStocks(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                            @RequestParam(value = "pageSize") Integer pageSize){
        List<ProductStockVO> productStockVOS = productService.findAllStocks(pageNum,pageSize);
        return ResponseBean.success(productStockVOS);
    }
    @ApiOperation(value = "加载库存信息")
    @GetMapping ("/findProductStocks")
    public ResponseBean<PageVO<ProductStockVO>> findProductStocks(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                            @RequestParam(value = "pageSize") Integer pageSize,
                                                                  ProductVO productVO){
        PageVO<ProductStockVO> productStockVOS = productService.findProductStocks(pageNum,pageSize,productVO);
        return ResponseBean.success(productStockVOS);
    }

}
