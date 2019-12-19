package com.haier.hailian.contract.entity;

import lombok.Data;


@Data
public class SysRole{
    private String id;
    /**
     * 角色名称
     */
    private String name;

    private String roleCode;

}
