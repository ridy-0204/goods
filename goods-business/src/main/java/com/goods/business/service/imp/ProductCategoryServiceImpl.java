package com.goods.business.service.imp;

import com.github.pagehelper.PageHelper;
import com.goods.business.converter.ProductCategoryConverter;
import com.goods.business.mapper.ProductCategoryMapper;
import com.goods.business.service.ProductCategoryService;
import com.goods.common.error.BusinessCodeEnum;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.utils.CategoryTreeBuilder;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.ProductCategoryVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author songxin
 * @date 2022/5/28
 **/
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    /**
     * 物资分类树状查询
     * @author songxin
     * @date 2022/5/28
     * @param pageNum
     * @param pageSize
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.ProductCategoryVO>
     */
    @Override
    public PageVO<ProductCategoryTreeNodeVO> categoryTree(Integer pageNum, Integer pageSize) {

        List<ProductCategory> productCategories = productCategoryMapper.selectAll();
        //分类树vo集合
        List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVOS = new ArrayList<>();

        if(!CollectionUtils.isEmpty(productCategories)){
            for (ProductCategory productCategory : productCategories) {
                ProductCategoryTreeNodeVO productCategoryTreeNodeVO = new ProductCategoryTreeNodeVO();
                BeanUtils.copyProperties(productCategory,productCategoryTreeNodeVO);
                productCategoryTreeNodeVOS.add(productCategoryTreeNodeVO);
            }
        }
        List<ProductCategoryTreeNodeVO> build = CategoryTreeBuilder.build(productCategoryTreeNodeVOS);
        //PageInfo<ProductCategoryTreeNodeVO> info = new PageInfo<>(build);
        //List<ProductCategoryTreeNodeVO> pages = ListPageUtils.page(build,pageSize,pageNum);

        if(pageSize!=null){
            PageHelper.startPage(pageNum,pageSize);
        }

        return new PageVO<>(build.size(),build);
    }

    /**
     * 查询父类列表
     * @author songxin
     * @date 2022/5/29
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.ProductCategoryTreeNodeVO>
     */
    @Override
    public List<ProductCategoryTreeNodeVO> getParentCategoryTree() {

        List<ProductCategory> productCategories = productCategoryMapper.selectAll();
        //分类树vo集合
        List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVOS = new ArrayList<>();

        if(!CollectionUtils.isEmpty(productCategories)){
            for (ProductCategory productCategory : productCategories) {
                ProductCategoryTreeNodeVO productCategoryTreeNodeVO = new ProductCategoryTreeNodeVO();
                BeanUtils.copyProperties(productCategory,productCategoryTreeNodeVO);
                productCategoryTreeNodeVOS.add(productCategoryTreeNodeVO);
            }
        }
        List<ProductCategoryTreeNodeVO> build = CategoryTreeBuilder.build(productCategoryTreeNodeVOS);
        return build;
    }
    /**
     * 添加分类
     * @author songxin
     * @date 2022/5/29
     * @param productCategoryVO
     */
    @Transactional(rollbackFor = NullPointerException.class)
    @Override
    public void add(ProductCategoryVO productCategoryVO) {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(productCategoryVO,productCategory);
        productCategory.setCreateTime(new Date());
        productCategory.setModifiedTime(new Date());
        int insert = productCategoryMapper.insert(productCategory);
        System.out.println("insert = " + insert);
    }
    /**
     * 回显分类数据
     * @author songxin
     * @date 2022/5/29
     * @param id
     * @return com.goods.common.vo.business.ProductCategoryVO
     */
    @Override
    public ProductCategoryVO selectOne(Long id) {
        ProductCategory productCategory = productCategoryMapper.selectByPrimaryKey(id);
        ProductCategoryVO productCategoryVO = ProductCategoryConverter.converterToDepartmentVO(productCategory);
        return productCategoryVO;
    }
    /**
     * 更新分类id
     * @author songxin
     * @date 2022/5/29
     * @param productCategory
     */
    @Override
    public void update(ProductCategory productCategory) {
        productCategory.setModifiedTime(new Date());
        productCategoryMapper.updateByPrimaryKey(productCategory);
    }
    /**
     * 删除分类
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    @Override
    public void delete(Long id) throws BusinessException {
        Example example = new Example(ProductCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("pid",id);
        int i = productCategoryMapper.selectCountByExample(example);
        if(i!=0){
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"存在子节点,无法直接删除");
        }
        productCategoryMapper.deleteByPrimaryKey(id);
    }
}
