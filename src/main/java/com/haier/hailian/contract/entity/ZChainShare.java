package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2020-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZChainShare extends Model<ZChainShare> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 链群编码
     */
    private String chainCode;

    /**
     * 增值空间计算公式
     */
    private String shareExpression;

    private Double sharePercent1;

    private Double sharePercent2;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
