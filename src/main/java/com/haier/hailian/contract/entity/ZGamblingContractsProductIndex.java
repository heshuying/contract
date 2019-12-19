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
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZGamblingContractsProductIndex extends Model<ZGamblingContractsProductIndex> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 合约id
     */
    private Integer contractsId;

    /**
     * 指标名称
     */
    private String indexName;

    /**
     * 举单目标值
     */
    private String indexTarget;

    /**
     * 指标编码
     */
    private String targetCode;

    /**
     * 指标类型
     */
    private String targetType;

    /**
     * 指标单位
     */
    private String targetUnit;

    /**
     * target_baic的target_to字段
     */
    private String targetTo;

    /**
     * 目标底线
     */
    private String targetBottomLine;

    /**
     * 目标年月
     */
    private String periodCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
