package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 通知表
 * </p>
 *
 * @author 19012964
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysMsg extends Model<SysMsg> {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发送方ID
     */
    private Long sendId;

    /**
     * 接收ID
     */
    private Long accessId;

    /**
     * 接收手机号
     */
    private String cellphone;

    /**
     * 类型
     */
    private String template;

    /**
     * 内容
     */
    private String content;

    /**
     * 验证内容
     */
    private String validCode;

    /**
     * 状态
     */
    private String status;

    /**
     * 标题
     */
    private String title;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
