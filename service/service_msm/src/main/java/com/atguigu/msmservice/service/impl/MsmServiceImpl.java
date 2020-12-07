package com.atguigu.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.naming.utils.StringUtils;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.servicebase.handler.GuliException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-11-30 20:24
 * @description:
 */
@Service
public class MsmServiceImpl implements MsmService {


    //根据手机号发送短信
    @Override
    public boolean sendMsmPhone(String phone, Map<String, Object> param) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }

        DefaultProfile profile =
                DefaultProfile.getProfile(
                        "default",
                        "LTAI4GGAx4McMdcTxT38onN9",
                        "z2kL9pfxmOtSCoh6D12Ni2abuhONJi");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();


        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "美年国宾");
        request.putQueryParameter("TemplateCode", "SMS_205121681");
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));


        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001, "发送短信失败");
        }

    }
}
