package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * (SysNodeEhr)实体类
 *
 * @author makejava
 * @since 2019-12-18 14:24:06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysNodeEhr implements Serializable {
    private static final long serialVersionUID = -75561385317989560L;
    @TableId(value = "ID", type = IdType.AUTO)
    //主键
    private Integer id;
    //小微编码
    private String xwcode;
    //小微名称
    private String xwname;
    //节点编码
    private String nodecode;
    //节点名称
    private String nodename;
    //节点直线工号
    private String nodemanagercode;
    //节点直线姓名
    private String nodemanagername;
    //节点直线岗位编码
    private String nodemanagerpos;
    //数据类型（01：ODS自动更新数据；02：自定义数据）
    private String dataType;

}