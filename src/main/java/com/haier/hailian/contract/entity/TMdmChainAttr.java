package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2020-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TMdmChainAttr extends Model<TMdmChainAttr> {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 值类型
     */
    private String valueSetId;

    /**
     * 值
     */
    private String value;

    /**
     * 值含义
     */
    private String valueMeaning;

    /**
     * 英文名
     */
    private String valueMeaningEn;

    /**
     * 项目大类编码
     */
    private String parentValueLow;

    /**
     * 更新时间
     */
    private Date lastUpd;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
