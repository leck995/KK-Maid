package cn.tealc995.kkmaid.service;

import cn.tealc995.api.PlayListApi;
import cn.tealc995.api.model.playList.MainPlayList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Map;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-15 23:16
 */
public class PlayListWorkExistService extends Service<MainPlayList> {
    private Map<String,String> params;
    private String host;
    @Override
    protected Task<MainPlayList> createTask() {

        Task<MainPlayList> task=new Task<MainPlayList>() {
            @Override
            protected MainPlayList call() throws Exception {
                updateMessage("true");
                MainPlayList mainPlayList = PlayListApi.workExistInPlayList(host, params);
                updateMessage("false");
                return mainPlayList;
            }
        };
        return task;
    }






    public void setHost(String host) {
        this.host = host;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

}