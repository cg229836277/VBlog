package org.sang.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.sang.dataobject.UserDO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<UserDO> {

    default UserDO selectByUsername(@Param("username") String userName) {
        return selectOne(new QueryWrapper<UserDO>().eq("username", userName));
    }

    default int insertUser(UserDO userDO) {
        return insert(userDO);
    }
}
