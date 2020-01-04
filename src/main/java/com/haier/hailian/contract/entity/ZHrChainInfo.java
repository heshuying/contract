package com.haier.hailian.contract.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * (ZHrChainInfo)实体类
 *
 * @author makejava
 * @since 2019-12-19 17:33:19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZHrChainInfo implements Serializable {
    private static final long serialVersionUID = -83285888149021595L;
    
    private Integer id;
    //链群编码
    private String chainCode;
    //链群名称
    private String chainName;
    
    private String xwCode;
    //小微名称
    private String xwName;
    
    private String masterCode;
    //链群主姓名
    private String masterName;
    //链群平台code
    private String chainPtCode;
    //是否删除
    private int deleted;
    //群组ID
    private String groupId;

}