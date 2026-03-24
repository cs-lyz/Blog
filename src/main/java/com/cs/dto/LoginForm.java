package com.cs.dto;

import lombok.Data;

@Data
public class LoginForm {
    private String loginUserName;

    private String loginPassword;

    private String code;
}
