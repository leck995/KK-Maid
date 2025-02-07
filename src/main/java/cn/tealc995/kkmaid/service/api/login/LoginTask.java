package cn.tealc995.kkmaid.service.api.login;

import cn.tealc995.kikoreu.KKApi;
import cn.tealc995.kikoreu.model.ResponseBody;
import javafx.concurrent.Task;

/**
 * @description: 用于在设置中登陆获取Token
 * @author: Leck
 * @create: 2023-07-19 10:09
 */
public class LoginTask extends Task<ResponseBody<String>> {
    private String username;
    private String password;

    public LoginTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected ResponseBody<String> call() throws Exception {
        ResponseBody<String> responseBody = KKApi.getInstance().userApi().login(username, password);
        //这里没做错误处理
        return responseBody;
    }
}