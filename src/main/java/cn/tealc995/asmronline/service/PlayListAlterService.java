package cn.tealc995.asmronline.service;

import cn.tealc995.asmronline.api.PlayListApi;
import cn.tealc995.asmronline.api.model.playList.PlayListAlter;
import cn.tealc995.asmronline.api.model.playList.PlayListCreate;
import cn.tealc995.asmronline.api.model.playList.PlayListRemoveWork;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-05 09:33
 */
public class PlayListAlterService extends Service<Boolean> {
    private String url;
    private PlayListAlter playList;
    private PlayListRemoveWork works;





    @Override
    protected Task<Boolean> createTask() {

        Task<Boolean> task=new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                if (works != null){
                    PlayListApi.add(url,works);
                }

                return PlayListApi.alter(url, playList);
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

    public void setPlayList(PlayListAlter playList) {
        this.playList = playList;
    }

    public void setWorks(PlayListRemoveWork works) {
        this.works = works;
    }
}