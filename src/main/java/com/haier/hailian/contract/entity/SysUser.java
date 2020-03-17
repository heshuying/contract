package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 19012964
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String userphone;

    private String chainName;

    /**
     * 简称
     */
    private String chainShortName;

    /**
     * 作为发布方时的名称
     */
    private String chainRole;

    /**
     * 区域编码
     */
    private String locationCode;

    /**
     * 区域-名称
     */
    private String locationName;

    /**
     * 小微类型 空：普通小微 1：市场小微 2：工贸小微
     */
    private String xiaoweiCode;

    /**
     * 小微-名称
     */
    private String xiaoweiName;

    private String password;

    private Integer age;

    /**
     * 1
     */
    private Integer status;

    /**
     * 操作人账号
     */
    private String optCode;

    /**
     * 操作人姓名
     */
    private String optName;

    /**
     * 操作时间
     */
    private Date optDate;

    /**
     * 作为甲方表达式
     */
    private String jiaExpression;

    /**
     * 作为乙方表达式
     */
    private String yiExpression;

    private Date createTime;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
