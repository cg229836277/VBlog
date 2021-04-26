package org.sang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sang.dataobject.CategoryDO;
import org.sang.dataobject.RoleDO;
import org.sang.mapper.CategoryMapper;
import org.sang.mapper.RoleMapper;
import org.sang.service.ICategoryService;
import org.sang.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryDO> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryDO> getAllCategories() {
        return categoryMapper.selectAllCategories();
    }

    @Override
    public List<CategoryDO> getByParentName(String parentName) {
        return categoryMapper.selectByParentName(parentName);
    }

    @Override
    public boolean addCategory(CategoryDO categoryDO) {
        return categoryMapper.addCategory(categoryDO);
    }

    @Override
    public boolean deleteCategory(long id) {
        return categoryMapper.deleteCategory(id);
    }

    @Override
    public boolean deleteCategories(int[] ids) {
        return categoryMapper.deleteCategories(ids);
    }

    @Override
    public boolean updateCategory(CategoryDO categoryDO) {
        return categoryMapper.updateCategory(categoryDO);
    }
}
