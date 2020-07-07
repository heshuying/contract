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
 * 节点关联关系表
 * </p>
 *
 * @author 19033715
 * @since 2020-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdJdLink extends Model<CdJdLink> {

    private static final long serialVersionUID=1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 数据日期YYYYMMDD
     */
    private String dataDate;

    /**
     * 链群编码
     */
    private String lqCode;

    /**
     * 源节点编码
     */
    private String sourceJdCode;

    /**
     * 源节点名称
     */
    private String sourceJdName;

    /**
     * 目标节点编码
     */
    private String targetJdCode;

    /**
     * 目标节点名称
     */
    private String targetJdName;

    /**
     * 关联关系
     */
    private String hy;

    /**
     * 有效标记
     */
    private Integer activeFlag;

    /**
     * 添加时间
     */
    private LocalDateTime createdDate;

    /**
     * 添加人
     */
    private String createdBy;

    /**
     * 修改时间
     */
    private LocalDateTime lastUpdDate;

    /**
     * 修改人
     */
    private String lastUpdBy;

    /**
     * 备注
     */
    private String remark;

    /**
     * 合约ID
     */
    private String contractId;

    /**
     * 合约名称
     */
    private String contractName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
