package com.haier.hailian.contract.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by 19012964 on 2020/3/16.
 */
@Data
public class ResetPwdDto {
    @NotBlank(message = "手机号不可以为空！")
    private String cellphone;
    @NotBlank(message = "密码不可以为空！")
    private String password;
    @NotBlank(message = "确认密码不可以为空！")
    private String confirmPwd;
}
