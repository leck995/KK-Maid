package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.api.model.playList.PlayList;
import cn.tealc995.asmronline.api.model.playList.PlayListCreate;
import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.MainNotificationEvent;
import cn.tealc995.asmronline.event.PlayListRemoveEvent;
import cn.tealc995.asmronline.service.PlayListCreateService;
import cn.tealc995.asmronline.service.PlayListDeleteService;
import cn.tealc995.asmronline.service.PlayListService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-02 08:40
 */
public class PlayListViewModel {
    private final PlayListService service;
    private ObservableList<PlayList> items;
    private SimpleIntegerProperty currentPage;
    private SimpleIntegerProperty pageSize;
    private SimpleIntegerProperty pageCount;


    public PlayListViewModel() {
        EventBusUtil.getDefault().register(this);
        items= FXCollections.observableArrayList();
        currentPage=new SimpleIntegerProperty(0);
        pageSize=new SimpleIntegerProperty(12);
        pageCount=new SimpleIntegerProperty(0);
        service = new PlayListService();
        service.valueProperty().addListener((observableValue, mainPlayList, t1) -> {
            if (t1 != null){
                items.setAll(t1.getPlayLists());

            }
        });
        update();
    }

    private void update(){
        Map<String,String> params=new HashMap<>();
        params.put("page", String.valueOf(currentPage.get() + 1));
        params.put("pageSize",String.valueOf(pageSize.get()));
        params.put("filterBy","all");
        service.setHost(Config.HOST.get());
        service.setParams(params);
        service.restart();
    }


    public void createPlayList(String name,Integer privacy,String description,String worksRow,boolean subtext){
        PlayListCreateService service=new PlayListCreateService();
        service.valueProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1!=null){
                EventBusUtil.getDefault().post(new MainNotificationEvent(String.format("创建歌单%s",t1 ? "成功" : "失败")));
                if (t1)
                    update();
            }
        });
        service.setUrl(Config.HOST.get());


        if (subtext){
            Set<String> list = getAllLocalSubtext();
            PlayListCreate playListCreate=new PlayListCreate(name,privacy,description,list.stream().toList()); //这里实现是有问题的，因为目前没做高级选项，所以暂时可以这么写
            service.setPlayList(playListCreate);
        }else {
            PlayListCreate playListCreate=new PlayListCreate(name,privacy,description,worksRow);
            service.setPlayList(playListCreate);
        }
        service.start();

    }




    /**
     * @description: 获取本地字幕所有的作品ID,不含RJ
     * @name: getAllLocalSubtext
     * @author: Leck
     * @param:
     * @return  java.util.Set<java.lang.String>
     * @date:   2023/8/7
     */
    public  Set<String> getAllLocalSubtext(){
        Set<String> list=new HashSet<>();

        Pattern pattern=Pattern.compile("(?<=RJ)\\d+");
        Matcher matcher;
        if (Config.lrcFileFolder.get() != null && Config.lrcFileFolder.get().length() > 0){
            File folder=new File(Config.lrcFileFolder.get());
            if (folder.exists()){
                File[] files = folder.listFiles(File::isDirectory);
                for (File file : files) {
                    matcher = pattern.matcher(file.getName().toUpperCase());
                    while (matcher.find()){
                        list.add(matcher.group());
                    }
                }
            }
        }
        if (Config.lrcZipFolder.get()  != null && Config.lrcZipFolder.get().length() > 0){
            File folder=new File(Config.lrcZipFolder.get());
            if (folder.exists()){
                File[] files = folder.listFiles(File::isFile);
                for (File file : files) {
                    matcher = pattern.matcher(file.getName().toUpperCase());
                    while (matcher.find()){
                        list.add(matcher.group());
                    }
                }
            }
        }


        return list;

    }



    @Subscribe
    public void delete(PlayListRemoveEvent event){
        PlayListDeleteService service1=new PlayListDeleteService();

        service1.valueProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1 != null && t1){
                EventBusUtil.getDefault().post(new MainNotificationEvent("删除成功"));
                items.remove(event.getPlayList());
            }
        });
        service1.setUrl(Config.HOST.get());
        service1.setId(event.getPlayList().getId());
        service1.start();

    }



    public ObservableList<PlayList> getItems() {
        return items;
    }

    public int getCurrentPage() {
        return currentPage.get();
    }

    public SimpleIntegerProperty currentPageProperty() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize.get();
    }

    public SimpleIntegerProperty pageSizeProperty() {
        return pageSize;
    }

    public int getPageCount() {
        return pageCount.get();
    }

    public SimpleIntegerProperty pageCountProperty() {
        return pageCount;
    }
}