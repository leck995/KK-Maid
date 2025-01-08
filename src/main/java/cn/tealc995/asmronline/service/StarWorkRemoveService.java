package cn.tealc995.asmronline.service;

import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.api.PlayListApi;
import cn.tealc995.asmronline.api.StarApi;
import cn.tealc995.asmronline.api.model.playList.PlayListCreate;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-05 09:33
 */
public class StarWorkRemoveService extends Service<Boolean> {
    private String url;
    private String id;





    @Override
    protected Task<Boolean> createTask() {

        Task<Boolean> task=new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return StarApi.deleteStar(Config.HOST.get(), id);
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}