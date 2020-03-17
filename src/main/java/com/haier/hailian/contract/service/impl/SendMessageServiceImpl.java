package com.haier.hailian.contract.service.impl;

import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.config.SmsConfig;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.dto.sms.NotifyMessage;
import com.haier.hailian.contract.dto.sms.NotifySms;
import com.haier.hailian.contract.dto.sms.PhoneMessage;
import com.haier.hailian.contract.dto.sms.SendMsgDto;
import com.haier.hailian.contract.entity.SysMsg;
import com.haier.hailian.contract.entity.SysMsgTemplate;
import com.haier.hailian.contract.service.SendMessageService;
import com.haier.hailian.contract.service.SysMsgService;
import com.haier.hailian.contract.service.SysMsgTemplateService;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.DESUtils;
import com.haier.hailian.contract.util.RandomValidateCodeUtil;
import com.haier.hailian.contract.util.SMSConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *   发送短信service
 * </p>
 *
 * @author sunjian 01505617
 * @since 2019/5/16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SendMessageServiceImpl implements SendMessageService {

    @Autowired
    private SmsConfig smsConf;
    @Autowired
    private SysMsgService sysMsgService;
    @Autowired
    private SysMsgTemplateService msgTemplateService;
    @Autowired
    private RestTemplate nRestTemplate;

    /**
     * 发送单个短信
     * @param dto
     * @return
     */
    @Override
    public R sendValidCode(SendMsgDto dto) {
        //获取验证码
        String validCode= RandomValidateCodeUtil.buildNumberRandomCode(SMSConstant.SMS_Random_Length);
        //解析短信模板
        SysMsgTemplate template=msgTemplateService.getOne(new QueryWrapper<SysMsgTemplate>()
            .eq("template_code",dto.getBizType()));
        if(template==null|| StringUtils.isBlank(template.getContent())){
            throw new RException("短信业务"+Constant.MSG_DATA_NOTFOUND,Constant.CODE_DATA_NOTFOUND);
        }
        String msgContent=arrayFormat(template.getContent(),validCode);
        //记录短信表
        SysMsg msg=new SysMsg();
        msg.setCellphone(dto.getCellphone());
        msg.setContent(msgContent);
        msg.setTemplate(dto.getBizType());
        msg.setStatus("0");
        msg.setValidCode(validCode);
        msg.setCreateTime(new Date());
        sysMsgService.save(msg);
        //发送短信
        Boolean sendResult=sendSMS(Arrays.asList(msg));
        msg.setStatus(sendResult?"1":"2");
        sysMsgService.updateById(msg);
        if (smsConf.getFlag()==false) {
            return R.ok().put("validcode",validCode);
        }else {
            return R.ok();
        }
    }

    @Override
    public boolean validSmsCode(SendMsgDto dto) {
        //查询验证码
        List<SysMsg> validCodes=sysMsgService.list(new QueryWrapper<SysMsg>()
                .eq("cellphone",dto.getCellphone())
                .eq("template", dto.getBizType())
                .orderByDesc("id")
                .last("limit 1"));
        if(validCodes==null||validCodes.size()==0){
            throw new RException("验证码校验失败", Constant.CODE_DATA_NOTFOUND);
        }
        long peroid=(new Date().getTime()-validCodes.get(0).getCreateTime().getTime())/(1000*60);
        if(peroid>SMSConstant.SMS_Invalid_Time){
            throw new RException("验证码校验失败", Constant.CODE_DATA_NOTFOUND);
        }
        String validcode=validCodes.get(0).getValidCode();
        if(!dto.getValidCode().equals(validcode)){
            throw new RException("验证码校验失败", Constant.CODE_DATA_NOTFOUND);
        }
        return true;
    }

    /**
     * 批量发送短信
     * @param list
     */
    @Override
    public Boolean sendSMS(List<SysMsg> list) {
        if (smsConf.getFlag()==false) {
            log.warn("mock环境，当前环境不发送短信");
            return  true;
        }
        NotifySms notifySms = new NotifySms();
        BeanUtils.copyProperties(smsConf,notifySms);
        notifySms.setToken(DESUtils.encryptToken(smsConf.getSysCode(), smsConf.getTokenKey()));
        List<NotifyMessage> messages = new ArrayList<>();
        for (SysMsg msg : list) {
            /**
             *  TODO 手机号发送频次控制
             *  TODO  IP控制
             *  通过才能发送短信
             */

            if (true) {
                NotifyMessage notifyMessage = new NotifyMessage();
                notifyMessage.setDesMobile(msg.getCellphone());
                notifyMessage.setContent(msg.getContent());
                messages.add(notifyMessage);
            }
        }
        if (messages != null && messages.size() > 0) {
            notifySms.setMessages(messages);
            HttpEntity<NotifySms> entity = new HttpEntity<NotifySms>(notifySms);
            ResponseEntity<String> responseEntity = nRestTemplate.postForEntity(smsConf.getSmsServer()
                            ,entity, String.class);

            String value = responseEntity.getBody();
            //String notifySmsString = JSON.toJSONString(notifySms);
            //JSONObject jsonObject = HttpClientUtils.doPostForJson(smsConf.getSmsServer(), notifySmsString);
            log.info("发送短信结果,{}", value);
            //批量保存结果
            return true;
        }
        return false;
    }

    @Override
    public Boolean sendSubUser() {
//        String content = "您有新的目标需要认领，请登录人单合一App-我的-待抢入进行目标认领!";
//        Subject subject = SecurityUtils.getSubject();
//        SysUser sysUser = (SysUser) subject.getPrincipal();
//        //查询上级用户
//        List<SysUser> users = sysUserDao.selectSonUser(sysUser.getUsername());
//        List<PhoneMessage> phoneMessageList = new ArrayList<>();
//        for (SysUser user : users) {
//            if (user.getUserphone() != null) {
//                PhoneMessage phoneMessage = new PhoneMessage(user.getUserphone(), SMSConstant.SMS_NOTIFICATION_2PART_TEMPLATE_CONTENT);
//                phoneMessageList.add(phoneMessage);
//            }
//        }
//        return this.sendSMSS(phoneMessageList);
        return false;

    }

//    @Override
//    public Boolean sendSMSCode(SendSMSCodeDTO sendSMSCodeDTO, HttpServletRequest request) {
//        //生成四位数验证码
//        RandomValidateCodeUtil randomValidateCodeUtil = new RandomValidateCodeUtil();
//        String randomString = randomValidateCodeUtil.getRandomCode(4);
//        //封装一条短信
//        PhoneMessage phoneMessage = new PhoneMessage(sendSMSCodeDTO.getPhone(),
//                this.arrayFormat(SMSConstant.SMS_CODE_TEMPLATE,randomString));
//        Boolean sendSucc = this.sendSMS(phoneMessage);
//        if (sendSucc) {
//            request.getSession().setAttribute(sendSMSCodeDTO.getPhone(), randomString);
//            return true;
//        }
//        return false;
//
//    }

    /**
     * 格式化参数  类似log4j.info的用法 log.info("pram1={},pram2={}","1","2")
     * @param messagePattern
     * @param argArray
     * @return
     */
    @Override
    public String arrayFormat(String messagePattern, String... argArray) {
        int i = 0;
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
        for (int L = 0; L < argArray.length; ++L) {
            int j = messagePattern.indexOf("{}", i);
            if (j == -1) {
                if (i == 0) {
                    return messagePattern;
                }
                sbuf.append(messagePattern, i, messagePattern.length());
                return sbuf.toString();
            }
            sbuf.append(messagePattern, i, j);
            sbuf.append(argArray[L]);
            i = j + 2;
        }
        sbuf.append(messagePattern, i, messagePattern.length());
        return sbuf.toString();
    }
}
