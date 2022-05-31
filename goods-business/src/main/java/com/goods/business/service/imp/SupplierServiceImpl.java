package com.goods.business.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.converter.SupplierConverter;
import com.goods.business.mapper.SupplierMapper;
import com.goods.business.service.SupplierService;
import com.goods.common.model.business.Supplier;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author songxin
 * @date 2022/5/29
 **/
@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private SupplierMapper supplierMapper;
    /**
     * 查找全部物资来源
     * @author songxin
     * @date 2022/5/29
     * @return com.goods.common.vo.system.PageVO<com.goods.common.vo.business.SupplierVO>
     * @param pageNum
     * @param pageSize
     * @param supplierVO
     */
    @Override
    public PageVO<SupplierVO> findSupplierList(Integer pageNum, Integer pageSize, SupplierVO supplierVO) {
        Example example = new Example(Supplier.class);
        Example.Criteria criteria = example.createCriteria();
        PageHelper.startPage(pageNum, pageSize);
        example.setOrderByClause("sort asc");
        if (supplierVO.getName() != null && !"".equals(supplierVO.getName())) {
            criteria.andLike("name", "%" + supplierVO.getName() + "%");
        }
        if (supplierVO.getContact() != null && !"".equals(supplierVO.getContact())) {
            criteria.andLike("contact", "%" + supplierVO.getContact() + "%");
        }
        if (supplierVO.getAddress() != null && !"".equals(supplierVO.getAddress())) {
            criteria.andLike("address", "%" + supplierVO.getAddress() + "%");
        }
        List<Supplier> suppliers = supplierMapper.selectByExample(example);
        List<SupplierVO> categoryVOS= SupplierConverter.converterToSupplierVOList(suppliers);
        PageInfo<Supplier> info = new PageInfo<>(suppliers);
        return new PageVO<>(info.getTotal(), categoryVOS);
    }
    /**
     * 添加物资来源
     * @author songxin
     * @date 2022/5/29
     * @param supplierVO
     * @return com.goods.common.model.business.Supplier
     */
    @Override
    public Supplier add(SupplierVO supplierVO) {
        Supplier supplier = SupplierConverter.converterToSupplier(supplierVO);
        supplier.setCreateTime(new Date());
        supplier.setModifiedTime(new Date());
        supplierMapper.insert(supplier);
        return supplier;
    }
    /**
     * 编辑物资来源
     * @author songxin
     * @date 2022/5/29
     * @param id
     * @return com.goods.common.vo.business.SupplierVO
     */
    @Override
    public SupplierVO edit(Long id) {
        Supplier supplier = supplierMapper.selectByPrimaryKey(id);
        SupplierVO supplierVO = SupplierConverter.converterToSupplierVO(supplier);
        return supplierVO;
    }
    /**
     * 更新物资来源
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    @Override
    public void update(Long id,SupplierVO supplierVO) {
        Supplier supplier = SupplierConverter.converterToSupplier(supplierVO);
        supplier.setModifiedTime(new Date());
        supplierMapper.updateByPrimaryKey(supplier);
    }
    /**
     * 删除物资来源
     * @author songxin
     * @date 2022/5/29
     * @param id
     */
    @Override
    public void delete(Long id) {
        supplierMapper.deleteByPrimaryKey(id);
    }
    /**
     * 查找所有
     * @author songxin
     * @date 2022/5/29
     * @return java.util.List<com.goods.common.vo.business.SupplierVO>
     */
    @Override
    public List<SupplierVO> findAll() {
        List<Supplier> suppliers = supplierMapper.selectAll();
        List<SupplierVO> supplierVOList = SupplierConverter.converterToSupplierVOList(suppliers);
        return supplierVOList;
    }
}
