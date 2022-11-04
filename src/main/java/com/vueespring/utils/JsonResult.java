package com.vueespring.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonResult {
    public final static Integer SUCCESS = 200;
    public final static Integer FAIL = 400;
    public final static Integer ERROR = 500;
    public final static Integer DENIED = 401;
    public Integer code;
    public Object data;
    public String message;
    public JsonResult ok(Object data){
        return new JsonResult(200,data,null);
    }
    public JsonResult ok(Object data,String msg){
        return new JsonResult(200,data,msg);
    }
    public JsonResult error(String msg){
        return new JsonResult(500,null,msg);
    }


    public JsonResult denied(String msg){
        return new JsonResult(401,null,msg);
    }


}
