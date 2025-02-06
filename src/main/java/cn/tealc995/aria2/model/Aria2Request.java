package cn.tealc995.aria2.model;

import java.util.List;

/**
 * @description:
 * @author: Leck
 * @create: 2023-09-13 01:33
 */
public class Aria2Request {
    private String id;
    private final String jsonrpc="2.0";
    private String method;
    private List<Object> params;

    public Aria2Request(String id, String method, List<Object> params) {
        this.id = id;
        this.method = method;
        this.params = params;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }
}