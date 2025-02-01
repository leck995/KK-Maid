package cn.tealc995.kkmaid.ui;

import cn.tealc995.kkmaid.Config;
import cn.tealc995.kikoreu.model.RoleEx;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainCenterEvent;
import cn.tealc995.kkmaid.event.SearchEvent;
import cn.tealc995.kkmaid.service.api.CategoryTask;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 07:11
 */
public class CategoryViewModel {
    private ObservableList<RoleEx> items;
    private ObservableList<RoleEx> itemsBack;
    private SimpleStringProperty title;

    private SimpleStringProperty searchKey;
    private SimpleBooleanProperty loading;

    public CategoryViewModel(CategoryType type) {
        items= FXCollections.observableArrayList();
        itemsBack=FXCollections.observableArrayList();
        title=new SimpleStringProperty();
        searchKey=new SimpleStringProperty();
        loading=new SimpleBooleanProperty(false);

        CategoryTask task=new CategoryTask(type);

        title.bind(task.titleProperty());
        task.valueProperty().addListener((observableValue, roleExes, t1) -> {
            items.setAll(t1.getData());
            itemsBack.setAll(items);
        });

        loading.bind(Bindings.createBooleanBinding(() -> Boolean.valueOf(task.getMessage()),task.messageProperty()));
       Thread.startVirtualThread(task);

        searchKey.addListener((observableValue, s, t1) -> filter());
    }



    public void search(CategoryType type,String key){
        EventBusUtil.getDefault().post(new SearchEvent(type,key));
        EventBusUtil.getDefault().post(new MainCenterEvent(null,false,true));
    }

    public void filter(){
        items.setAll(itemsBack.stream().filter(roleEx -> roleEx.getName().contains(searchKey.get())).toList());
    }
    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public ObservableList<RoleEx> getItems() {
        return items;
    }

    public String getSearchKey() {
        return searchKey.get();
    }

    public SimpleStringProperty searchKeyProperty() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey.set(searchKey);
    }

    public boolean isLoading() {
        return loading.get();
    }

    public SimpleBooleanProperty loadingProperty() {
        return loading;
    }
}