package org.sang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sang.dataobject.UserDO;
import org.sang.vo.CommonResult;

public interface IUsersService extends IService<UserDO> {
    UserDO getUserByUserName(String userName);

    void insert(UserDO userDO);

    CommonResult<UserDO> login(final UserDO userDO);

    CommonResult<UserDO> register(final UserDO userDO);
}
