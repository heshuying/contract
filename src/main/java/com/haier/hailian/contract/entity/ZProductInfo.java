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
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZProductInfo extends Model<ZProductInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品名称
     */
    private String productName;

    /**
     *  产品系列
     */
    private String productSeries;

    /**
     * 产品信号
     */
    private String productModel;

    /**
     * 型号编码
     */
    private String productModelCode;

    /**
     * 产品结构（01：低端；02：中端；03：高端）
     */
    private String productStru;

    /**
     * 产品对应平台编码
     */
    private String ptCode;

    private String company;

    /**
     * 是否是网器产品（01：是；）
     */
    private String isNetDevice;

    /**
     * 是否是生态场景产品（01：是；）
     */
    private String isScene;

    /**
     * 型号状态（01：在销；02：下市）
     */
    private String productStatus;

    /**
     * 年月
     */
    private String periodCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
