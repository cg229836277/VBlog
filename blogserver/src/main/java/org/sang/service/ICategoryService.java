package org.sang.service;

import org.sang.dataobject.CategoryDO;

import java.util.List;

public interface ICategoryService {
    List<CategoryDO> getAllCategories();
}
