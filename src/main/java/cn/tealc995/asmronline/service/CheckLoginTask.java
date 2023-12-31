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