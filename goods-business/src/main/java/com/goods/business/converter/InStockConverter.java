package com.goods.business.converter;

import com.goods.business.mapper.SupplierMapper;
import com.goods.common.model.business.InStock;
import com.goods.common.model.business.Supplier;
import com.goods.common.vo.business.InStockVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author songxin
 * @date 2022/5/29
 **/
@Component
public class InStockConverter {
    @Autowired
    private SupplierMapper supplierMapper;
    /**
     * 转vo
     * @return
     */
    public  InStockVO converterToInStockVO(InStock inStock){
        InStockVO inStockVO = new InStockVO();
        BeanUtils.copyProperties(inStock,inStockVO);
        return inStockVO;
    }
    /**
     * 转model
     * @return
     */
    public InStock converterToInStock(InStockVO inStockVO){
        InStock inStock = new InStock();
        BeanUtils.copyProperties(inStockVO,inStock);
        return inStock;
    }
    /**
     * 转为 SupplierVOList
     * @author songxin
     * @date 2022/5/29
     * @param inStockList
     * @return java.util.List<com.goods.common.vo.business.SupplierVO>
     */
    public  List<InStockVO> converterToInStockVOList(List<InStock>  inStockList) {
        List<InStockVO> inStockVOList = inStockList.stream().map(inStock -> {
            InStockVO inStockVO = new InStockVO();
            BeanUtils.copyProperties(inStock, inStockVO);
            Supplier supplier = supplierMapper.selectByPrimaryKey(inStock.getSupplierId());
            if (supplier != null) {
                inStockVO.setSupplierName(supplier.getName());
                inStockVO.setPhone(supplier.getPhone());
            }
            return inStockVO;
        }).collect(Collectors.toList());
        return inStockVOList;
    }
}
