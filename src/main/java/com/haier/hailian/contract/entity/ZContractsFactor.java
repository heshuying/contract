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
 * @author 19012964
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZContractsFactor extends Model<ZContractsFactor> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 合约ID
     */
    private Integer contractId;

    /**
     * 编码
     */
    private String factorCode;

    /**
     * 名称
     */
    private String factorName;

    /**
     * 值
     */
    private String factorValue;

    /**
     * 类型 1：底线、2：抢单、3：E2E
     */
    private String factorType;

    /**
     * 单位
     */
    private String factorUnit;

    /**
     * 1-正向；0-负向
     */
    private String factorDirecton;

    /**
     * 地区编码
     */
    private String regionCode;

    /**
     * 地区名称
     */
    private String regionName;

    /**
     * 网格编码
     */
    private String meshCode;

    /**
     * 网格名称
     */
    private String meshName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
