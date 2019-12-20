package com.haier.hailian.contract.config;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.RException;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BindExceptionConfig {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleBindException1(MethodArgumentNotValidException e)
            throws RException {
        FieldError  fieldError=e.getBindingResult().getFieldError();
        assert fieldError != null;
        return R.error("400",
                fieldError.getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public R handleBindException1(Exception e)
            throws RException {
        if(e instanceof RException){
            RException rException=(RException) e;
            return R.error(rException.getCode(),rException.getMsg());
        }else {

            return R.error("500",
                    "请联系管理员");
        }
    }

}
