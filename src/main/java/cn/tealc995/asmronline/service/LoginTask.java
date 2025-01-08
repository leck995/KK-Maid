package cn.tealc995.asmronline.service;

import cn.tealc995.asmronline.api.UserApi;
import cn.tealc995.asmronline.api.model.User;
import javafx.concurrent.Task;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-19 10:09
 */
public class LoginTask extends Task<String> {
    private String url;
    private String username;
    private String password;

    public LoginTask(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    protected String call() throws Exception {
        String token = UserApi.login(url, username, password);

        return token;
    }
}