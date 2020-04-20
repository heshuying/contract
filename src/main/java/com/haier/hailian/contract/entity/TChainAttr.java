package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
public class TChainAttr extends Model<TChainAttr> {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 链群属性编码
     */
    private String chainAttrCode;

    /**
     * 链群属性名称
     */
    private String chainAttrName;

    /**
     * 链群属性英文名称
     */
    private String chainAttrEnglish;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
