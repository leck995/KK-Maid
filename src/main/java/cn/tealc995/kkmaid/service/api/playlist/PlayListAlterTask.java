package cn.tealc995.kkmaid.service.api.playlist;

import cn.tealc995.kikoreu.KKApi;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.playList.PlayListAlter;
import cn.tealc995.kikoreu.model.playList.PlayListRemoveWork;
import javafx.concurrent.Task;

/**
 * @description:
 * @author: Leck
 * @create: 2023-08-05 09:33
 */
public class PlayListAlterTask extends Task<ResponseBody<Boolean>> {
    private PlayListAlter playList;
    private PlayListRemoveWork works;

    public PlayListAlterTask(PlayListAlter playList, PlayListRemoveWork works) {
        this.playList = playList;
        this.works = works;
    }

    @Override
    protected ResponseBody<Boolean> call() throws Exception {
        if (works != null){
            KKApi.getInstance().playListApi().add(works);
        }

        return KKApi.getInstance().playListApi().alter(playList);
    }



}