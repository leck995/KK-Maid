package cn.tealc995.kkmaid.service.api;

import cn.tealc995.kikoreu.KKApi;
import cn.tealc995.kikoreu.api.CategoryApi;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.RoleEx;
import cn.tealc995.kkmaid.ui.CategoryType;
import javafx.concurrent.Task;

import java.util.*;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-15 23:16
 */
public class CategoryTask extends Task<ResponseBody<List<RoleEx>>> {
    private CategoryType type;

    public CategoryTask(CategoryType type) {
        this.type = type;
    }

    @Override
    protected ResponseBody<List<RoleEx>> call() throws Exception {
        updateMessage("true");
        ResponseBody<List<RoleEx>> body = null;
        if (type == CategoryType.CIRCLE){
            body= KKApi.getInstance().categoryApi().circle();
            updateTitle("All CIRCLES");
        } else if (type == CategoryType.TAG) {
            body=KKApi.getInstance().categoryApi().tag();
            updateTitle("All TAGS");
        }else if (type == CategoryType.VA){
            body=KKApi.getInstance().categoryApi().va();
            updateTitle("All VAS");
        }
        if (body.isSuccess() && body.getData() != null){
            Collections.sort(body.getData() , (o1, o2) -> o2.getCount().compareTo(o1.getCount()));
        }
        updateMessage("false");
        return body;
    }






}