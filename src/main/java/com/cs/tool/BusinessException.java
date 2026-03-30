package com.cs.tool;

// 继承 RuntimeException，使异常成为非受检异常（推荐，无需强制 try-catch）
public class BusinessException extends RuntimeException {

    // 构造方法：仅传入异常消息
    public BusinessException(String message) {
        super(message);
    }

    // 构造方法：传入消息和原始异常（用于异常链）
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    // 可选：如果需要状态码等其他属性，可以添加
    private Integer code;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}