package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.api.model.MainWorks;
import cn.tealc995.asmronline.api.model.playList.PlayList;
import cn.tealc995.asmronline.api.model.Work;
import cn.tealc995.asmronline.api.model.playList.PlayListRemoveWork;
import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.MainNotificationEvent;
import cn.tealc995.asmronline.event.MainPlayListRemoveWorkEvent;
import cn.tealc995.asmronline.service.MainPlayListService;
import cn.tealc995.asmronline.service.PlayListRemoveWorkService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @program: Asmr-Online
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
    private SimpleStringProperty playListId;
    private SimpleIntegerProperty removeIndex;//用来让界面上的work移除
    private SimpleBooleanProperty loading;
    private SimpleStringProperty message;
    private MainPlayListService service;
    private final ObservableList<Integer> pageSizeItems=FXCollections.observableArrayList(12,24,48,96);

    private PlayListRemoveWorkService playListRemoveWorkService;
    public MainPlayListViewModel(PlayList playList) {
        EventBusUtil.getDefault().register(this);
        init(playList);
        update();
    }

    private void init(PlayList playList){
        title=new SimpleStringProperty(playList.getName());
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
        if (Config.HOST.get() == null || Config.HOST.get().length() == 0){
            message.set("请先填写服务器地址");
            return;
        }
        if (service==null){
            service=new MainPlayListService();
            loading.bind(Bindings.createBooleanBinding(() -> Boolean.valueOf(service.getMessage()),service.messageProperty()));
            mainWorks.bind(service.valueProperty());
        }
        Map<String,String> params=new HashMap<>();
        params.put("page", String.valueOf(currentPage.get() + 1));
        params.put("pageSize",String.valueOf(pageSize.get()));
        params.put("id", playListId.get());
        service.setParams(params);
        service.setHost(Config.HOST.get());
        service.restart();
    }





    @Subscribe
    public void removeWork(MainPlayListRemoveWorkEvent event){
        System.out.println(event.getWork().getId());
        if (playListRemoveWorkService == null){
            playListRemoveWorkService=new PlayListRemoveWorkService();
            playListRemoveWorkService.valueProperty().addListener((observableValue, aBoolean, t1) -> {
                if (t1 != null && t1) {
                    EventBusUtil.getDefault().post(new MainNotificationEvent("移除成功"));
                    Iterator<Work> iterator = workItems.iterator();
                    while (iterator.hasNext()) {
                        Work next = iterator.next();
                        List<String> works = playListRemoveWorkService.getWork().getWorks();
                        if (works.contains(next.getId())) {
                            iterator.remove();
                            System.out.println("移除");
                        }
                    }
                }
            });
        }
        playListRemoveWorkService.setUrl(Config.HOST.get());
        playListRemoveWorkService.setWork(new PlayListRemoveWork(playListId.get(), List.of(event.getWork().getId()),true));
        playListRemoveWorkService.restart();
    }


    public String getTitle() {
        return " 当前歌单: "+title.get();
    }


    public void setPageSize(int pageSize) {
        this.pageSize.set(pageSize);
    }

    /**
     * @description: 当前功能不知为何没生效，在ui中虽然绑定了removeIndex,但值变化时不会监听到，很奇怪。请注意
     * @name: removeWork
     * @author: Leck
     * @param:	event
     * @return  void
     * @date:   2023/7/15
     */
/*    @Subscribe
    public void removeWork(GridItemRemoveEvent event){
        removeIndex.set(-1);
        if (title.get() == CategoryType.STAR){
            for (int i = 0; i < workItems.size(); i++) {
                if (event.getWork().getId().equals(workItems.get(i).getId())){
                    removeIndex.set(i);
                    break;
                }
            }
        }

    }*/


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
}