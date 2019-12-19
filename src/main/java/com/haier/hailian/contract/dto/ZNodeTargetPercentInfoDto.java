package com.haier.hailian.contract.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * (ZNodeTargetPercentInfo)实体类
 *
 * @author makejava
 * @since 2019-12-19 11:27:24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZNodeTargetPercentInfoDto implements Serializable {
    private static final long serialVersionUID = 314181001214392364L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //链群ID
    private Integer lqId;
    //链群编码
    private  String lqCode;
    //链群名称
    private  String lqName;
    //节点编码
    private String nodecode;
    //节点名称
    private String nodename;
    //员工编码
    private String empcode;
    //员工姓名
    private String empname;
    //小微编码
    private String xwcode;
    //小微名称
    private String xwname;
    //目标编码
    private String targetcode;
    //目标名称
    private String targetname;
    //目标线
    private String targetline;
    //分享比例
    private String sharepercent;
}