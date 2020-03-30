package com.haier.hailian.contract.config;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BindExceptionConfig {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleBindException1(MethodArgumentNotValidException e)
            throws RException {
        FieldError  fieldError=e.getBindingResult().getFieldError();
        assert fieldError != null;
        return R.error(Constant.CODE_VALIDFAIL,
                fieldError.getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public R handleBindException1(Exception e)
            throws RException {
        log.error(e.getMessage(), e);
        if(e instanceof RException){
            RException rException=(RException) e;
            return R.error(rException.getCode(),rException.getMsg());
        } else if(e instanceof AuthenticationException){
            AuthenticationException rException=(AuthenticationException) e;
            return R.error(Constant.CODE_ERROR,
                    rException.getMessage());
        }
        else {
            return R.error(Constant.CODE_ERROR,
                    Constant.MSG_ERROR);
        }
    }

}
