package com.goods.business.service;

import com.goods.common.model.business.Supplier;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;

import java.util.List;

/**
 * @author songxin
 * @date 2022/5/29
 **/
public interface SupplierService {
    /**
     * 查找全部物资来源
     * @author songxin
     * @date 2022/5/29
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.SupplierVO>
     * @param pageNum
     * @param pageSize
     * @param supplierVO
     */
    PageVO<SupplierVO> findSupplierList(Integer pageNum, Integer pageSize, SupplierVO supplierVO);
    /**
     * 添加物资来源
     * @author songxin
     * @date 2022/5/29
     * @param supplierVO
     * @return com.goods.common.model.business.Supplier
     */
    Supplier add(SupplierVO supplierVO);
    /**
     * 编辑物资来源
     * @author songxin
     * @date 2022/5/29
     * @param id
     * @return com.goods.common.vo.business.SupplierVO
     */
    SupplierVO edit(Long id);
    /**
     * 更新物资来源
     * @author songxin
     * @date 2022/5/29
     * @param id
     * @return int
     */
    void update(Long id,SupplierVO supplierVO);
    /**
     * 删除物资来源
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    void delete(Long id);
    /**
     * 查找所有
     * @author songxin
     * @date 2022/5/29
     * @return java.util.List<com.goods.common.vo.business.SupplierVO>
     */
    List<SupplierVO> findAll();

}
