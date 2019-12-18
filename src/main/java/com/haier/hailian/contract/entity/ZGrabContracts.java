package com.haier.hailian.contract.entity;

import java.math.BigDecimal;
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
 * @author 19033715
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZGrabContracts extends Model<ZGrabContracts> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联合约id
     */
    private Integer parentId;

    /**
     * 关联爆品/需求id
     */
    private Integer pdId;

    /**
     * 系列
     */
    private String series;

    /**
     * 类别（标识是爆品/需求）
     */
    private String type;

    /**
     * 分享比例
     */
    private String shareRatio;

    /**
     * 增值分享酬
     */
    private String valuAddedShare;

    /**
     * 抢单人姓名
     */
    private String grabOrderName;

    /**
     * 抢单人编码
     */
    private String grabOrderCode;

    /**
     * 小微名称
     */
    private String xwName;

    /**
     * 小微code
     */
    private String xwCode;

    /**
     * 抢单时间
     */
    private LocalDateTime grabOrderDate;

    /**
     * 状态，0抢入中，1抢入成功,（已审批），2已驳回，3：被踢出
     */
    private String status;

    /**
     * 市场总体分享比例
     */
    private String overallShareRatio;

    /**
     * 交互意见
     */
    private String opinion;

    /**
     * 抢单量
     */
    private String overallGrabCount;

    /**
     * 抢单收入
     */
    private BigDecimal overallGrabIncome;

    /**
     * 小微类型(用户交互、研发、模块商等)
     */
    private String xwType;

    /**
     * 组织类型//1节点  2小微  3创客  4网格
     */
    private String orgType;

    /**
     * dts分享比例
     */
    private Integer dtsSharePercentage;

    /**
     * 目标优化状态：1.待优化 2 已优化
     */
    private String betterStatus;

    /**
     * 发布动态优化的时间
     */
    private LocalDateTime betterDate;

    /**
     * 实际贡献
     */
    private String contribution;

    /**
     * 是否市场0：是1：不是
     */
    private String isMarket;

    /**
     * 抢单利润
     */
    private String grabProfit;

    private String orgCode;

    private String orgName;

    /**
     * 上链关键字
     */
    private String hashCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
