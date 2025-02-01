package cn.tealc995.kkmaid.service.api;

import cn.tealc995.kikoreu.KKApi;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.Track;
import javafx.concurrent.Task;

import java.util.List;

/**
 * @program: KK-Maid
 * @description:
 * @author: Leck
 * @create: 2025-02-01 23:30
 */
public class StarWorkAddTask extends Task<ResponseBody<Boolean>> {
    private String workId;
    private Integer rating;

    public StarWorkAddTask(String workId, Integer rating) {
        this.workId = workId;
        this.rating = rating;
    }

    @Override
    protected ResponseBody<Boolean> call() throws Exception {
        return KKApi.getInstance().starApi().updateStar(workId,rating);
    }
}