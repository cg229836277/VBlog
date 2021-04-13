package org.sang.exception;

public enum ServiceExceptionEnum {

    //系统级别
    SUCCESS(0, "成功"),
    SYS_ERROR(2001001000, "服务端发生异常"),
    MISSING_REQUEST_PARAM_ERROR(2001001001, "参数缺失"),
    AUTHORIZATION_FAIL_ERROR(2001001002, "认证失败"),
    TOKEN_GENERATE_FAIL_ERROR(2001001003, "Token生成失败"),
    JSON_PARSE_FAIL_ERROR(2001001004, "json解析失败"),

    //用户登录及注册相关
    USER_NOT_FOUND(1001002000, "用户不存在"),
    INVALID_REQUEST_PARAM_ERROR(1001002002, "请求参数不合法"),
    USER_ALREADY_EXIST_ERROR(1001002003, "用户已经存在"),
    USER_DEVICE_CHANGED_ERROR(1001002004, "用户登录的设备发生变更，请重新登录"),
    SAVE_USER_FAIL_ERROR(1001002005, "服务端保存用户失败"),
    LOGIN_USER_PASSWORD_ERROR(1001002006, "登录账号或密码错误"),
    USER_DELETED_OR_LOCKED_ERROR(1001002007, "用户已被删除或锁定"),
    MODIFY_DYNAMIC_CODE_ERROR(1001002008, "随机验证码错误"),
    LOGIN_FAILED(1001002009, "登录失败"),
    REGISTER_FAILED(1001002010, "注册失败"),
    LOGOUT_FAILED(1001002011, "注销失败"),
    LOGIN_EXPIRATION(1001002012, "登录过期，请重新登录"),

    SAVE_ARTICLE_FAIL(1001003000, "文章保存失败"),
    GET_CATEGORY_PARAMETERS_INVALID(1001003001, "文章栏目参数不完整"),
    ;

    /**
     * 错误码
     */
    private int code;
    /**
     * 错误提示
     */
    private String message;

    ServiceExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
