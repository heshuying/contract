package com.haier.hailian.contract.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.haier.hailian.contract.entity.ZReservePlanDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
public class ZReservePlanDto extends Model<ZReservePlanDto> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 抢入主表关联id
     */
    private Integer parentId;

    /**
     * 单属性
     */
    private String orderType;

    /**
     * 标题
     */
    private String title;


    private List<ZReservePlanDetail> details;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
