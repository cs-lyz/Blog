package com.cs.dto;

// HttpStatusEnum.java
public enum HttpStatusEnum {
    
    // 成功状态
    SUCCESS(200, "操作成功"),
    
    // 客户端错误（4xx）
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权，请先登录"),
    FORBIDDEN(403, "禁止访问，权限不足"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    
    // 参数验证错误
    PARAM_ERROR(4001, "参数验证失败"),
    PARAM_MISSING(4002, "缺少必要参数"),
    PARAM_TYPE_ERROR(4003, "参数类型错误"),
    
    // 用户认证相关错误
    USERNAME_OR_PASSWORD_ERROR(4001, "用户名或密码错误"),
    USER_NOT_FOUND(4004, "用户不存在"),
    USER_DISABLED(4005, "账户已被禁用"),
    USER_LOCKED(4006, "账户已被锁定"),
    USER_EXPIRED(4007, "账户已过期"),
    
    // 验证码错误
    CAPTCHA_ERROR(4008, "验证码错误"),
    CAPTCHA_EXPIRED(4009, "验证码已过期"),
    
    // Token相关错误
    TOKEN_MISSING(4010, "Token不能为空"),
    TOKEN_INVALID(4011, "Token无效"),
    TOKEN_EXPIRED(4012, "Token已过期"),
    
    // 权限错误
    PERMISSION_DENIED(4030, "权限不足，无法访问"),
    
    // 服务器错误（5xx）
    ERROR(500, "系统内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    DATABASE_ERROR(5001, "数据库操作失败"),
    NETWORK_ERROR(5002, "网络异常"),
    TIMEOUT_ERROR(5003, "请求超时"),
    
    // 业务逻辑错误
    BUSINESS_ERROR(6000, "业务处理失败"),
    DATA_EXIST(6001, "数据已存在"),
    DATA_NOT_EXIST(6002, "数据不存在"),
    OPERATION_FAILED(6003, "操作失败");
    
    private final Integer code;
    private final String message;
    
    HttpStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}