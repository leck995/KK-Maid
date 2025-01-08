package cn.tealc995.asmronline.service;

import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.api.CategoryApi;
import cn.tealc995.asmronline.api.model.MainWorks;
import cn.tealc995.asmronline.api.model.RoleEx;
import cn.tealc995.asmronline.ui.CategoryType;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.*;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-15 23:16
 */
public class CategoryService extends Service<List<RoleEx>> {
    private CategoryType type;
    private String host;
    @Override
    protected Task<List<RoleEx>> createTask() {

        Task<List<RoleEx>> task=new Task<List<RoleEx>>() {
            @Override
            protected List<RoleEx> call() throws Exception {
                updateMessage("true");
                List<RoleEx> list = null;
                if (type == CategoryType.CIRCLE){
                    list=CategoryApi.circle(host);
                    updateTitle("All CIRCLES");
                } else if (type == CategoryType.TAG) {
                    list=CategoryApi.tag(host);
                    updateTitle("All TAGS");
                }else if (type == CategoryType.VA){
                    list=CategoryApi.va(host);
                    updateTitle("All VAS");
                }
                updateMessage("false");

                Collections.sort(list, new Comparator<RoleEx>() {
                    @Override
                    public int compare(RoleEx o1, RoleEx o2) {
                        return o2.getCount().compareTo(o1.getCount());
                    }
                });
                return list;
            }
        };
        return task;
    }


    public void setHost(String host) {
        this.host = host;
    }


    public void setType(CategoryType type) {
        this.type = type;
    }
}