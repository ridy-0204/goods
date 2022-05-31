package com.goods.business.converter;

import com.goods.common.model.business.Supplier;
import com.goods.common.vo.business.SupplierVO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author songxin
 * @date 2022/5/29
 **/
public class SupplierConverter {
    /**
     * 转vo
     * @return
     */
    public static SupplierVO converterToSupplierVO(Supplier supplier){
        SupplierVO supplierVO = new SupplierVO();
        BeanUtils.copyProperties(supplier,supplierVO);
        return supplierVO;
    }
    /**
     * 转model
     * @return
     */
    public static Supplier converterToSupplier(SupplierVO supplierVO){
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierVO,supplier);
        return supplier;
    }
    /**
     * 转为 SupplierVOList
     * @author songxin
     * @date 2022/5/29
     * @param supplierList
     * @return java.util.List<com.goods.common.vo.business.SupplierVO>
     */
    public static List<SupplierVO> converterToSupplierVOList(List<Supplier>  supplierList){
        List<SupplierVO> supplierVOList = supplierList.stream().map(supplier -> {
            SupplierVO supplierVO = new SupplierVO();
            BeanUtils.copyProperties(supplier, supplierVO);
            return supplierVO;
        }).collect(Collectors.toList());
        return supplierVOList;
    }
}
