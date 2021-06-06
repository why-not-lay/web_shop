package com.web.web_shop.beans;
import com.web.web_shop.Tool.Constant;

/**
 * APIResult
 */
public class APIResult {

    private int code;
    private String message;
    private Object data;

    public static APIResult createOK(Object data) {
        return createWithCodeAndData(Constant.Code.OK,null,data);
    }

    public static APIResult createOKMessage(String message) {
        return createWithCodeAndData(Constant.Code.OK,message,null);
    }

    public static APIResult createNG(String message) {
        return createWithCodeAndData(Constant.Code.NG,message,null);
    }

    private static APIResult createWithCodeAndData(int code, String message, Object data) {
        APIResult result = new APIResult();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public Object getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
