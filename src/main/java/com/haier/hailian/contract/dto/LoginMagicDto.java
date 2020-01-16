package com.haier.hailian.contract.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginMagicDto {
    @ApiModelProperty(value="用户名",name="userName",required = true)
    @NotBlank(message = "用户名不可以为空！")
    private String userName;

    @ApiModelProperty(value="标识",name="salt",required = true)
    @NotBlank(message = "标识！")
    private String salt;

}
