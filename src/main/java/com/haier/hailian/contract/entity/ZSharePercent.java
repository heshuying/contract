package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * @author 19012964
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZSharePercent extends Model<ZSharePercent> {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 型号编码
     */
    private String modelCode;

    /**
     * 型号名称
     */
    private String modelName;

    /**
     * 小微编码
     */
    private String xwCode;

    /**
     * 小微名称
     */
    private String xwName;

    /**
     * 链群编码
     */
    private String lqCode;

    /**
     * 链群名称
     */
    private String lqName;

    /**
     * 分享比例
     */
    private String percent;

    /**
     * 年月
     */
    private String periodCode;

    /**
     * 公司-用于区分导数区分
     */
    private String company;

    /**
     * 数据时间
     */
    private Date date;

    /**
     * 地区编码
     */
    private String regionCode;

    /**
     * 组织类型（1：节点；2：小微；3：创客；4：网格）
     */
    private String orgType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
