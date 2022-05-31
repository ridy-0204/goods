package com.goods.business.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.converter.ConsumerConverter;
import com.goods.business.converter.OutStockConverter;
import com.goods.business.mapper.*;
import com.goods.business.service.OutStockService;
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
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author songxin
 * @date 2022/5/30
 **/
@Service
public class OutStockServiceImpl implements OutStockService {
    @Autowired
    private OutStockMapper outStockMapper;
    @Autowired
    private ConsumerMapper consumerMapper;
    @Autowired
    private ProductStockMapper productStockMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OutStockInfoMapper outStockInfoMapper;
    /**
     * 查询所有出库
     * @author songxin
     * @date 2022/5/30
     * @param pageNum
     * @param pageSize
     * @param outStockVO
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.OutStockVO>
     */
    @Override
    public PageVO<OutStockVO> findOutStockList(Integer pageNum, Integer pageSize, OutStockVO outStockVO) {

        Example example = new Example(InStock.class);
        Example.Criteria criteria = example.createCriteria();
        if (outStockVO.getOutNum() != null && !"".equals(outStockVO.getOutNum())) {
            criteria.andLike("out_num", "%" + outStockVO.getName() + "%");
        }
        if(outStockVO.getType()!=null&&!"".equals(outStockVO.getType())){
            criteria.andEqualTo("type",outStockVO.getType());
        }
        if (outStockVO.getStatus() != null) {
            criteria.andEqualTo("status",outStockVO.getStatus());
        }

        example.setOrderByClause("create_time desc");

        List<OutStock> outStocks = outStockMapper.selectByExample(example);
        List<OutStockVO> outStockVOS = outStocks.stream().map(outStock -> {
            Long consumerId = outStock.getConsumerId();
            Consumer consumer = consumerMapper.selectByPrimaryKey(consumerId);
            OutStockVO outStockVO1 = OutStockConverter.converterToProductVO(outStock);
            outStockVO1.setAddress(consumer.getAddress());
            outStockVO1.setPhone(consumer.getPhone());
            outStockVO1.setName(consumer.getName());
            outStockVO1.setSort(consumer.getSort());
            outStockVO1.setContact(consumer.getContact());
            return outStockVO1;
        }).collect(Collectors.toList());
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<OutStockVO> info = new PageInfo<>(outStockVOS);
        return new PageVO<>(info.getTotal(),outStockVOS);
    }
    /**
     * 添加出库
     * @author songxin
     * @date 2022/5/30
     * @param outStockVO
     */
    @Override
    public void addOutStock(OutStockVO outStockVO) throws BusinessException {
        String OUT_STOCK_NUM = UUID.randomUUID().toString().substring(0, 32).replace("-","");
        List<Object> products = outStockVO.getProducts();
        int count = 0;
        for (Object product : products) {
            Map map = JSONObject.parseObject(JSONObject.toJSONString(product));
            int productId = (int) map.get("productId");
            int productNumber = (int) map.get("productNumber");
            Product dbProduct = productMapper.selectByPrimaryKey(productId);
            if (dbProduct == null) {
                throw new BusinessException(BusinessCodeEnum.PRODUCT_NOT_FOUND,dbProduct.getName()+"物资找不到");
            } else if (dbProduct.getStatus() == 1) {
                throw new BusinessException(BusinessCodeEnum.PRODUCT_IS_REMOVE, dbProduct.getName() + "物资已被回收,无法出库");
            } else if (dbProduct.getStatus() == 2) {
                throw new BusinessException(BusinessCodeEnum.PRODUCT_WAIT_PASS, dbProduct.getName() + "物资待审核,无法出库");
            } else {
                count+= productNumber;
                OutStockInfo outStockInfo = new OutStockInfo();
                outStockInfo.setCreateTime(new Date());
                outStockInfo.setPNum(dbProduct.getPNum());
                outStockInfo.setModifiedTime(new Date());
                outStockInfo.setOutNum(OUT_STOCK_NUM);
                outStockInfo.setProductNumber(productNumber);
                outStockInfoMapper.insert(outStockInfo);
            }
        }
        OutStock outStock = OutStockConverter.converterToProduct(outStockVO);
        outStock.setOutNum(OUT_STOCK_NUM);
        outStock.setCreateTime(new Date());
        outStock.setStatus(2);
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        outStock.setOperator(activeUser.getUser().getUsername());
        outStock.setProductNumber(count);
        outStockMapper.insert(outStock);
    }
    /**
     * 移入回收
     * @author songxin
     * @date 2022/5/31
     * @param id
     */
    @Override
    public void remove(Long id) throws BusinessException {
        OutStock outStock = outStockMapper.selectByPrimaryKey(id);
        if (outStock == null) {
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"入库单不存在");
        }
        if(outStock.getStatus() != 0){
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"入库单状态不正确");
        }
        this.removeAndBackAndPublish(id,1);
    }
    /**
     * 从回收站恢复
     * @author songxin
     * @date 2022/5/31
     * @param id
     */
    @Override
    public void back(Long id) throws BusinessException {
        OutStock outStock = outStockMapper.selectByPrimaryKey(id);
        if (outStock == null) {
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"入库单不存在");
        }
        if (outStock.getStatus() != 1) {
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"入库单状态不正确");
        }
        this.removeAndBackAndPublish(id,0);
    }
    /**
     * 审核
     * @author songxin
     * @date 2022/5/31
     * @param id
     */
    @Override
    public void publish(Long id) throws BusinessException {
        OutStock outStock = outStockMapper.selectByPrimaryKey(id);
        if (outStock == null) {
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"入库单不存在");
        }
        if (outStock.getStatus() != 2) {
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"入库单状态不正确");
        }
        String outNum = outStock.getOutNum();
        OutStockInfo outStockInfo = new OutStockInfo();
        outStockInfo.setOutNum(outNum);
        List<OutStockInfo> outStockInfoList = outStockInfoMapper.select(outStockInfo);
        for (OutStockInfo info : outStockInfoList) {
            String pNum = info.getPNum();
            Integer productNumber = info.getProductNumber();
            Product product = new Product();
            product.setPNum(pNum);
            Product dbProduct = productMapper.selectOne(product);
            if (dbProduct != null) {
                ProductStock productStock = new ProductStock();
                productStock.setPNum(pNum);
                ProductStock dbProductStock = productStockMapper.selectOne(productStock);
                long l = dbProductStock.getStock() - productNumber;
                if(l < 0){
                    throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"库存不足");
                }else{
                    dbProductStock.setStock(l);
                }
                productStockMapper.updateByPrimaryKey(dbProductStock);
                info.setModifiedTime(new Date());
                outStockInfoMapper.updateByPrimaryKey(info);
            }else {
                throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"商品不存在");
            }
        }
        outStock.setStatus(0);
        outStockMapper.updateByPrimaryKey(outStock);
    }
    /**
     * 删除
     * @author songxin
     * @date 2022/5/31
     * @param id
     */
    @Override
    public void delete(Long id) throws BusinessException {
        OutStock outStock = outStockMapper.selectByPrimaryKey(id);
        if (outStock == null) {
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"入库单不存在");
        }
        if (outStock.getStatus() == 0) {
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR,"入库单状态不正确");
        }
        outStockMapper.deleteByPrimaryKey(id);
        OutStockInfo outStockInfo = new OutStockInfo();
        outStockInfo.setOutNum(outStock.getOutNum());
        outStockInfoMapper.delete(outStockInfo);
    }
    /**
     * 更新状态
     * @author songxin
     * @date 2022/5/29
     * @param id
     * @param status
     */
    private void removeAndBackAndPublish(Long id, Integer status) {
        OutStock outStock = new OutStock();
        outStock.setId(id);
        outStock.setStatus(status);
        outStockMapper.updateByPrimaryKeySelective(outStock);
    }
    /**
     * 出库明细
     * @author songxin
     * @date 2022/5/31
     * @param id
     * @param pageNum
     * @return com.goods.common.vo.business.OutStockDetailVO
     */
    @Override
    public OutStockDetailVO detail(Long id, Integer pageNum) {
        PageHelper.startPage(pageNum,5);
        OutStock outStock = outStockMapper.selectByPrimaryKey(id);
        Consumer consumer = consumerMapper.selectByPrimaryKey(outStock.getConsumerId());
        ConsumerVO consumerVO = ConsumerConverter.converterToConsumerVO(consumer);

        OutStockInfo outStockInfo = new OutStockInfo();
        outStockInfo.setOutNum(outStock.getOutNum());
        List<OutStockInfo> outStockInfoList = outStockInfoMapper.select(outStockInfo);
        List<OutStockItemVO> outStockItemVOS = outStockInfoList.stream().map(outStockInfo1 -> {
            Product product1 = new Product();
            product1.setPNum(outStockInfo1.getPNum());
            Product product = productMapper.selectOne(product1);
            OutStockItemVO outStockItemVO = new OutStockItemVO();
            if (product != null) {
                BeanUtils.copyProperties(product, outStockItemVO);
                outStockItemVO.setCount(outStockInfo1.getProductNumber());
            }
            return outStockItemVO;
        }).collect(Collectors.toList());

        //返回数据
        OutStockDetailVO outStockDetailVO = new OutStockDetailVO();
        BeanUtils.copyProperties(outStock,outStockDetailVO);
        outStockDetailVO.setConsumerVO(consumerVO);
        outStockDetailVO.setItemVOS(outStockItemVOS);
        outStockDetailVO.setTotal(outStockInfoList.size());
        return outStockDetailVO;
    }
}
