package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("z_target_basic")
public class TargetBasic extends Model<TargetBasic> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 目标编码
     */
    private String targetCode;

    /**
     * 目标名称
     */
    private String targetName;

    /**
     * 目标描述
     */
    private String targetDes;

    /**
     * 目标单位
     */
    private String targetUnit;

    /**
     * 目标方向（1：正向；0：负向）
     */
    private String targetTo;

    /**
     * 举单标识（0或空：非举单目标；1：举单目标）
     */
    private String judanFlag;

    /**
     * 平台编码
     */
    private String targetPtCode;

    /**
     * 目标所属预实差类型(01：按照抢单目标平均合约所属月度的自然天数，并结合实际达成数据进行计算；02：始终按照抢单目标进行计算)
     */
    private String targetDiffType;

    /**
     * 目标维度范围:01|02|03
     */
    private String targetDim;

    /**
     * 目标流程类型01:爆款02“新需求
     */
    private String targetProcessType;

    /**
     * 目标承接小微类型Code
     */
    private String targetXwCategoryCode;

    /**
     * 小微名称
     */
    private String targetXwCategoryName;

    /**
     * 目标实际数据源
     */
    @TableField("target_DSLogic_code")
    private String targetDslogicCode;

    /**
     * 目标底线年度
     */
    private String targeYear;

    /**
     * 目标底线月度
     */
    private String targetMonth;

    /**
     * 目标底线型号
     */
    private String targetModel;

    /**
     * 目标底线型号
     */
    private String targetModelCode;

    /**
     * 目标底线
     */
    private String targetBottomLine;

    /**
     * 目标举单线
     */
    private String targetJdLine;

    /**
     * 目标完成度核算逻辑
     */
    private String targetLogic;

    /**
     * 权重
     */
    private String targetWeight;

    /**
     * 区域编码
     */
    private String targetRegionCode;

    /**
     * 区域名称
     */
    private String targetRegionName;

    /**
     * 目标对应的角色编码，小微/节点/人
     */
    private String roleCode;

    /**
     * 目标对应的角色名称，小微/节点/人
     */
    private String roleName;

    /**
     * 时间
     */
    private String periodCode;

    /**
     * 链群编码
     */
    private String chainCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
