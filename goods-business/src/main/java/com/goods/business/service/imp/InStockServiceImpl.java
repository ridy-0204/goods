package com.goods.business.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.converter.InStockConverter;
import com.goods.business.converter.SupplierConverter;
import com.goods.business.mapper.*;
import com.goods.business.service.InStockService;
import com.goods.common.error.BusinessCodeEnum;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.*;
import com.goods.common.response.ActiveUser;
import com.goods.common.vo.business.*;
import com.goods.common.vo.system.PageVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author songxin
 * @date 2022/5/28
 **/
@Service
public class InStockServiceImpl implements InStockService {
    @Autowired
    private InStockConverter inStockConverter;
    @Autowired
    private InStockMapper inStockMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private InStockInfoMapper inStockInfoMapper;
    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private ProductStockMapper productStockMapper;
    /**
     * 查找所有入库记录
     * @author songxin
     * @date 2022/5/28
     * @param pageNum
     * @param pageSize
     * @return java.util.List<com.goods.common.model.business.InStock>
     */
    @Override
    public   PageVO<InStockVO> findInStockList(Integer pageNum, Integer pageSize,InStockVO inStockVO) {

        Example example = new Example(InStock.class);
        Example.Criteria criteria = example.createCriteria();
        if (inStockVO.getInNum() != null && !"".equals(inStockVO.getInNum())) {
            criteria.andLike("in_num", "%" + inStockVO.getName() + "%");
        }
        if(inStockVO.getType()!=null&&!"".equals(inStockVO.getType())){
            criteria.andEqualTo("type",inStockVO.getType());
        }
        if (inStockVO.getStatus() != null) {
            criteria.andEqualTo("status",inStockVO.getStatus());
        }
        if(inStockVO.getStartTime()!=null){
            criteria.andGreaterThanOrEqualTo("createTime",inStockVO.getStartTime());
        }
        if(inStockVO.getEndTime()!=null){
            criteria.andLessThanOrEqualTo("createTime",inStockVO.getEndTime());
        }
        example.setOrderByClause("create_time desc");

        List<InStock> inStockList = inStockMapper.selectByExample(example);
        List<InStockVO> inStockVOS = inStockConverter.converterToInStockVOList(inStockList);
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<InStock> info = new PageInfo<>(inStockList);
        return new PageVO<>(info.getTotal(),inStockVOS);
    }
    /**
     * 添加入库记录
     * @author songxin
     * @date 2022/5/29
     * @param inStockVO
     */
    @Override
    public void addIntoStock(InStockVO inStockVO) throws BusinessException{
        String IN_STOCK_NUM = UUID.randomUUID().toString().substring(0, 32).replace("-","");
        List<Object> products = inStockVO.getProducts();
        //记录该单的总数
        int count = 0;
        if (products != null) {
            for (Object product : products) {
                Map map=JSONObject.parseObject(JSONObject.toJSONString(product));
                int productId = (int) map.get("productId");
                int productNumber = (int) map.get("productNumber");
                Product dbProduct = productMapper.selectByPrimaryKey(productId);
                if (dbProduct == null) {
                    throw new BusinessException(BusinessCodeEnum.PRODUCT_NOT_FOUND,dbProduct.getName()+"物资找不到");
                } else if (dbProduct.getStatus() == 1) {
                    throw new BusinessException(BusinessCodeEnum.PRODUCT_IS_REMOVE, dbProduct.getName() + "物资已被回收,无法入库");
                } else if (dbProduct.getStatus() == 2) {
                    throw new BusinessException(BusinessCodeEnum.PRODUCT_WAIT_PASS, dbProduct.getName() + "物资待审核,无法入库");
                } else {
                    count += productNumber;
                    //插入入库单明细
                    InStockInfo inStockInfo = new InStockInfo();
                    inStockInfo.setCreateTime(new Date());
                    inStockInfo.setModifiedTime(new Date());
                    inStockInfo.setProductNumber(productNumber);
                    inStockInfo.setPNum(dbProduct.getPNum());
                    inStockInfo.setInNum(IN_STOCK_NUM);
                    inStockInfoMapper.insert(inStockInfo);
                }
            }
            InStock inStock = inStockConverter.converterToInStock(inStockVO);
            inStock.setCreateTime(new Date());
            inStock.setModified(new Date());
            inStock.setInNum(IN_STOCK_NUM);
            inStock.setProductNumber(count);
            inStock.setStatus(2);
            ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
            inStock.setOperator(activeUser.getUser().getUsername());
            inStockMapper.insert(inStock);
        }else {
            throw new BusinessException(BusinessCodeEnum.PRODUCT_IN_STOCK_EMPTY);
        }

    }

    /**
     * 移入回收站
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    @Override
    public void remove(Long id) {
        this.removeAndBackAndPublish(id,1);
    }
    /**
     * 从回收站恢复
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    @Override
    public void back(Long id) {
        this.removeAndBackAndPublish(id,0);
    }


    /**
     * 审核通过
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    @Override
    public void publish(Long id) throws BusinessException {
        InStock inStock = inStockMapper.selectByPrimaryKey(id);
        Supplier supplier = supplierMapper.selectByPrimaryKey(inStock.getSupplierId());
        if(inStock==null){
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"入库单不存在");
        }
        if(inStock.getStatus()!=2){
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"入库单状态错误");
        }
        if(supplier==null){
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"入库来源信息错误");
        }
        //单号
        String inNum = inStock.getInNum();
        Example o = new Example(InStockInfo.class);
        o.createCriteria().andEqualTo("inNum",inNum);
        List<InStockInfo> infoList = inStockInfoMapper.selectByExample(o);
        if(!CollectionUtils.isEmpty(infoList)){
            infoList.stream().forEach(inStockInfo -> {
                String pNum = inStockInfo.getPNum();
                Integer productNumber = inStockInfo.getProductNumber();
                Product product = new Product();
                product.setPNum(pNum);
                Product product1 = productMapper.selectOne(product);
                if ( product1!= null) {
                    ProductStock productStock = new ProductStock();
                    productStock.setPNum(pNum);
                    ProductStock productStock1 = productStockMapper.selectOne(productStock);
                    if (productStock1 != null) {
                        productStock1.setStock(productStock1.getStock() + productNumber);
                        productStockMapper.updateByPrimaryKeySelective(productStock1);
                    } else {
                        productStock.setStock(Long.valueOf(productNumber));
                        productStockMapper.insert(productStock);
                    }
                }
            });
            //修改入库单状态.
            inStock.setCreateTime(new Date());
            inStock.setStatus(0);
            inStockMapper.updateByPrimaryKeySelective(inStock);
        }else {
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"入库的明细不能为空");
        }
        this.removeAndBackAndPublish(id,0);
    }
    /**
     * 更新状态
     * @author songxin
     * @date 2022/5/29
     * @param id
     * @param status
     */
    private void removeAndBackAndPublish(Long id, Integer status) {
        InStock inStock = new InStock();
        inStock.setId(id);
        inStock.setModified(new Date());
        inStock.setStatus(status);
        inStockMapper.updateByPrimaryKeySelective(inStock);
    }
    /**
     * 逻辑删除
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    @Override
    public void delete(Long id) throws BusinessException {
        InStock in = new InStock();
        in.setId(id);
        InStock inStock = inStockMapper.selectByPrimaryKey(in);
        //只有处于回收站,或者待审核的情况下可删除
        if(inStock==null){
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"入库单不存在");
        }else if(inStock.getStatus()!=1&&inStock.getStatus()!=2){
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"入库单状态错误,无法删除");
        }else {
            inStockMapper.deleteByPrimaryKey(id);
        }
        //单号，删除入库信息
        String inNum = inStock.getInNum();
        Example o = new Example(InStockInfo.class);
        o.createCriteria().andEqualTo("inNum",inNum);
        inStockInfoMapper.deleteByExample(o);
    }
    /**
     * 查找入库记录详情
     * @author songxin
     * @date 2022/5/30
     * @param id
     * @param pageNum
     * @param pageNum
     * @return com.goods.common.vo.business.InStockDetailVO
     */
    @Override
    public InStockDetailVO  detail(Long id,Integer pageNum) {
            PageHelper.startPage(pageNum,5);
          /** 总数**/
        InStock inStock = inStockMapper.selectByPrimaryKey(id);
        String inNum = inStock.getInNum();
        Integer status = inStock.getStatus();
        Integer type = inStock.getType();
        String operator = inStock.getOperator();
        Supplier supplier = supplierMapper.selectByPrimaryKey(inStock.getSupplierId());
        SupplierVO supplierVO = SupplierConverter.converterToSupplierVO(supplier);
        //查询入库明细中商品信息
        InStockInfo inStockInfo = new InStockInfo();
        inStockInfo.setInNum(inNum);
        List<InStockInfo> inStockInfoList = inStockInfoMapper.select(inStockInfo);
        List<InStockItemVO> inStockItemVOS = inStockInfoList.stream().map(inStockInfo1 -> {
            Product product = new Product();
            product.setPNum(inStockInfo1.getPNum());
            Product product1 = productMapper.selectOne(product);
            InStockItemVO inStockItemVO = new InStockItemVO();
            if (product1 == null) {
                return null;
            }
            BeanUtils.copyProperties(product1, inStockItemVO);
            inStockItemVO.setCount(inStockInfo1.getProductNumber());
            return inStockItemVO;
        }).collect(Collectors.toList());

        InStockDetailVO inStockDetailVO = new InStockDetailVO();
        inStockDetailVO.setItemVOS(inStockItemVOS);
        inStockDetailVO.setStatus(status);
        inStockDetailVO.setType(type);
        inStockDetailVO.setOperator(operator);
        inStockDetailVO.setSupplierVO(supplierVO);
        inStockDetailVO.setTotal(inStockItemVOS.size());
        inStockDetailVO.setInNum(inNum);
        return inStockDetailVO;

    }
}
