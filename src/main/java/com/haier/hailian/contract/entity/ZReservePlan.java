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
 * @since 2019-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZReservePlan extends Model<ZReservePlan> {

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

    /**
     * 任务开始时间
     */
    private Date startTime;

    /**
     * 任务结束时间
     */
    private Date endTime;

    /**
     * 提醒类型：0不提醒，1截止提醒，2截止前1小时，3截止前1天
     */
    private Integer remindType;

    /**
     * 提醒时间，0不提醒，1每工作日，2每日，3每周，4每两周，5每月
     */
    private Integer remindTime;

    /**
     * 是否重要：0/1 - 是/否
     */
    private Integer isImportant;

    /**
     * 执行人（多选时，逗号分隔）
     */
    private String executer;

    /**
     * 抄送人（多选时，号分割）
     */
    private String teamworker;

    /**
     * 发送人（多选时，号分割）
     */
    private String senduser;

    /**
     * 创建人编码
     */
    private String createUserCode;

    /**
     * 创建人名字
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private Date createUserTime;

    /**
     * 任务编码
     */
    private String taskCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
