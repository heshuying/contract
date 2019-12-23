package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.CurrentUser;
import com.haier.hailian.contract.dto.HacLoginDto;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.service.HacLoginService;
import com.haier.hailian.contract.util.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping(value = "/current/set")
    @ApiOperation(value = "设置当前所选用户")
    public R setCurrent(@RequestBody @Validated @ApiParam(value = "设置当前用户", required = true) CurrentUser currentUser) {
        Subject subject = SecurityUtils.getSubject();
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        sysUser.setCurrentUser(currentUser);
        return R.ok().put("data",sysUser);
    }
    @PostMapping(value = "/current/get")
    @ApiOperation(value = "设置当前所选用户")
    public R getCurrent(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();
        return R.ok().put("data",sysUser.getCurrentUser());
    }

    @PostMapping(value = "/logout")
    @ApiOperation(value = "注销登录")
    public R logout() {
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
        } catch (Exception e) {
            throw new RException(e.getMessage());
        }
        return R.ok();
    }

    @GetMapping(value = "/unauthorized")
    @ApiOperation(value = "未登陆提示401")
    public R unauthorized() {
        return R.error(Constant.CODE_AUTH,Constant.MSG_AUTH);
    }

    @GetMapping(value = "/forbidden")
    @ApiOperation(value = "没权限403")
    public R forbidden() {
        return R.error(Constant.CODE_FORBIDDEN,Constant.MSG_FORBIDDEN);
    }
}
