package cn.tealc995.kikoreu.model;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-15 22:49
 */
public class Response {
    private int code;
    private String message;
    private Object content;

    public Response() {
    }

    public Response(int code,String message) {
        this.code = code;
        this.message = message;
    }
    public Response(int code, String message, Object content) {
        this.code = code;
        this.message = message;
        this.content = content;
    }

    public boolean isSuccess(){
        return code == 200;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}