package cn.tealc995.kkmaid.service;

import cn.tealc995.api.PlayListApi;
import cn.tealc995.api.model.playList.PlayListRemoveWork;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-05 09:33
 */
public class PlayListRemoveWorkService extends Service<Boolean> {
    private String url;
    private PlayListRemoveWork work;





    @Override
    protected Task<Boolean> createTask() {

        Task<Boolean> task=new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                updateValue(false);
                if (work.getRemove()){
                    return PlayListApi.remove(url, work);
                }else {
                    return PlayListApi.add(url, work);
                }



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

    public void setWork(PlayListRemoveWork work) {
        this.work = work;
    }

    public PlayListRemoveWork getWork() {
        return work;
    }
}