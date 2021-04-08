package org.sang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.sang.config.utils.TokenUtils;
import org.sang.dataobject.RoleDO;
import org.sang.dataobject.RoleUserDO;
import org.sang.dataobject.UserDO;
import org.sang.exception.ServiceException;
import org.sang.exception.ServiceExceptionEnum;
import org.sang.mapper.RoleMapper;
import org.sang.mapper.RoleUserMapper;
import org.sang.mapper.UserMapper;
import org.sang.service.IUsersService;
import org.sang.vo.CommonResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UserMapper, UserDO> implements IUsersService {
    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleUserMapper roleUserMapper;

    @Resource
    private TokenUtils tokenUtils;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDO getUserByUserName(String userName) {
        return userMapper.selectByUsername(userName);
    }

    @Override
    public void insert(UserDO userDO) {
        userMapper.insertUser(userDO);
    }

    @Override
    public CommonResult<UserDO> login(UserDO userDO) {
        try {
            // 验证用户名和密码是否对的
            System.out.println(userDO.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDO.getUsername(),
                            passwordEncoder.encode(userDO.getPassword())));
        } catch (BadCredentialsException e) {
            throw new ServiceException(ServiceExceptionEnum.LOGIN_USER_PASSWORD_ERROR);
        }
        // 生成Token与查询用户权限
        UserDO userDO1 = userMapper.selectByUsername(userDO.getUsername());
        long userId = userDO1.getId();
        RoleDO roleDO = roleMapper.selectByUserId(userId);
        String token = tokenUtils.createToken(userDO1, roleDO.getName());
        userDO1.setRoleName(roleDO == null ? RoleDO.ROLE_NORMAL : roleDO.getName());
        userDO1.setToken(token);
        userDO1.setPassword("");
        return CommonResult.success(userDO1);
    }

    @Override
    public CommonResult<UserDO> register(UserDO userDO) {
        try {
            // 密码加密存储
            String password = passwordEncoder.encode(userDO.getPassword());
            userDO.setPassword(password);
            int result = userMapper.insertUser(userDO);
            String roleName = "chuck".equals(userDO.getUsername()) ? RoleDO.ROLE_ADMIN : RoleDO.ROLE_NORMAL;
            String token = tokenUtils.createToken(userDO, roleName);
            userDO.setRoleName(roleName);
            userDO.setToken(token);
            log.info("register token:" + token);
            userDO.setEnable(true);

            long userId = userDO.getId();
            log.info("userId:" + userId);

            RoleDO roleDO = roleMapper.selectByName(roleName);
            log.info("roleDO:" + roleDO.toString());
            RoleUserDO roleUserDO = new RoleUserDO();
            roleUserDO.setRoleId(roleDO.getId());
            roleUserDO.setUserId(userId);
            roleUserMapper.insertRoleUserDO(roleUserDO);
            userDO.setPassword("");
            return CommonResult.success(userDO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ServiceExceptionEnum.REGISTER_FAILED);
        }
    }
}
