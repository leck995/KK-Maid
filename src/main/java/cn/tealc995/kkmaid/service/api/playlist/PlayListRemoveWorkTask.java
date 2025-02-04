package cn.tealc995.kkmaid.service.api.playlist;

import cn.tealc995.kikoreu.KKApi;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.playList.PlayListRemoveWork;
import javafx.concurrent.Task;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-05 09:33
 */
public class PlayListRemoveWorkTask extends Task<ResponseBody<Boolean>> {

    private final PlayListRemoveWork work;

    public PlayListRemoveWorkTask(PlayListRemoveWork work) {
        this.work = work;
    }

    @Override
    protected ResponseBody<Boolean> call() throws Exception {
        if (work.getRemove()){
            return KKApi.getInstance().playListApi().remove(work);
        }else {
            return KKApi.getInstance().playListApi().add(work);
        }
    }

}