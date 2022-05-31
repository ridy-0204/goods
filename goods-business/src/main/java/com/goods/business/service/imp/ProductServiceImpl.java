package com.goods.business.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.converter.ProductConverter;
import com.goods.business.mapper.ProductMapper;
import com.goods.business.mapper.ProductStockMapper;
import com.goods.business.service.ProductService;
import com.goods.common.error.BusinessCodeEnum;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.Product;
import com.goods.common.model.business.ProductStock;
import com.goods.common.vo.business.ProductStockVO;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author songxin
 * @date 2022/5/29
 **/
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductStockMapper productStockMapper;

    /**
     * 查找所有物资资料
     *
     * @param pageNum
     * @param pageSize
     * @param productVO
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.ProductVO>
     * @author songxin
     * @date 2022/5/29
     */
    @Override
    public PageVO<ProductVO> findProductList(Integer pageNum, Integer pageSize, ProductVO productVO) {

        List<Product> products = new ArrayList<>();
        Example o = new Example(Product.class);
        Example.Criteria criteria = o.createCriteria();
        o.setOrderByClause("sort asc");
        if (!StringUtils.isEmpty(productVO.getName()) && "".equals(productVO.getName())) {
            criteria.andEqualTo("name", productVO.getName());
        }
        if (productVO.getStatus() != null) {
            criteria.andEqualTo("status", productVO.getStatus());
        }
        //String s = productVO.getCategoryKeys().toString();
        //List<Long> longs = JSON.parseArray(s, Long.class);
        List<Long> categorys = productVO.getCategorys();
        if (categorys != null && categorys.size() == 3) {
            criteria.andEqualTo("threeCategoryId", categorys.get(2));
            criteria.andEqualTo("twoCategoryId", categorys.get(1));
            criteria.andEqualTo("oneCategoryId", categorys.get(0));
        }
        if (categorys != null && categorys.size() == 2) {
            criteria.andEqualTo("twoCategoryId", categorys.get(1));
            criteria.andEqualTo("oneCategoryId", categorys.get(0));
        }
        if (categorys != null && categorys.size() == 1) {
            criteria.andEqualTo("oneCategoryId", categorys.get(0));
        }
        products = productMapper.selectByExample(o);
        List<ProductVO> categoryVOS = ProductConverter.converterToVOList(products);
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Product> info = new PageInfo<>(products);
        return new PageVO<>(info.getTotal(), categoryVOS);
    }

    /**
     * 添加物资资料
     *
     * @param productVO
     * @author songxin
     * @date 2022/5/29
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(ProductVO productVO) {
        Product product = ProductConverter.converterToProduct(productVO);
        product.setCreateTime(new Date());
        product.setModifiedTime(new Date());
        @NotNull(message = "分类不能为空")
        Long[] categoryKeys = productVO.getCategoryKeys();
        //未审核
        if (categoryKeys.length == 3) {
            product.setOneCategoryId(categoryKeys[0]);
            product.setTwoCategoryId(categoryKeys[1]);
            product.setThreeCategoryId(categoryKeys[2]);
        }
        product.setStatus(2);
        product.setPNum(UUID.randomUUID().toString().substring(0, 32));
        productMapper.insert(product);
    }

    /**
     * 编辑物资资料
     *
     * @param id
     * @return com.goods.common.vo.business.ProductVO
     * @author songxin
     * @date 2022/5/29
     */
    @Override
    public ProductVO edit(Long id) {
        Product product = productMapper.selectByPrimaryKey(id);
        ProductVO productVO = ProductConverter.converterToProductVO(product);
        return productVO;
    }

    /**
     * 更新物资资料
     *
     * @param id
     * @param productVO
     * @author songxin
     * @date 2022/5/29
     */
    @Override
    public void update(Long id, ProductVO productVO) {
        Product product = ProductConverter.converterToProduct(productVO);
        @NotNull(message = "分类不能为空") Long[] categoryKeys = productVO.getCategoryKeys();
        if (categoryKeys.length == 3) {
            product.setOneCategoryId(categoryKeys[0]);
            product.setTwoCategoryId(categoryKeys[1]);
            product.setThreeCategoryId(categoryKeys[2]);
        }
        productMapper.updateByPrimaryKey(product);
    }

    /**
     * 移入回收站
     *
     * @param id
     * @author songxin
     * @date 2022/5/29
     */
    @Override
    public void remove(Long id) {
        this.removeAndBackAndPublish(id, 1);
    }

    /**
     * 从回收站恢复
     *
     * @param id
     * @author songxin
     * @date 2022/5/29
     */
    @Override
    public void back(Long id) {
        this.removeAndBackAndPublish(id, 0);
    }


    /**
     * 审核通过
     *
     * @param id
     * @author songxin
     * @date 2022/5/29
     */
    @Override
    public void publish(Long id) {
        this.removeAndBackAndPublish(id, 0);
    }

    /**
     * 更新状态
     *
     * @param id
     * @param status
     * @author songxin
     * @date 2022/5/29
     */
    private void removeAndBackAndPublish(Long id, Integer status) {
        Product product = new Product();
        product.setId(id);
        product.setModifiedTime(new Date());
        product.setStatus(status);
        productMapper.updateByPrimaryKeySelective(product);
    }

    /**
     * 逻辑删除
     *
     * @param id
     * @author songxin
     * @date 2022/5/29
     */
    @Override
    public void delete(Long id) throws BusinessException {
        Product t = new Product();
        t.setId(id);
        Product product = productMapper.selectByPrimaryKey(t);
        //只有物资处于回收站,或者待审核的情况下可删除
        if (product.getStatus() != 1 && product.getStatus() != 2) {
            throw new BusinessException(BusinessCodeEnum.PRODUCT_STATUS_ERROR);
        } else {
            productMapper.deleteByPrimaryKey(id);
        }
        productMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查找所有
     *
     * @param pageNum
     * @param pageSize
     * @return java.util.List<com.goods.common.vo.business.ProductVO>
     * @author songxin
     * @date 2022/5/30
     */
    @Override
    public List<ProductStockVO> findAllStocks(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectAll();

        List<ProductStockVO> productStockVOS = products.stream().map(product -> {
            ProductStockVO productStockVO = new ProductStockVO();
            BeanUtils.copyProperties(product, productStockVO);
            //查找对应的库存
            ProductStock productStock = new ProductStock();
            productStock.setPNum(product.getPNum());
            ProductStock productStock1 = productStockMapper.selectOne(productStock);
            if (productStock1 != null) {
                productStockVO.setStock(productStock1.getStock());
            }

            return productStockVO;
        }).collect(Collectors.toList());

        return productStockVOS;
    }

    /**
     * 加载库存信息
     *
     * @param pageNum
     * @param pageSize
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.ProductStockVO>
     * @author songxin
     * @date 2022/5/30
     */
    @Override
    public PageVO<ProductStockVO> findProductStocks(Integer pageNum, Integer pageSize, ProductVO productVO) {
        PageHelper.startPage(pageNum, pageSize);
        List<Long> categorys = productVO.getCategorys();
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        if (categorys != null && categorys.size() == 3) {
            criteria.andEqualTo("threeCategoryId", categorys.get(2));
            criteria.andEqualTo("twoCategoryId", categorys.get(1));
            criteria.andEqualTo("oneCategoryId", categorys.get(0));
        }
        if (categorys != null && categorys.size() == 2) {
            criteria.andEqualTo("twoCategoryId", categorys.get(1));
            criteria.andEqualTo("oneCategoryId", categorys.get(0));
        }
        if (categorys != null && categorys.size() == 1) {
            criteria.andEqualTo("oneCategoryId", categorys.get(0));
        }
        if (productVO.getName() != null) {
            criteria.andLike("name", "%" + productVO.getName() + "%");
        }
        List<Product> products = productMapper.selectByExample(example);
        List<ProductStockVO> productStockVOList=new ArrayList<>();
        if(!CollectionUtils.isEmpty(products)){
            productStockVOList = products.stream().map(product -> {
                ProductStockVO productStockVO = new ProductStockVO();
                BeanUtils.copyProperties(product, productStockVO);
                //查找对应的库存
                ProductStock productStock = new ProductStock();
                productStock.setPNum(product.getPNum());
                ProductStock productStock1 = productStockMapper.selectOne(productStock);
                if (productStock1 != null) {
                    productStockVO.setStock(productStock1.getStock());
                }
                return productStockVO;
            }).collect(Collectors.toList());
        }
        PageInfo<ProductStockVO> pageInfo = new PageInfo<>(productStockVOList);
        return new PageVO<>(pageInfo.getTotal(),productStockVOList);
    }
}
