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
 * @author 19033715
 * @since 2020-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TSubClass extends Model<TSubClass> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 子项目编码
     */
    private String subClassCode;

    /**
     * 子项目名称
     */
    private String subClassNam;

    /**
     * 子项目英文名称
     */
    private String subClassEnglish;

    /**
     * 项目大类编码
     */
    private String majorClassCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
