package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
public class ZGamblingContracts extends Model<ZGamblingContracts> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 合约名称
     */
    private String contractsName;

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
    private String joinTime;

    /**
     * 开始时间
     */
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    private LocalDateTime endDate;

    private String xiaoweiCode;

    private String xiaoweiName;

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
    private LocalDateTime createDate;

    /**
     * 更新人编码
     */
    private String updateCode;

    /**
     * 更新人姓名
     */
    private String updateName;

    /**
     * 更新时间
     */
    private LocalDateTime updateDate;

    /**
     * 合同状态   0：已过期(不可抢入） 1：有效  2：作废（主动）
     */
    private String contractStatus;

    /**
     * 1.爆款产品   2.新需求 3.按照系列举单  4.按型号举单
     */
    private String contractType;

    /**
     * 合同用户群,1冰箱用户，2洗衣机用户
     */
    private String contractGroup;

    /**
     * 组织类型orgType,//1节点  2小微  3创客  4网格
     */
    private String orgType;

    /**
     * 上链关键字
     */
    private String hashCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
