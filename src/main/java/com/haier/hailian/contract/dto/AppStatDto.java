package com.haier.hailian.contract.dto;

import lombok.Data;

@Data
public class AppStatDto {
    /**
     * PC、App
     */
    private String source;

    /**
     * 页面
     */
    private String page;

    /**
     * 用户编码
     */
    private String empSn;
}
