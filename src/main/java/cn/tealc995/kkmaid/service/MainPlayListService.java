package cn.tealc995.kkmaid.service;

import cn.tealc995.kikoreu.KKApi;
import cn.tealc995.kikoreu.model.*;
import cn.tealc995.kkmaid.config.Config;
import javafx.collections.ObservableSet;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: Asmr-Online
 * @description: 获取歌单的作品列表
 * @author: Leck
 * @create: 2023-07-15 23:16
 */
public class MainPlayListService extends Service<MainWorks> {
    private Map<String, String> params;
    private String host;

    private Set<String> folderList;//保存所有字幕
    private Set<String> zipList;//保存所有字幕

    @Override
    protected Task<MainWorks> createTask() {

        Task<MainWorks> task = new Task<MainWorks>() {
            @Override
            protected MainWorks call() throws Exception {
                updateMessage("true");
                if (folderList == null && zipList == null) { //此处有待优化，比如更新了目录地址，无法实时更新
                    updateList();
                }

                ResponseBody<MainWorks> responseBody = KKApi.getInstance().playListApi().works(params);

                if (responseBody.isSuccess()) {
                    MainWorks works = responseBody.getData();
                    ObservableSet<String> workBlackList = Config.blackList.getWorkBlackList();
                    ObservableSet<String> tagBlackList = Config.blackList.getTagBlackList();
                    if (works != null) {
                        for (Work work : works.getWorks()) {
                            //筛选RJ黑名单
                            work.setBlack(workBlackList.contains(work.getFullId()));
                            //筛选标签黑名单
                            for (Role tag : work.getTags()) {
                                if (tagBlackList.contains(tag.getName())) {
                                    work.setBlack(true);
                                    break;
                                }
                            }

                            //不是黑名单作品，匹配本地字幕
                            if (!work.isBlack()) {
                                if (!work.isHas_subtitle() && folderList != null) {
                                    for (String s : folderList) {
                                        boolean exist = exist(work, s);
                                        if (exist) {
                                            work.setHas_subtitle(true);//存在字幕文件夹
                                            work.setHasLocalSubtitle(true);
                                            break;
                                        }
                                    }
                                }
                                if (!work.isHas_subtitle() && zipList != null) {
                                    for (String s : zipList) {
                                        boolean exist = exist(work, s);
                                        if (exist) {
                                            work.setHas_subtitle(true);//存在字幕zip包
                                            work.setHasLocalSubtitle(true);
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        //过滤所有黑名单作品
                        List<Work> list = works.getWorks().stream().filter(work -> !work.isBlack()).toList();
                        works.setWorks(list);
                    }

                    updateMessage("false");
                    return works;
                } else {
                    updateMessage("false");
                    return null;
                }
            }
        };
        return task;
    }


    private void updateList(){
        String lrcFileFolder = Config.setting.getLrcFileFolder();
        if (lrcFileFolder != null && !lrcFileFolder.isEmpty()){
            folderList=new HashSet<>();
            File dir=new File(lrcFileFolder);
            File[] files = dir.listFiles((dir1, name) -> dir1.isDirectory());
            if (files != null) {
                for (File file : files) {
                    folderList.add(file.getName().toUpperCase());
                }
            }
        }

        String lrcZipFolder = Config.setting.getLrcZipFolder();
        if (lrcZipFolder != null && !lrcZipFolder.isEmpty()){
            zipList=new HashSet<>();
            File dir=new File(lrcZipFolder);
            File[] files = dir.listFiles((dir1, name) -> dir1.isFile() && name.toLowerCase().endsWith(".zip"));
            if (files != null) {
                for (File file : files) {
                    zipList.add(file.getName().toUpperCase());
                }
            }
        }
    }



    private boolean exist(Work work, String name) {
        String id = getId(work.getFullId());
        if (name.contains(id)) {
            return true;
        } else {
            if (work.hasLanguages()) {
                for (LanguageEdition languageEdition : work.getLanguage_editions()) {
                    id = getId(languageEdition.getWorkno());
                    if (name.contains(id)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private String getId(String id) {
        String temp;
        if (id.toLowerCase().contains("rj")) {
            temp = id.toLowerCase();
        } else {
            temp = "rj" + id;
        }
        return temp;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

}
