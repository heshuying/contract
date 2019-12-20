package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 01431594
 * @since 2019-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class XiaoweiEhr extends Model<XiaoweiEhr> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 小微编码
     */
    @TableField("xwCode")
    private String xwCode;

    /**
     * 小微名称
     */
    @TableField("xwName")
    private String xwName;

    /**
     * 小微主工号
     */
    @TableField("xwMasterCode")
    private String xwMasterCode;

    /**
     * 小微主姓名
     */
    @TableField("xwMasterName")
    private String xwMasterName;

    /**
     * 平台编码
     */
    @TableField("ptCode")
    private String ptCode;

    /**
     * 平台名称
     */
    @TableField("ptName")
    private String ptName;

    /**
     * 领域编码
     */
    @TableField("buCode")
    private String buCode;

    /**
     * 领域名称
     */
    @TableField("buName")
    private String buName;

    /**
     * 链群类型编码  例:1 2 1,2   1：创单  2：体验
     */
    @TableField("xwGroupType")
    private String xwGroupType;

    /**
     * 链群类型名称 例:创单 体验 创单,体验
     */
    @TableField("xwGroupName")
    private String xwGroupName;

    /**
     * 小微行业
     */
    @TableField("xwStyle")
    private String xwStyle;

    /**
     * 小微行业编码
     */
    @TableField("xwStyleCode")
    private String xwStyleCode;

    /**
     * 小微类型(1用户2设计3制造)
     */
    @TableField("xwType")
    private String xwType;

    /**
     * 小微类型(1用户2设计3制造)编码
     */
    @TableField("xwTypeCode")
    private String xwTypeCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
