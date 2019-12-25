package com.haier.hailian.contract.entity;

import java.io.Serializable;

/**
 * (ZReservePlanTeamworkDetail)实体类
 *
 * @author makejava
 * @since 2019-12-24 17:13:55
 */
public class ZReservePlanTeamworkDetail implements Serializable {
    private static final long serialVersionUID = -48987379389347499L;
    //主键
    private Integer id;
    //问题id
    private Integer parentId;
    //预案内容
    private String content;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}