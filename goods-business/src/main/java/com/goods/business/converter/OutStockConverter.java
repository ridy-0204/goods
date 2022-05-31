package com.goods.business.converter;

import com.goods.common.model.business.OutStock;
import com.goods.common.vo.business.OutStockVO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author songxin
 * @date 2022/5/30
 **/
public class OutStockConverter {
    /**
     * 转vo
     * @return
     */
    public static OutStockVO converterToProductVO(OutStock outStock){
        OutStockVO outStockVO = new OutStockVO();
        BeanUtils.copyProperties(outStock,outStockVO);
        return outStockVO;
    }
    /**
     * 转model
     * @return
     */
    public static OutStock converterToProduct(OutStockVO outStockVO){
        OutStock outStock = new OutStock();
        BeanUtils.copyProperties(outStockVO,outStock);
        return outStock;
    }
    /**
     * 转为 ProductVOList
     * @author songxin
     * @date 2022/5/29
     * @return java.util.List<com.goods.common.vo.business.ProductVO>
     */
    public static List<OutStockVO> converterToVOList(List<OutStock> outStocks) {
        List<OutStockVO> outStockVOList = outStocks.stream().map(outStock -> {
            OutStockVO outStockVO = new OutStockVO();
            BeanUtils.copyProperties(outStock, outStockVO);
            return outStockVO;
        }).collect(Collectors.toList());
        return outStockVOList;
    }
}
