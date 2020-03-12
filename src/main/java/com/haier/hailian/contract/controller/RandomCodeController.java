package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.util.RandomValidateCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * ${desc}
 * </p>
 */
@Slf4j
@RestController
@RequestMapping(value = {"/code"})
@Api(value = "图形验证码接口", tags = {"图形验证码接口"})
public class RandomCodeController {
    /**
     * 生成验证码
     */
    @ApiOperation(value = "获取图形验证码")
    @GetMapping(value = "/getVerifyCode")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            //设置相应类型,告诉浏览器输出的内容为图片
            response.setContentType("image/jpeg");
            //设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            //输出验证码图片方法
            randomValidateCode.getRandcode(request, response);
        } catch (Exception e) {
            log.error("获取验证码失败>>>>   ", e);
        }
    }


    /**
     * 生成验证码
     */
    @RequestMapping(value = "/testVerify")
    public String testVerify(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        return session.getAttribute(RandomValidateCodeUtil.RANDOM_CODE_KEY).toString();
    }
}
