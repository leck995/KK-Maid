package cn.tealc995.kkmaid.service.subtitle.beans;

import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kkmaid.model.lrc.LrcBean;
import javafx.concurrent.Task;

import java.util.List;

/**
 * @program: KK-Maid
 * @description: 读取字幕的基本类
 * @author: Leck
 * @create: 2025-02-03 21:05
 */
public class SubtitleBeansBaseTask extends Task<ResponseBody<List<LrcBean>>> {

    @Override
    protected ResponseBody<List<LrcBean>> call() throws Exception {
        return null;
    }

}