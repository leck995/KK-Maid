package cn.tealc995.asmronline.service;

import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.api.PlayListApi;
import cn.tealc995.asmronline.api.SearchApi;
import cn.tealc995.asmronline.api.StarApi;
import cn.tealc995.asmronline.api.WorksApi;
import cn.tealc995.asmronline.api.model.LanguageEdition;
import cn.tealc995.asmronline.api.model.MainWorks;
import cn.tealc995.asmronline.api.model.Role;
import cn.tealc995.asmronline.api.model.Work;
import cn.tealc995.asmronline.ui.CategoryType;
import javafx.collections.ObservableSet;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-15 23:16
 */
public class MainGridService extends Service<MainWorks> {
    private Map<String,String> params;
    private String searchKey;
    private CategoryType type;
    private String host;

    private Set<String> folderList;//保存所有字幕
    private Set<String> zipList;//保存所有字幕
    @Override
    protected Task<MainWorks> createTask() {

        Task<MainWorks> task=new Task<MainWorks>() {
            @Override
            protected MainWorks call() throws Exception {
                updateMessage("true");
                if (folderList == null && zipList ==null){ //此处有待优化，比如更新了目录地址，无法实时更新
                    updateList();
                }

                MainWorks works;
                if (type==CategoryType.ALL){
                    works = WorksApi.works(host, params);
                }else if (type==CategoryType.STAR){
                    works= StarApi.star(host,params);
                } else if (type==CategoryType.PLAY_LIST){
                    works= PlayListApi.works(host,params);
                }else {
                    works = SearchApi.search(host,String.format(type.getFormat(),searchKey), params);
                }


                ObservableSet<String> workBlackList = Config.workBlackList;
                if (works != null){
                    Iterator<Work> iterator = works.getWorks().iterator();
                    while (iterator.hasNext()){
                        Work work = iterator.next();
                        if (workBlackList.contains(work.getFullId())){
                            iterator.remove();
                            continue;
                        }else {
                            work.setBlack(false);
                        }
                        if (!work.isHas_subtitle() && folderList != null){
                            for (String s : folderList) {
                                boolean exist = exist(work, s);
                                if (exist){
                                    work.setHas_subtitle(true);//存在字幕文件夹
                                    break;
                                }
                            }
                        }
                        if (!work.isHas_subtitle() && zipList != null){
                            for (String s : zipList) {
                                boolean exist = exist(work, s);
                                if (exist){
                                    work.setHas_subtitle(true);//存在字幕zip包
                                    break;
                                }
                            }
                        }
                    }
                }

                List<Work> list = works.getWorks().stream().filter(work -> {
                    for (Role tag : work.getTags()) {
                        for (String s : Config.tagBlackList) {
                            if (tag.getName().equals(s)) {
                                return false;
                            }
                        }
                    }
                    return true;
                }).toList();
                works.setWorks(list);


                updateMessage("false");
                return works;
            }
        };
        return task;
    }



    private void
    updateList(){
        if (Config.lrcFileFolder.get() != null && Config.lrcFileFolder.get().length() > 0){
            folderList=new HashSet<>();
            File dir=new File(Config.lrcFileFolder.get());
            File[] files = dir.listFiles((dir1, name) -> dir1.isDirectory());
            if (files != null){
                for (File file : files) {
                    folderList.add(file.getName().toLowerCase());
                }
            }

        }

        if (Config.lrcZipFolder.get() != null && Config.lrcZipFolder.get().length() > 0){
            zipList=new HashSet<>();
            File dir=new File(Config.lrcZipFolder.get());
            File[] files = dir.listFiles(pathname -> pathname.isFile() && pathname.getName().toLowerCase().endsWith(".zip"));
            if (files != null){
                for (File file : files) {
                    zipList.add(file.getName().toLowerCase());
                }
            }
        }
    }




    private boolean exist(Work work,String name){
        String id= getId(work.getFullId());
        if (name.contains(id)){
            return true;
        }else {
            if (work.hasLanguages()){
                for (LanguageEdition languageEdition : work.getLanguage_editions()) {
                    id=getId(languageEdition.getWorkno());
                    if (name.contains(id)){
                        return true;
                    }
                }
            }
        }
        return false;
    }



    private String getId(String id){
        String temp;
        if (id.toLowerCase().contains("rj")){
            temp=id.toLowerCase();
        }else {
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

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }
}