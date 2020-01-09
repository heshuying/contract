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
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZContractsProduct extends Model<ZContractsProduct> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 举单合约ID
     */
    private Integer contractId;

    /**
     * 产品系列
     */
    private String productSeries;

    /**
     * 年度数量
     */
    private Integer qtyYear;

    /**
     * 月度数量
     */
    private Integer qtyMonth;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
