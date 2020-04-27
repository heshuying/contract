package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 19033715
 * @since 2020-04-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VwDmLqDim extends Model<VwDmLqDim> {

    private static final long serialVersionUID=1L;

    /**
     * rowid
     */
    private String rowId;

    /**
     * 链群内部编码
     */
    private String chainGroupCodeL;

    /**
     * 链群编号
     */
    private String chainCodeL;

    /**
     * 链群名称
     */
    private String chainNameL;

    /**
     * 大类
     */
    private String majorClass;

    /**
     * 大类名称
     */
    private String majorValueMeaning;

    /**
     * 子项目
     */
    private String subClass;

    /**
     * 子项目名称
     */
    private String subValueMeaning;

    /**
     * 链群属性
     */
    private String chainAttr;

    /**
     * 链群属性名称
     */
    private String chainValueMeaning;

    /**
     * 序号
     */
    private String chainNo;

    /**
     * 简写
     */
    private String shortName;

    /**
     * 自扩展
     */
    private String customExt;

    /**
     * 样板标记
     */
    private String isTpt;

    /**
     * 删除标记
     */
    private String deleteFlag;

    /**
     * 使用标记
     */
    private String activeFlag;

    /**
     * 发布审核标记
     */
    private String auditFlag;

    /**
     * 主链群编码
     */
    private String chainCodeZ;

    /**
     * 主链群名称
     */
    private String chainNameZ;

    /**
     * 主链群内部编码
     */
    private String chainGroupCodeZ;

    /**
     * 子项目负责人
     */
    private String subClassManager;

    /**
     * 链群负责人
     */
    private String chainManager;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 最后更新人
     */
    private String lastUpdBy;

    /**
     * 最后更新时间
     */
    private Date lastUpd;

    /**
     * 链群主工号
     */
    private String chainMangerNo;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
