package cn.tealc995.kkmaid.service.api.star;

import cn.tealc995.kikoreu.KKApi;
import cn.tealc995.kikoreu.model.ResponseBody;
import javafx.concurrent.Task;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-05 09:33
 */
public class StarWorkRemoveTask extends Task<ResponseBody<Boolean>> {
    private final String workId;

    public StarWorkRemoveTask(String workId) {
        this.workId = workId;
    }

    @Override
    protected ResponseBody<Boolean> call() throws Exception {
        ResponseBody<Boolean> body = KKApi.getInstance().starApi().deleteStar(workId);
        return body;
    }
}