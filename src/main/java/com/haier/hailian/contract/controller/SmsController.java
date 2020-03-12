package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.sms.PhoneMessage;
import com.haier.hailian.contract.dto.sms.SendMsgDto;
import com.haier.hailian.contract.service.SendMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * ${desc}
 * </p>
 */
@RestController
@RequestMapping(value = {"/sms"})
@Api(value = "短信接口", tags = {"短信接口"})
@Slf4j
public class SmsController {
    @Autowired
    SendMessageService sendMessageService;

    @PostMapping(value = {"/sendSMS"})
    @ApiOperation(value = "发送短信")
    public R sendSMS(@RequestBody SendMsgDto dto) {
        return sendMessageService.sendValidCode(dto);
    }

    @GetMapping(value = {"/send2Second"})
    @ApiOperation(value = "发送通知节点短信")
    public R send2Second() {
//        String content = "您有新的目标需要认领，请登录人单合一App-我的-待抢入进行目标认领!";
//        List<PhoneMessage> phoneMessageList = new ArrayList<>();
//        PhoneMessage phone1 = new PhoneMessage("13335007116", content);
//        PhoneMessage phone2 = new PhoneMessage("18765977822", content);
//        PhoneMessage phone3 = new PhoneMessage("18562625076", content);
//        phoneMessageList.add(phone1);
//        phoneMessageList.add(phone2);
//        phoneMessageList.add(phone3);
//        Boolean aBoolean = sendMessageService.sendSMSS(phoneMessageList);
//        if (aBoolean) {
//            return new ResultDTO<>(MessageEnum.C0000, "短信发送成功");
//        } else {
//
//            return new ResultDTO<>(MessageEnum.C0000, "短信发送失败");
//        }

        Boolean flag = sendMessageService.sendSubUser();
        return R.ok().put("data",flag);
    }

}
