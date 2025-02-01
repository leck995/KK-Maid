package cn.tealc995.kikoreu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBody<T> {
    private Integer code; //code可以随意设置，但成功必须是200
    private String msg;
    private T data;

    public ResponseBody() {
    }

    public ResponseBody(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseBody(Integer code, String msg, Boolean success) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> ResponseBody<T> create(Integer code, String msg, T t) {
        ResponseBody<T> responseBody = new ResponseBody<>();
        responseBody.setCode(code);
        responseBody.setMsg(msg);
        responseBody.setData(t);
        return responseBody;
    }

    public boolean isSuccess() {
        return code == 200;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}