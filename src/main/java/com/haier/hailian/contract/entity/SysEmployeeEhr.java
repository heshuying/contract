package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

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
public class SysEmployeeEhr extends Model<SysEmployeeEhr> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工工号
     */
    @TableField("empSn")
    private String empSn;

    /**
     * 员工姓名
     */
    @TableField("empName")
    private String empName;

    /**
     * 岗位编码
     */
    @TableField("posCode")
    private String posCode;

    /**
     * 岗位名称
     */
    @TableField("posName")
    private String posName;

    /**
     * 员工所在部门编码
     */
    @TableField("deptCode")
    private String deptCode;

    /**
     * 员工所在部门名称
     */
    @TableField("deptName")
    private String deptName;

    @TableField("nodeCode")
    private String nodeCode;

    /**
     * 小微节点名称
     */
    @TableField("nodeName")
    private String nodeName;

    /**
     * 电话号码
     */
    @TableField("mobileNo")
    private String mobileNo;

    /**
     * 邮箱
     */
    private String notesmail;

    @TableField(exist=false)
    private List<SysNet> wanggeList;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
