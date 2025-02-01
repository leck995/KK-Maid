package cn.tealc995.kkmaid.service;

import cn.tealc995.kikoreu.KKApi;
import cn.tealc995.kikoreu.api.UserApi;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.User;
import javafx.concurrent.Task;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-19 10:09
 */
public class CheckLoginTask extends Task<Boolean> {
    @Override
    protected Boolean call() throws Exception {
        ResponseBody<User> userResponseBody = KKApi.getInstance().userApi().checkLogin();
        if (userResponseBody.isSuccess() && userResponseBody.getData() != null)
            return userResponseBody.getData().isLoggedIn();
        else
            return false;
    }
}