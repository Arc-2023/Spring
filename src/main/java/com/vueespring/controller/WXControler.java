package com.vueespring.controller;

import com.vueespring.utils.json_callback.com.qq.weixin.mp.aes.AesException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import com.vueespring.utils.json_callback.com.qq.weixin.mp.aes.WXBizJsonMsgCrypt;
import org.json.JSONObject;
@RestController
public class WXControler {
    @GetMapping("/wxdecode")
    public String decodemessage(
            @PathParam("msg_signature") String token,
            @PathParam("timestamp") String timestamp,
            @PathParam("nonce") String nonce,
            @PathParam("echostr") String echostr) throws Exception {
        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt("123456qwe", "123456qwe", "1000002");
        String sEchoStr; //需要返回的明文
        try {
            sEchoStr = wxcpt.VerifyURL(token, timestamp, nonce, echostr);
            return sEchoStr;
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
        }
        return "fasle";
    }
}
