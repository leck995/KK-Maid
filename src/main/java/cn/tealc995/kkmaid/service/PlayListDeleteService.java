package cn.tealc995.kkmaid.service;

import cn.tealc995.kkmaid.api.PlayListApi;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-05 09:33
 */
public class PlayListDeleteService extends Service<Boolean> {
    private String url;
    private String id;





    @Override
    protected Task<Boolean> createTask() {

        Task<Boolean> task=new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return PlayListApi.delete(url, id);
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