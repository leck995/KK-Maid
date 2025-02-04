package cn.tealc995.kkmaid.service.api;

import cn.tealc995.kikoreu.KKApi;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.Track;
import cn.tealc995.kkmaid.util.comparator.TrackComparator;
import javafx.concurrent.Task;

import java.util.List;

/**
 * @program: KK-Maid
 * @description: 获取指定作品的在线文件列表
 * @author: Leck
 * @create: 2025-02-01 23:30
 */
public class WorkTracksTask extends Task<ResponseBody<List<Track>>> {
    private final String key;

    private final TrackComparator trackComparator;

    public WorkTracksTask(String key) {
        this.key = key;
        trackComparator = new TrackComparator();
    }

    @Override
    protected ResponseBody<List<Track>> call() throws Exception {
        ResponseBody<List<Track>> body = KKApi.getInstance().trackApi().track(key);
        // 递归对每个 Track 的 children 进行排序
        sortChildrenRecursively(body.getData());

        return body;
    }

    // 递归排序 children
    private void sortChildrenRecursively(List<Track> children) {
        if (children != null && !children.isEmpty()) {
            // 对当前层级的 children 进行排序
            children.sort(trackComparator);

            // 递归对每个 child 的 children 进行排序
            for (Track child : children) {
                sortChildrenRecursively(child.getChildren());
            }
        }
    }
}