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
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZReservePlanDetail extends Model<ZReservePlanDetail> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联预案表id
     */
    private Integer parentId;

    /**
     * 内容描述/方案
     */
    private String content;

    /**
     * 成本
     */
    private String cost;

    /**
     * 期限
     */
    private String term;

    /**
     * 负责人
     */
    private String liable;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
