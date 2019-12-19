package com.haier.hailian.contract.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
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
 * @author liuyq
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class XXwActual extends Model<XXwActual> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日期
     */
    private String curdate;

    /**
     * 组织代码
     */
    private String entity;

    /**
     * 组织名称
     */
    private String entityName;

    /**
     * 科目代码
     */
    private String account;

    /**
     * 科目名称
     */
    private String accountName;

    /**
     * 数据
     */
    private BigDecimal data;

    /**
     * 情景(s01目标 s03实际)
     */
    private String scenario;

    /**
     * 区域编码
     */
    private String region;

    /**
     * (v03月累计 v01当日  v04年累计 )
     */
    private String version;

    /**
     * 创建时间
     */
    private LocalDate createDate;

    private String noaCreateDate;

    /**
     * 区域名称
     */
    private String regionName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
