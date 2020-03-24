package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
public class ZContracts extends Model<ZContracts> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联id
     */
    private Integer parentId;

    /**
     * 合约名称
     */
    private String contractName;

    /**
     * 10链群主合约、20商圈合约、30创客合约
     */
    private String contractType;

    /**
     * 状态，0抢入中，1抢入成功,（已审批），2已驳回，3：被踢出,4:已过期
     */
    private String status;

    /**
     * 分享空间
     */
    private BigDecimal shareSpace;

    /**
     * 链群编码
     */
    private String chainCode;

    /**
     * 地区编码
     */
    private String regionCode;

    /**
     * 抢入截止时间
     */
    private Date joinTime;

    @TableField(exist = false)
    private String joinTimeStr;

    //链群主复核分享比例截止时间
    private Date checkTime;

    @TableField(exist = false)
    private String checkTimeStr;

    // 是否已复核
    private String isChecked;

    //  链群分享额
    private BigDecimal shareMoney;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * xw编码
     */
    private String xiaoweiCode;

    /**
     * 创建人编码
     */
    private String createCode;

    /**
     * 创建人姓名
     */
    private String createName;

    /**
     * 创建日期
     */
    private Date createTime;

    private String orgCode;

    private String orgName;

    private String orgType;

    /**
     * 校验闸口开关0：关闭；1：开启
     */
    private String openValid;


    @TableField(exist = false)
    private String startDateStr;
    @TableField(exist = false)
    private String endDateStr;
    @TableField(exist = false)
    private String status2;//0 不可优化 1.可优化
    @TableField(exist = false)
    private String status3;//0 不可撤销 1.可撤销
    @TableField(exist = false)
    private String status4;//0 不可复核 1 可复核
    private String sharePercent;

    private String groupId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
