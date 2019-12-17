package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.HacLoginDto;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.service.HacLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by 19012964 on 2019/12/16.
 */
@Api(value = "用户登录接口", tags = {"用户登录"})
@RestController
@Slf4j
public class LoginController {
    @Autowired
    private HacLoginService hacLoginService;

    public static String JWT_AUTH_HEADER = "Authorization";

    @PostMapping(value = {"/login"})
    @ApiOperation(value = "登录")
    public R hacLogin(@RequestBody @Validated HacLoginDto hacSignInDTO) {
        return hacLoginService.login(hacSignInDTO);
    }
}
