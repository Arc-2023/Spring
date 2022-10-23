package com.vueespring.utils;

import cn.hutool.Hutool;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WXUtils {
    private String secret = "iwaFn6TvQYiLrQH1ZPLgT0hEXPkATkjtbs8ZzDW0G3c";
    private Integer appid = 1000002;
    private String corpid = "ww99d6e046c3d3241e";
    private String  accesstoken = "yIYBxZ4Fs_zzHlePZbo2RAZsQPWTpIxd_NGDG4mcs7PTO9PcjhCBrVPL51oqWKepGDcaejW8CFQc3DlEeIrXao8QIEKVovvfxAus1D07KqBfMRGS5ZxknnVBledOQNP5ALiMbEBvmG3IrE4XSLXxJ5L9_y-u8keGEr2I_IYV0KqnGIUMqs0O4A2RbPFAhyjWe50tajY1x8ASn7acf4WI2g";
    public Boolean sendMsg(String meassage){
        JSONObject json = new JSONObject();
        json.append("touser","CuiYiKe").append("msgtype","text").append("agentid",appid)
                        .append("text",meassage).append("safe",0).append("enable_id_trans",0).append("enable_duplicate_check",0);
        HttpRequest post = HttpUtil.createPost("https://qyapi.weixin.qq.com/cgi-bin/message/send" + "?access_token=" + accesstoken)
                .body(JSONUtil.toJsonStr(json));
        HttpResponse res = post.execute(true);
        if(res.getStatus()==200){
            JSONObject resdata = JSONUtil.parseObj(res);
            if(resdata.getInt("errcode")==0){
                return true;
            }else {
                return false;
            }
        }else {return false;}
    }
}
