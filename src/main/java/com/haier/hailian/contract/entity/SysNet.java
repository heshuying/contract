package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysNet extends Model<SysNet> {

    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 中心编码
     */
    private String centerCode;

    /**
     * 中心名称
     */
    private String centerName;

    /**
     * 经营体编码
     */
    private String jytCode;

    /**
     * 经营体名称
     */
    private String jytName;

    /**
     * 工号
     */
    @TableField("empSN")
    private String empSN;

    /**
     * 姓名
     */
    @TableField("empName")
    private String empName;

    /**
     * 网格所属市场小微编码
     */
    private String xwCode;

    /**
     * 网格所属市场小微名称
     */
    private String xwName;

    /**
     * 网格GDP
     */
    private String netGdp;

    /**
     * 网格人群规模
     */
    private String netPopulationCount;

    private String productLineCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
