package cn.tealc995.kkmaid.service.api;

import cn.tealc995.kikoreu.KKApi;
import cn.tealc995.kikoreu.api.PlayListApi;
import cn.tealc995.kikoreu.model.ResponseBody;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * @description:
 * @author: Leck
 * @create: 2023-08-05 09:33
 */
public class PlayListDeleteTask extends Task<ResponseBody<Boolean>> {
    private final String workId;

    public PlayListDeleteTask(String workId) {
        this.workId = workId;
    }

    @Override
    protected ResponseBody<Boolean> call() throws Exception {
        return KKApi.getInstance().playListApi().delete(workId);
    }
}