package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.api.model.playList.PlayList;
import cn.tealc995.asmronline.service.PlayListService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

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