package org.sang.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.sang.dataobject.CategoryDO;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface CategoryMapper extends BaseMapper<CategoryDO> {
    default List<CategoryDO> selectAllCategories() {
        return selectList(new QueryWrapper<CategoryDO>());
    }

    default List<CategoryDO> selectByParentName(String parentName) {
        return selectList(new QueryWrapper<CategoryDO>().eq("parent_name", parentName));
    }

    default boolean addCategory(CategoryDO categoryDO) {
        insert(categoryDO);
        return categoryDO.getId() >= 0;
    }

    default boolean deleteCategory(long id) {
        int result = deleteById(id);
        return result > 0;
    }

    default boolean deleteCategories(int[] ids) {
        List<Integer> idsList = Arrays.stream(ids).boxed().collect(Collectors.toList());
        int result = deleteBatchIds(idsList);
        return result > 0;
    }

    default boolean updateCategory(CategoryDO categoryDO) {
        int result = updateById(categoryDO);
        return result > 0;
    }
}
