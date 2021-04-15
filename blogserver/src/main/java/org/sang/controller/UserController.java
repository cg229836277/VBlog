package org.sang.controller;

import org.sang.dataobject.UserDO;
import org.sang.exception.ServiceExceptionEnum;
import org.sang.service.IUsersService;
import org.sang.vo.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by sang on 2017/12/24.
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    IUsersService userService;

    @RequestMapping("/login_page")
    public CommonResult loginPage() {
        return CommonResult.error(ServiceExceptionEnum.LOGIN_FAILED.getCode(), ServiceExceptionEnum.LOGIN_FAILED.getMessage());
    }

    /**
     * 用户登录接口
     *
     * @param userDO 用户登录的用户名和密码
     * @return 用户Token和角色
     * @throws AuthenticationException 身份验证错误抛出异常
     */
    @PostMapping(value = "/login")
    public CommonResult<UserDO> login(@RequestBody final UserDO userDO) throws AuthenticationException {
        return userService.login(userDO);
    }

    /**
     * 用户注册接口
     *
     * @param userDO 用户注册信息
     * @return 用户注册结果
     */
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/register")
    public CommonResult<UserDO> register(@RequestBody @Valid final UserDO userDO) {
        return userService.register(userDO);
    }
}
