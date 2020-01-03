package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 19012964
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MonthChainGroupOrder extends Model<MonthChainGroupOrder> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 年
     */
    private Integer year;

    /**
     * 月
     */
    private Integer month;

    /**
     * 产品组编码
     */
    private String productLineCode;

    /**
     * 产品组名称
     */
    private String productLineName;

    /**
     * 品牌编码
     */
    private String brandCdde;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 用户小微编码
     */
    private String xwCode;

    /**
     * 用户小微名称
     */
    private String xwName;

    /**
     * 系列编码
     */
    private String seriesCode;

    /**
     * 系列名称
     */
    private String seriesName;

    /**
     * 型号编码
     */
    private String productCode;

    /**
     * 型号名称
     */
    private String productName;

    /**
     * 中心编码
     */
    private String tradeCode;

    /**
     * 中心名称
     */
    private String tradeName;

    /**
     * 网格小微编码
     */
    private String wgCode;

    /**
     * 网格小微名称
     */
    private String wgName;

    /**
     * 抢单数量
     */
    private Integer orderNum;

    /**
     * 抢单数量金额（万元）
     */
    private String orderAmt;

    private Integer submitEmp;

    private Date submitTime;

    /**
     * 达成目标需协同问题-产品竞争力
     */
    private String add1;

    /**
     * 达成目标需协同问题-价格
     */
    private String add2;

    /**
     * 达成目标需协同问题-供货
     */
    private String add3;

    /**
     * 达成目标需协同问题-质量
     */
    private String add4;

    /**
     * 达成目标需协同问题-服务
     */
    private String add5;

    /**
     * 达成目标需协同问题-营销
     */
    private String add6;

    /**
     * 达成目标需协同问题-其他
     */
    private String add7;

    /**
     * 启用备用字段，A:线上渠道；O：传统渠道；T:自有渠道
     */
    private String add8;

    /**
     * 备用字段，暂不送数
     */
    private String add9;

    /**
     * 备用字段，暂不送数
     */
    private String add10;

    private String isGzd;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
