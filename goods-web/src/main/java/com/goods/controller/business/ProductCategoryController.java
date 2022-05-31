package com.goods.controller.business;

import com.goods.business.service.ProductCategoryService;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.ProductCategoryVO;
import com.goods.common.vo.system.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author songxin
 * @date 2022/5/28
 **/
@Api(tags = "业务模块-分类相关接口")
@RestController
@RequestMapping("/business/productCategory")
public class ProductCategoryController  {
    @Autowired
    private ProductCategoryService productCategoryService;
    @ApiOperation(value = "物资类别分类查询", notes = "物资分类,3层树")
    @GetMapping("/categoryTree")
    public ResponseBean<PageVO<ProductCategoryTreeNodeVO>>  categoryTree(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                        @RequestParam(value = "pageSize",required = false) Integer pageSize){
        PageVO<ProductCategoryTreeNodeVO> pageVO = productCategoryService.categoryTree(pageNum, pageSize);
        return ResponseBean.success(pageVO);
    }
    @ApiOperation(value = "物资类别查询父类列表", notes = "查询父类列表")
    @GetMapping("/getParentCategoryTree")
    public ResponseBean<List<ProductCategoryTreeNodeVO>>  getParentCategoryTree(){
        List<ProductCategoryTreeNodeVO> pageVO = productCategoryService.getParentCategoryTree();
        return ResponseBean.success(pageVO);
    }
    @ApiOperation(value = "物资类别,添加分类", notes = "添加列表")
    @PostMapping("/add")
    public ResponseBean  add(@RequestBody @Validated ProductCategoryVO productCategoryVO){
       productCategoryService.add(productCategoryVO);
        return ResponseBean.success();
    }
    @ApiOperation(value = "物资类别,回显分类")
    @GetMapping("/edit/{id}")
    public ResponseBean  add(@PathVariable Long id){
       ProductCategoryVO productCategoryVO = productCategoryService.selectOne(id);
        return ResponseBean.success(productCategoryVO);
    }
    @ApiOperation(value = "物资类别,更新分类")
    @PutMapping ("/update/{id}")
    public ResponseBean  update(@PathVariable Long id,
            @RequestBody ProductCategory productCategory){
        productCategoryService.update(productCategory);
        return ResponseBean.success();
    }
    @ApiOperation(value = "物资类别,删除分类")
    @DeleteMapping ("/delete/{id}")
    public ResponseBean  delete(@PathVariable Long id) throws BusinessException {
        productCategoryService.delete(id);
        return ResponseBean.success();
    }
}
