package cn.tealc995.kkmaid.service.api.playlist;

import cn.tealc995.kikoreu.KKApi;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.playList.MainPlayList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Map;

/**
 * @description: 获取播放列表的列表
 * @author: Leck
 * @create: 2023-07-15 23:16
 */
public class PlayListService extends Service<MainPlayList> {
    private Map<String, String> params;

    @Override
    protected Task<MainPlayList> createTask() {
        Task<MainPlayList> task = new Task<MainPlayList>() {
            @Override
            protected MainPlayList call() throws Exception {
                updateMessage("true");
                ResponseBody<MainPlayList> body = KKApi.getInstance().playListApi().playList(params);
                updateMessage("false");
                return body.getData();
            }
        };
        return task;
    }


    public void setParams(Map<String, String> params) {
        this.params = params;
    }

}