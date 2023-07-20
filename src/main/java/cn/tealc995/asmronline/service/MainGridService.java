package cn.tealc995.asmronline.service;

import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.api.SearchApi;
import cn.tealc995.asmronline.api.StarApi;
import cn.tealc995.asmronline.api.WorksApi;
import cn.tealc995.asmronline.api.model.MainWorks;
import cn.tealc995.asmronline.ui.CategoryType;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Map;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-15 23:16
 */
public class MainGridService extends Service<MainWorks> {
    private Map<String,String> params;
    private String searchKey;
    private CategoryType type;
    private String host;
    @Override
    protected Task<MainWorks> createTask() {

        Task<MainWorks> task=new Task<MainWorks>() {
            @Override
            protected MainWorks call() throws Exception {
                updateMessage("true");
                MainWorks works;
                if (type==CategoryType.ALL){
                    works = WorksApi.works(host, params);
                }else if (type==CategoryType.STAR){
                    works= StarApi.star(host,params);
                } else {
                    works = SearchApi.search(host,String.format(type.getFormat(),searchKey), params);
                }
                updateMessage("false");
                return works;
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

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }
}