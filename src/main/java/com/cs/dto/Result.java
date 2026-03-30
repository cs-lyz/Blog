package com.cs.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result<T> {
    private Integer code;    // 状态码（如 200 成功，500 失败）
    private String message;  // 提示信息
    private T data;          // 具体业务数据（泛型）   登录就返回token
    private Long timestamp;  // 时间戳（可选）

    public static <T> Result<T> success(String message,T data) {
        return Result.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }
    public static <T> Result<T> fail(Integer code,String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .timestamp(System.currentTimeMillis())
                .build();
    }

}
