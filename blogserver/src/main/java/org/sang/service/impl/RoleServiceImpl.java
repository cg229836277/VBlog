package org.sang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.sang.dataobject.RoleDO;
import org.sang.mapper.RoleMapper;
import org.sang.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDO> implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;


    @Override
    public RoleDO getRoleById(long id) {
        return roleMapper.selectById(id);
    }

    @Override
    public RoleDO getRoleByUserId(long userId) {
        return roleMapper.selectByUserId(userId);
    }
}
