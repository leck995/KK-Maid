package cn.tealc995.kkmaid.ui;

import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.User;
import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainNotificationEvent;
import cn.tealc995.kkmaid.service.api.login.CheckLoginTask;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-12 21:04
 */
public class MainViewModel {
    public MainViewModel() {

    }



    public void checkLogin() {
        if (Config.setting.getHOST() != null && !Config.setting.getHOST().isEmpty() && Config.setting.getTOKEN() != null && !Config.setting.getTOKEN().isEmpty()) {
            CheckLoginTask checkLoginTask = new CheckLoginTask();
            checkLoginTask.setOnSucceeded(workerStateEvent -> {
                ResponseBody<User> responseBody = checkLoginTask.getValue();
                if (responseBody.isSuccess()) {
                    if (responseBody.getData().isLoggedIn()) {
                        EventBusUtil.getDefault().post(new MainNotificationEvent("登陆成功"));
                    } else {
                        EventBusUtil.getDefault().post(new MainNotificationEvent("登陆失效，请重新登录获取Token"));
                    }
                } else {
                    EventBusUtil.getDefault().post(new MainNotificationEvent("登陆失败,原因：" + responseBody.getMsg()));
                }
            });
            Thread.startVirtualThread(checkLoginTask);
        } else {
            EventBusUtil.getDefault().post(new MainNotificationEvent("当前未登录，请登录后使用"));
        }
    }

}