package org.sang.service.impl;

import org.sang.dataobject.CategoryDO;
import org.sang.mapper.CategoryMapper;
import org.sang.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryDO> getAllCategories() {
        return categoryMapper.selectAllCategories();
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
