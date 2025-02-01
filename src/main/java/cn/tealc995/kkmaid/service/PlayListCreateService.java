package cn.tealc995.kkmaid.service;

import cn.tealc995.api.PlayListApi;
import cn.tealc995.api.model.playList.PlayListCreate;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-05 09:33
 */
public class PlayListCreateService extends Service<Boolean> {
    private String url;
    private PlayListCreate playList;





    @Override
    protected Task<Boolean> createTask() {

        Task<Boolean> task=new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return PlayListApi.create(url, playList);
            }
        };
        return task;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PlayListCreate getPlayList() {
        return playList;
    }

    public void setPlayList(PlayListCreate playList) {
        this.playList = playList;
    }
}