package com.haier.hailian.contract.dto.sms;

import lombok.Data;

/**
 * <p>
 * 	通知
 * </p>
 *
 */
@Data
public class PhoneMessage {

	private Integer id;
    /**
     * 来自
     */
	private String from;
    /**
     * 发送给
     */
	private String desMobile;
	/**
	 * 短信内容
	 */
	private String content;
	/**
	 * 状态 发送是否成功
	 * 默认成功
	 */
	private Boolean status = true;

	/**
	 * 备注
	 */
	private String remark;

	public PhoneMessage(String desMobile, String content) {
		this.desMobile = desMobile;
		this.content = content;
	}
}
