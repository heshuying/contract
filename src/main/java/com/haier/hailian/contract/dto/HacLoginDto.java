package com.haier.hailian.contract.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "hac登陆实体类",description = "hac登陆实体类")
public class HacLoginDto {
    @ApiModelProperty(value="用户名",name="userName",required = true)
    @NotBlank(message = "用户名不可以为空！")
    private String userName;

    @ApiModelProperty(value="密码",name="password",required = true)
    @NotBlank(message = "密码不可以为空！")
    private String password;

}
