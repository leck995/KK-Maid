package cn.tealc995.kkmaid.service.api.works;

import cn.tealc995.kikoreu.KKApi;
import cn.tealc995.kikoreu.model.*;
import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kkmaid.model.SubtitleData;
import cn.tealc995.kkmaid.ui.CategoryType;
import javafx.collections.ObservableSet;
import javafx.concurrent.Task;

import java.util.Map;

/**
 * @description: 主界面中，获取作品列表
 * @author: Leck
 * @create: 2023-07-15 23:16
 */
public class MainWorksService extends WorksBaseService {
    private Map<String, String> params;
    private String searchKey;
    private CategoryType type;
    private SubtitleData subtitleData;

    @Override
    protected Task<ResponseBody<MainWorks>> createTask() {
        Task<ResponseBody<MainWorks>> task = new Task<>() {
            @Override
            protected ResponseBody<MainWorks> call() {
                updateMessage("true");
                if (subtitleData == null) {
                    subtitleData = SubtitleData.getInstance();
                }

                ResponseBody<MainWorks> responseBody = fetchWorks();
                if (responseBody.isSuccess()) {
                    MainWorks works = responseBody.getData();
                    if (works != null) {
                        processWorks(works);
                    }
                }
                updateMessage("false");
                return responseBody;
            }
        };
        return task;
    }

    private ResponseBody<MainWorks> fetchWorks() {
        return switch (type) {
            case ALL -> KKApi.getInstance().worksApi().works(params);
            case STAR -> KKApi.getInstance().starApi().star(params);
            case PLAY_LIST -> KKApi.getInstance().playListApi().works(params);
            default -> KKApi.getInstance().searchApi().search(String.format(type.getFormat(), searchKey), params);
        };
    }

    private void processWorks(MainWorks works) {
        ObservableSet<String> workBlackList = Config.blackList.getWorkBlackList();
        ObservableSet<String> tagBlackList = Config.blackList.getTagBlackList();

        // 过滤黑名单作品
        works.getWorks().removeIf(work -> {
            boolean isBlack = isBlacklisted(work, workBlackList, tagBlackList);
            if (!isBlack) {
                matchLocalSubtitles(work);
            }
            return isBlack;
        });
    }

    private boolean isBlacklisted(Work work, ObservableSet<String> workBlackList, ObservableSet<String> tagBlackList) {
        work.setBlack(workBlackList.contains(work.getFullId()));
        for (Role tag : work.getTags()) {
            if (!tag.getName().isEmpty() && tagBlackList.contains(tag.getName())) {
                work.setBlack(true);
                break;
            }
        }
        return work.isBlack();
    }

    private void matchLocalSubtitles(Work work) {
        if (subtitleData.getFolderList() != null) {
            for (String folder : subtitleData.getFolderList()) {
                if (exist(work, folder)) {
                    work.setHasLocalSubtitle(true);
                    break;
                }
            }
        }

        if (subtitleData.getZipList() != null) {
            for (String zip : subtitleData.getZipList()) {
                if (exist(work, zip)) {
                    work.setHasLocalSubtitle(true);
                    break;
                }
            }
        }
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }
}
