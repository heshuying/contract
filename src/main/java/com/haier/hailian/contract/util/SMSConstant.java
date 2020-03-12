package com.haier.hailian.contract.util;

/**
 * <p>
 *  发送短信模版
 * </p>
 */
public class SMSConstant {

    public enum SmsBizType {
        Valid_Reg,
        Valid_Login,
        Valid_ForgetPwd;
    }

    public static  int SMS_Random_Length=6;

    /**
     * 通知下游公司短信模版
     */
    public static String SMS_NOTIFICATION_2PART_TEMPLATE_CONTENT = "您有新的目标需要认领，请登录人单合一App-我的-待抢入进行目标认领!";
    /**
     * 验证码登陆短信模版
     */
    public static String SMS_CODE_TEMPLATE = "您正在使用短信验证码登陆增值分享平台,短信验证码是:{}";
    /**
     * 更换绑定手机号短信模版
     */
    public static String SMS_CHANGE_PHONE_CODE_TEMPLATE = "您正在使用短信验证码绑定手机号,短信验证码是:{}";

  /**
   * 合约平台短信提醒模板
   */
  public static String SMS_CONTRACT_PLATFORM_TIPS = "【海尔】智能合约平台提醒您，{}正在提醒您抢入链群目标（抢入截止时间：{} 23:59:59），请尽快登陆智能合约平台（{}）进行操作。";

}
