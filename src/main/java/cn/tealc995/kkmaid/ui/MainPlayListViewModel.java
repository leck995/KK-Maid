package cn.tealc995.kkmaid.ui;

import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kkmaid.App;
import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kikoreu.model.MainWorks;
import cn.tealc995.kikoreu.model.playList.PlayList;
import cn.tealc995.kikoreu.model.Work;
import cn.tealc995.kikoreu.model.playList.PlayListRemoveWork;
import cn.tealc995.kkmaid.event.BlackWorkEvent;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainNotificationEvent;
import cn.tealc995.kkmaid.event.MainPlayListRemoveWorkEvent;
import cn.tealc995.kkmaid.service.api.works.PlayListWorksService;
import cn.tealc995.kkmaid.service.api.playlist.PlayListRemoveWorkTask;
import cn.tealc995.teaFX.controls.notification.MessageType;
import cn.tealc995.teaFX.controls.notification.Notification;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-12 23:19
 */
public class MainPlayListViewModel {
    private SimpleObjectProperty<MainWorks> mainWorks;
    private ObservableList<Work> workItems;
    private SimpleIntegerProperty countPage;
    private SimpleIntegerProperty currentPage;
    private SimpleIntegerProperty pageSize;
    private SimpleLongProperty totalCount;
    private SimpleStringProperty title;
    private SimpleStringProperty description;
    private SimpleStringProperty playListId;
    private SimpleIntegerProperty removeIndex;//用来让界面上的work移除
    private SimpleBooleanProperty loading;
    private SimpleStringProperty message;
    private PlayListWorksService service;
    private final ObservableList<Integer> pageSizeItems=FXCollections.observableArrayList(12,24,48,96);

    public MainPlayListViewModel(PlayList playList) {
        EventBusUtil.getDefault().register(this);
        init(playList);
        update();
    }

    private void init(PlayList playList){
        title=new SimpleStringProperty(playList.getName());
        description=new SimpleStringProperty(playList.getDescription());
        playListId=new SimpleStringProperty(playList.getId());

        mainWorks=new SimpleObjectProperty<>();
        workItems=FXCollections.observableArrayList();
        countPage=new SimpleIntegerProperty(10);
        currentPage=new SimpleIntegerProperty(0);
        pageSize=new SimpleIntegerProperty(24);
        totalCount=new SimpleLongProperty(0);
        removeIndex=new SimpleIntegerProperty(-1);
        loading=new SimpleBooleanProperty(false);
        message=new SimpleStringProperty();


        mainWorks.addListener((observableValue, mainWorks1, mainWorks2) -> {
            if (mainWorks2 != null){

                workItems.setAll(mainWorks2.getWorks());
                countPage.set((int) Math.ceil((double) mainWorks2.getPagination().getTotalCount() /mainWorks2.getPagination().getPageSize()));
                totalCount.set(mainWorks2.getPagination().getTotalCount());
            }
        });
        currentPage.addListener((observableValue, number, t1) -> update());
        pageSize.addListener((observableValue, number, t1) -> update());
    }

    public void update(){
        if (Config.setting.getHOST() == null || Config.setting.getHOST().isEmpty()){
            message.set("请先填写服务器地址");
            return;
        }
        if (service==null){
            service=new PlayListWorksService();
            loading.bind(Bindings.createBooleanBinding(() -> Boolean.valueOf(service.getMessage()),service.messageProperty()));
            service.valueProperty().addListener((observableValue, mainWorksResponseBody, t1) -> {
                if (t1 != null) {
                    if (t1.isSuccess()){
                        mainWorks.set(t1.getData());
                    }else {
                        EventBusUtil.getDefault().post(new MainNotificationEvent("加载失败："+t1.getMsg()));
                    }
                }

            });
        }
        Map<String,String> params=new HashMap<>();
        params.put("page", String.valueOf(currentPage.get() + 1));
        params.put("pageSize",String.valueOf(pageSize.get()));
        params.put("id", playListId.get());
        service.setParams(params);
        service.restart();
    }





    @Subscribe
    public void removeWork(MainPlayListRemoveWorkEvent event){
        PlayListRemoveWork playListRemoveWork=new PlayListRemoveWork(playListId.get(), List.of(event.getWork().getId()),true);
        PlayListRemoveWorkTask task =new PlayListRemoveWorkTask(playListRemoveWork);
        task.setOnSucceeded(workerStateEvent -> {
            ResponseBody<Boolean> value = task.getValue();
            if (value.isSuccess()){
                EventBusUtil.getDefault().post(new MainNotificationEvent("移除成功"));
                Iterator<Work> iterator = workItems.iterator();
                while (iterator.hasNext()) {
                    Work next = iterator.next();
                    List<String> works = playListRemoveWork.getWorks();
                    if (works.contains(next.getId())) {
                        iterator.remove();
                    }
                }
            }
        });
        Thread.startVirtualThread(task);
    }


    public String getTitle() {
        return " 当前歌单: "+title.get();
    }


    public void setPageSize(int pageSize) {
        this.pageSize.set(pageSize);
    }




    /**
     * @description: 在加入黑名单的时候调用此方法，用于在当前列表中移除
     * @name: removeWork
     * @author: Leck
     * @param:	event
     * @return  void
     * @date:   2023/8/8
     */
    @Subscribe
    public void removeWork(BlackWorkEvent event){
        workItems.remove(event.getWork());
    }


    public void nextPage(){
        int next = getCurrentPage() + 1;
        if (next < getCountPage()){
            setCurrentPage(next);
        }
    }
    public void prePage(){
        int pre = getCurrentPage() - 1;
        if (pre >= 0){
            setCurrentPage(pre);
        }
    }

    public boolean isLoading() {
        return loading.get();
    }

    public String getMessage() {
        return message.get();
    }

    public SimpleStringProperty messageProperty() {
        return message;
    }

    public SimpleBooleanProperty loadingProperty() {
        return loading;
    }

    public MainWorks getMainWorks() {
        return mainWorks.get();
    }

    public SimpleObjectProperty<MainWorks> mainWorksProperty() {
        return mainWorks;
    }

    public int getCountPage() {
        return countPage.get();
    }

    public SimpleIntegerProperty countPageProperty() {
        return countPage;
    }

    public int getCurrentPage() {
        return currentPage.get();
    }

    public SimpleIntegerProperty currentPageProperty() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage.set(currentPage);
    }

    public long getTotalCount() {
        return totalCount.get();
    }

    public SimpleLongProperty totalCountProperty() {
        return totalCount;
    }

    public ObservableList<Work> getWorkItems() {
        return workItems;
    }

    public int getRemoveIndex() {
        return removeIndex.get();
    }

    public SimpleIntegerProperty removeIndexProperty() {
        return removeIndex;
    }

    public ObservableList<Integer> getPageSizeItems() {
        return pageSizeItems;
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }
}