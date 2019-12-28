package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * @since 2019-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TOdsMinbuEmp extends Model<TOdsMinbuEmp> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 最小作战单元编码（小小微编码）
     */
    @TableField("littleXwCode")
    private String littleXwCode;

    /**
     * 最小作战单元名称（小小微名称）
     */
    @TableField("littleXwName")
    private String littleXwName;

    /**
     * 所属最小作战单元工号
     */
    @TableField("littleEmpsn")
    private String littleEmpsn;

    /**
     * 所属最小作战单元姓名
     */
    @TableField("littleEmpname")
    private String littleEmpname;

    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
