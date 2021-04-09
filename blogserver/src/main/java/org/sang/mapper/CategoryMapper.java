package org.sang.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.sang.dataobject.CategoryDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper extends BaseMapper<CategoryDO> {
    default List<CategoryDO> selectAllCategories() {
        return selectList(new QueryWrapper<CategoryDO>());
    }
}
