package org.sang.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sang.dataobject.RoleDO;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMapper extends BaseMapper<RoleDO> {

    default RoleDO selectById(@Param("id") long id) {
        return selectOne(new QueryWrapper<RoleDO>().eq("id", id));
    }

    default RoleDO selectByName(@Param("name") String name) {
        return selectOne(new QueryWrapper<RoleDO>().eq("name", name));
    }

    @Select("SELECT * FROM roles r LEFT JOIN roles_users r_u ON r.id = r_u.uid WHERE r_u.uid = #{userId}")
    RoleDO selectByUserId(@Param("userId") long userId);
}
