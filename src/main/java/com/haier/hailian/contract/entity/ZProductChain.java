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
 * @author 01431594
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZProductChain extends Model<ZProductChain> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 产品系列
     */
    private String productSeries;

    /**
     * 型号名称
     */
    private String modelName;

    /**
     * 型号编码
     */
    private String modelCode;

    /**
     * 链群编码
     */
    private String chainCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
