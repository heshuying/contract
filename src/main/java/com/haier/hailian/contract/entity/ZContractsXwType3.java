package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * @author 01431594
 * @since 2020-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZContractsXwType3 extends Model<ZContractsXwType3> {

    private static final long serialVersionUID=1L;

    private Integer id;

    private Integer contractId;

    /**
     * 资源类型编码
     */
    @TableField("xwType3Code")
    private String xwType3Code;

    /**
     * 资源类型
     */
    @TableField("xwType3")
    private String xwType3;

    /**
     * 最小作战单元类型
     */
    private Integer nodeNumber;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
