package cn.tealc995.kkmaid.ui;

import cn.tealc995.kkmaid.Config;
import cn.tealc995.api.model.MainWorks;
import cn.tealc995.api.model.SortType;
import cn.tealc995.api.model.Work;
import cn.tealc995.kkmaid.event.BlackWorkEvent;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.GridItemRemoveEvent;
import cn.tealc995.kkmaid.event.SearchEvent;
import cn.tealc995.kkmaid.service.MainGridService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 23:19
 */
public class MainGridViewModel {
    private SimpleObjectProperty<MainWorks> mainWorks;

    private ObservableList<Work> workItems;
    private SimpleIntegerProperty countPage;
    private SimpleIntegerProperty currentPage;
    private SimpleIntegerProperty pageSize;
    private SimpleLongProperty totalCount;
    private SimpleObjectProperty<CategoryType> title;
    private SimpleBooleanProperty subtext;

    private SimpleStringProperty playListId;


    private SimpleBooleanProperty descOrder;

    private ObservableList<SortType> sortItems= FXCollections.observableArrayList(SortType.release,SortType.create_date,SortType.nsfw,SortType.rate_average_2dp,SortType.dl_count,SortType.price,SortType.review_count,SortType.random);

    private SimpleObjectProperty<SortType> selectSortType;
    private SimpleStringProperty searchKey;

    private SimpleBooleanProperty loading;
    private SimpleStringProperty message;


    private MainGridService service;
    public MainGridViewModel() {
        EventBusUtil.getDefault().register(this);
        init();
        update();
    }

    private void init(){
        title=new SimpleObjectProperty<>(CategoryType.ALL);
        mainWorks=new SimpleObjectProperty<>();
        workItems=FXCollections.observableArrayList();
        countPage=new SimpleIntegerProperty(10);
        currentPage=new SimpleIntegerProperty(0);
        pageSize=new SimpleIntegerProperty(48);
        totalCount=new SimpleLongProperty(0);
        subtext=new SimpleBooleanProperty(Config.gridSubtitleModel.get());
        descOrder=new SimpleBooleanProperty(Config.gridSortDescModel.get());
        selectSortType=new SimpleObjectProperty<>(SortType.valueOf(Config.gridOrder.get()));
        searchKey=new SimpleStringProperty();
        loading=new SimpleBooleanProperty(false);
        message=new SimpleStringProperty();
        playListId=new SimpleStringProperty();


        mainWorks.addListener((observableValue, mainWorks1, mainWorks2) -> {
            if (mainWorks2 != null){
                workItems.setAll(mainWorks2.getWorks());
                countPage.set((int) Math.ceil((double) mainWorks2.getPagination().getTotalCount() /mainWorks2.getPagination().getPageSize()));
                totalCount.set(mainWorks2.getPagination().getTotalCount());
            }
        });

        selectSortType.addListener((observableValue, sortType, t1) -> update());
        subtext.addListener((observableValue, sortType, t1) -> update());
        descOrder.addListener((observableValue, sortType, t1) -> update());
        currentPage.addListener((observableValue, number, t1) -> update());

    }

    public void update(){
        if (Config.HOST.get() == null || Config.HOST.get().length() == 0){
            message.set("请先填写服务器地址");
            return;
        }

        if (service==null){
            service=new MainGridService();
            loading.bind(Bindings.createBooleanBinding(() -> Boolean.valueOf(service.getMessage()),service.messageProperty()));
            mainWorks.bind(service.valueProperty());
        }



        Map<String,String> params=new HashMap<>();
        if (title.get() !=CategoryType.PLAY_LIST){
            params.put("order",getSelectSortType().name());
            if(getSelectSortType()==SortType.nsfw){
                descOrder.set(false);
            }
            params.put("sort",isDescOrder() ? "desc":"asc");
            params.put("page",String.valueOf(getCurrentPage()+1));
            params.put("subtitle",isSubtext() ? "1" : "0");
            params.put("pageSize",String.valueOf(pageSize.get()));
            //params.put("withPlaylistStatus[]","c939f5c9-04ff-49fb-99e7-09872c6b639a");
            params.put("includeTranslationWorks","true");
        }else {
            params.put("page", String.valueOf(currentPage.get() + 1));
            params.put("pageSize",String.valueOf(pageSize.get()));
            params.put("id", playListId.get());
        }



        service.setParams(params);
        service.setSearchKey(searchKey.get());
        service.setType(title.get());
        service.setHost(Config.HOST.get());
        service.restart();

    }



    @Subscribe
    public void setSearchKey(SearchEvent event) {
        if (event.getType()==CategoryType.SEARCH){
            subtext.set(false);
        }
        title.set(event.getType());
        this.searchKey.set(event.getKey());
        this.playListId.set(event.getInfo());
        setCurrentPage(0);
        update();
    }

    public String getTitle() {
        if (title.get() ==CategoryType.ALL){
            return title.get().getTitle();
        }else if (title.get() ==CategoryType.STAR){
            return title.get().getTitle();
        }else{
            return title.get().getTitle()+" : "+searchKey.get();
        }
    }


    /**
     * @description: 当前功能不知为何没生效，在ui中虽然绑定了removeIndex,但值变化时不会监听到，很奇怪。请注意
     * @name: removeWork
     * @author: Leck
     * @param:	event
     * @return  void
     * @date:   2023/7/15
     */
    @Subscribe
    public void removeWork(GridItemRemoveEvent event){
        if (title.get() == CategoryType.STAR){
            workItems.remove(event.getWork());
        }
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

    public boolean isSubtext() {
        return subtext.get();
    }

    public SimpleBooleanProperty subtextProperty() {
        return subtext;
    }

    public boolean isDescOrder() {
        return descOrder.get();
    }

    public SimpleBooleanProperty descOrderProperty() {
        return descOrder;
    }

    public ObservableList<SortType> getSortItems() {
        return sortItems;
    }

    public SortType getSelectSortType() {
        return selectSortType.get();
    }

    public SimpleObjectProperty<SortType> selectSortTypeProperty() {
        return selectSortType;
    }

    public void setSelectSortType(SortType selectSortType) {
        this.selectSortType.set(selectSortType);
    }

    public ObservableList<Work> getWorkItems() {
        return workItems;
    }


}