package cn.tealc995.kkmaid.service.api.login;

import cn.tealc995.kikoreu.KKApi;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.User;
import javafx.concurrent.Task;

/**
 * @description: 启动程序时，检测是否成功登录，
 *               注意：网络错误也会返回false
 * @author: Leck
 * @create: 2023-07-19 10:09
 */
public class CheckLoginTask extends Task<ResponseBody<User>> {
    @Override
    protected ResponseBody<User> call() throws Exception {
        ResponseBody<User> userResponseBody = KKApi.getInstance().userApi().checkLogin();
        return userResponseBody;
    }
}