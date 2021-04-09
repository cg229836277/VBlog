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
}
