package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 19012964
 * @since 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VJdxp extends Model<VJdxp> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 期间
     */
    @TableField("PERIOD_CODE")
    private String periodCode;

    /**
     * 产业编码
     */
    @TableField("LQ_PT_CODE")
    private String lqPtCode;

    /**
     * 产业名称
     */
    @TableField("LQ_PT_NAME")
    private String lqPtName;

    /**
     * 链群编码
     */
    @TableField("LQ_CODE")
    private String lqCode;

    /**
     * 链群名称
     */
    @TableField("LQ_NAME")
    private String lqName;

    /**
     * 节点类型
     */
    @TableField("JD_TYPE")
    private String jdType;

    /**
     * 节点编码
     */
    @TableField("JD_CODE")
    private String jdCode;

    /**
     * 节点名称
     */
    @TableField("JD_NAME")
    private String jdName;

    /**
     * 月累计评价
     */
    @TableField("PJ_M")
    private String pjM;

    /**
     * 年累计评价
     */
    @TableField("PJ_Y")
    private String pjY;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
