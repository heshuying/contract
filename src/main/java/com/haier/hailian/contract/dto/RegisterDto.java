package com.haier.hailian.contract.dto;

import com.haier.hailian.contract.util.Constant;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created by 19012964 on 2020/3/10.
 */
@Data
public class RegisterDto {
    @NotBlank(message = "手机号不可以为空！")
    @Pattern(regexp = Constant.REGEX_MOBILE, message = "手机号校验失败")
    private String cellphone;
    @Pattern(regexp = Constant.REGEX_PASSWORD, message = "密码不满足格式要求")
    @NotBlank(message = "密码不可以为空！")
    private String password;
    @NotBlank(message = "验证码不可以为空！")
    private String validecode;
}
