package com.haier.hailian.contract.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//import javax.validation.constraints.NotBlank;

/**
 * <p>
 *  当前用户
 * </p>
 *
 * @author sunjian 01505617
 * @since 2019/6/28
 */
@Data
public class CurrentUser {
    /**
     * 员工工号
     */

//    @NotBlank(message = "员工工号！")
    @ApiModelProperty(value = "员工工号", name = "empsn", required = true)
    private String empsn;
    /**
     * 员工姓名
     */
//    @NotBlank(message = "员工姓名！")
    @ApiModelProperty(value = "员工姓名", name = "empname", required = true)
    private String empname;
    /**
     * 平台编码
     */
//    @NotBlank(message = "平台编码")
    @ApiModelProperty(value = "平台编码", name = "ptcode", required = true)
    private String ptcode;
    /**
     * 平台名称
     */
//    @NotBlank(message = "平台名称")
    @ApiModelProperty(value = "平台名称", name = "ptname", required = true)
    private String ptname;
    /**
     * 部门类型（节点还是小微）
     */
//    @NotBlank(message = "部门类型（节点还是小微）")
    @ApiModelProperty(value = "部门类型（节点还是小微）", name = "orgType", required = true)
    private String orgType;//1节点  2小微
    /**
     * 节点/小微 编号
     */
//    @NotBlank(message = "节点/小微 编号")
    @ApiModelProperty(value = "节点/小微 编号", name = "orgNum", required = true)
    private String orgNum;
    /**
     * 节点/小微 编号
     */
//    @NotBlank(message = "节点/小微 名称")
    @ApiModelProperty(value = "节点/小微 名称", name = "orgNum", required = true)
    private String orgName;
    /**
     *  小微编码
     */
//    @NotBlank(message = "小微编码！")
    @ApiModelProperty(value = "小微编码！", name = "xwCode", required = true)
    private String xwCode;
    /**
     * 小微名称
     */
//    @NotBlank(message = "小微名称！")
    @ApiModelProperty(value = "小微名称！", name = "xwName", required = true)
    private String xwName;
}
