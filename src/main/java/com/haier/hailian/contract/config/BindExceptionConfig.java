package com.haier.hailian.contract.config;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.util.Constant;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
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
                Constant.MSG_VALIDFAIL);
    }

    @ExceptionHandler(Exception.class)
    public R handleBindException1(Exception e)
            throws RException {
        if(e instanceof RException){
            RException rException=(RException) e;
            return R.error(rException.getCode(),rException.getMsg());
        }else {
            log.error(e.getMessage());
            return R.error(Constant.CODE_VALIDFAIL,
                    Constant.MSG_VALIDFAIL);
        }
    }

}
