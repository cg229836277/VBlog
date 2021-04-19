package org.sang.service;

import org.sang.dataobject.CategoryDO;

import java.util.List;

public interface ICategoryService {
    List<CategoryDO> getAllCategories();

    List<CategoryDO> getByParentName(String parentName);

    boolean addCategory(CategoryDO categoryDO);

    boolean deleteCategory(long id);

    boolean deleteCategories(int[] ids);

    boolean updateCategory(CategoryDO categoryDO);
}
