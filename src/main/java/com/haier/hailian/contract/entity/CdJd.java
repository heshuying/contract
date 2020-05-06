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
 * 节点基础表-来源ODS-最小作战单元
 * </p>
 *
 * @author 19033715
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdJd extends Model<CdJd> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String dataDate;

    /**
     * 产业编码
     */
    private String cyCode;

    /**
     * 产业名称
     */
    private String cyName;

    /**
     * 节点编码
     */
    private String jdCode;

    /**
     * 节点名称
     */
    private String jdName;

    /**
     * 节点简称
     */
    private String jdNameEazy;

    /**
     * 节点类型编码
     */
    private String jdTypeCode;

    /**
     * 节点类型
     */
    private String jdType;

    /**
     * 节点X坐标值
     */
    private Integer x;

    /**
     * 节点Y坐标值
     */
    private Integer y;

    /**
     * 有效标记
     */
    private Integer activeFlag;

    private LocalDateTime createDate;

    private String createBy;

    private LocalDateTime updateDate;

    private String updateBy;

    private String bz;

    /**
     * 创单/体验编码
     */
    private String xwTypeCode;

    /**
     * 创单/体验
     */
    private String xwType;

    /**
     * 小微节点
     */
    private String xwCode;

    /**
     * 小微名称
     */
    private String xwName;

    /**
     * 小微负责人编码
     */
    private String xwMasterCode;

    /**
     * 小微负责人名称
     */
    private String xwMasterName;

    /**
     * 节点负责人编码
     */
    private String jdMasterCode;

    /**
     * 节点负责人名称
     */
    private String jdMasterName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
