package cn.tealc995.kkmaid.service.api.playlist;

import cn.tealc995.kikoreu.KKApi;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.playList.PlayListCreate;
import javafx.concurrent.Task;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-05 09:33
 */
public class PlayListCreateTask extends Task<ResponseBody<Boolean>> {
    private final PlayListCreate playList;

    public PlayListCreateTask(PlayListCreate playList) {
        this.playList = playList;
    }

    @Override
    protected ResponseBody<Boolean> call() throws Exception {
        return KKApi.getInstance().playListApi().create(playList);
    }
}