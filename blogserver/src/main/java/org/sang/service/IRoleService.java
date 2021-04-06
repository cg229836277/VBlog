package org.sang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sang.dataobject.RoleDO;

public interface IRoleService extends IService<RoleDO> {
    RoleDO getRoleById(long id);

    RoleDO getRoleByUserId(long userId);
}
