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
 * @author 19033715
 * @since 2020-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TOdsDictionary extends Model<TOdsDictionary> {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("Type")
    private String Type;

    @TableField("Key")
    private String Key;

    @TableField("Value")
    private String Value;

    @TableField("Status")
    private String Status;

    @TableField("updateTime")
    private LocalDateTime updateTime;

    @TableField("Remark")
    private String Remark;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
