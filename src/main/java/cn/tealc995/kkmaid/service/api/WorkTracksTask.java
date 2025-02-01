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
public class WorkTracksTask extends Task<ResponseBody<List<Track>>> {
    private String key;

    public WorkTracksTask(String key) {
        this.key = key;
    }

    @Override
    protected ResponseBody<List<Track>> call() throws Exception {
        return KKApi.getInstance().trackApi().track(key);
    }
}