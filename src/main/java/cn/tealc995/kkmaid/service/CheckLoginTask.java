package cn.tealc995.kkmaid.service;

import cn.tealc995.api.UserApi;
import cn.tealc995.api.model.User;
import javafx.concurrent.Task;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-19 10:09
 */
public class CheckLoginTask extends Task<Boolean> {
    private String url;

    public CheckLoginTask(String url) {
        this.url = url;
    }

    @Override
    protected Boolean call() throws Exception {
        User user = UserApi.checkLogin(url);
        if (user != null)
            return user.isLoggedIn();
        else
            return false;
    }
}