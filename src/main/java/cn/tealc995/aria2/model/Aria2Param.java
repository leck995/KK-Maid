package cn.tealc995.aria2.model;


/**
 * @description:
 * @author: Leck
 * @create: 2023-09-13 01:27
 */
public class Aria2Param {
    private String token;
    private String[] urls;
    private Aria2Option option;

    public Aria2Param(String token, String[] urls, Aria2Option option) {
        this.token = token;
        this.urls = urls;
        this.option = option;
    }

    public Aria2Param(String token, String url, Aria2Option option) {
        this.token = token;
        this.urls = new String[]{url};
        this.option = option;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public Aria2Option getOption() {
        return option;
    }

    public void setOption(Aria2Option option) {
        this.option = option;
    }
}