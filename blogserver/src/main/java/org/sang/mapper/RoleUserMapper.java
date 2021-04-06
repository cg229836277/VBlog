package org.sang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.sang.dataobject.RoleUserDO;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleUserMapper extends BaseMapper<RoleUserDO> {
    default int insertRoleUserDO(RoleUserDO roleUserDO) {
        return insert(roleUserDO);
    }
}
