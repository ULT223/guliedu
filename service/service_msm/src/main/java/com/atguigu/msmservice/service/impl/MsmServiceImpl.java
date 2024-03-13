package com.atguigu.msmservice.service.impl;


import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.ConstantSmsUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Map;
import com.aliyun.tea.*;
import com.aliyun.dysmsapi20170525.*;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.*;
import com.aliyun.teaopenapi.models.*;
import com.aliyun.teaconsole.*;
import com.aliyun.darabonba.env.*;
import com.aliyun.teautil.*;
import com.aliyun.darabonbatime.*;
import com.aliyun.darabonbastring.*;

@Service
public class MsmServiceImpl implements MsmService {

    //发送短信的方法
    @Override
    public boolean send(Map<String, String> params, String phone){
        if (StringUtils.isEmpty(phone))
            return false;

        Config config = new Config();
        config.accessKeyId = ConstantSmsUtils.ACCESS_KEY_ID;
        config.accessKeySecret = ConstantSmsUtils.ACCESS_KEY_SECRET;
        Client client = null;
        try {
            client = new Client(config);
            String templateParams = JSON.toJSONString(params);
            SendSmsRequest sendReq = new SendSmsRequest()
                    .setPhoneNumbers(phone)
                    .setSignName("谷粒在线教育网站")
                    .setTemplateCode(ConstantSmsUtils.TEMPLATE_CODE)
                    .setTemplateParam(templateParams);
            SendSmsResponse sendResp = client.sendSms(sendReq);
            String code = sendResp.body.code;
            if (!com.aliyun.teautil.Common.equalString(code, "OK")) {
                com.aliyun.teaconsole.Client.log("错误信息: " + sendResp.body.message + "");
                return false;
            }

            String bizId = sendResp.body.bizId;
            // 2. 等待 10 秒后查询结果
            com.aliyun.teautil.Common.sleep(10000);
            // 3.查询结果
            java.util.List<String> phoneNums = com.aliyun.darabonbastring.Client.split(phone, ",", -1);
            for (String phoneNum : phoneNums) {
                QuerySendDetailsRequest queryReq = new QuerySendDetailsRequest()
                        .setPhoneNumber(com.aliyun.teautil.Common.assertAsString(phoneNum))
                        .setBizId(bizId)
                        .setSendDate(com.aliyun.darabonbatime.Client.format("yyyyMMdd"))
                        .setPageSize(10L)
                        .setCurrentPage(1L);
                QuerySendDetailsResponse queryResp = client.querySendDetails(queryReq);
                java.util.List<QuerySendDetailsResponseBody.QuerySendDetailsResponseBodySmsSendDetailDTOsSmsSendDetailDTO> dtos = queryResp.body.smsSendDetailDTOs.smsSendDetailDTO;
                // 打印结果
                for (QuerySendDetailsResponseBody.QuerySendDetailsResponseBodySmsSendDetailDTOsSmsSendDetailDTO dto : dtos) {
                    if (com.aliyun.teautil.Common.equalString("" + dto.sendStatus + "", "3")) {
                        com.aliyun.teaconsole.Client.log("" + dto.phoneNum + " 发送成功，接收时间: " + dto.receiveDate + "");
                    } else if (com.aliyun.teautil.Common.equalString("" + dto.sendStatus + "", "2")) {
                        com.aliyun.teaconsole.Client.log("" + dto.phoneNum + " 发送失败");
                        return false;
                    } else {
                        com.aliyun.teaconsole.Client.log("" + dto.phoneNum + " 正在发送中...");
                    }
                }
            }
            return true;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
