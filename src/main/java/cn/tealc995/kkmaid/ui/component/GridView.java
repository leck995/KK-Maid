package cn.tealc995.kkmaid.ui.component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 21:38
 */
public class GridView<T> extends ScrollPane {
    private ObservableList<T> items;
    private FlowPane flowPane;
    public GridView() {
        items= FXCollections.observableArrayList();
        flowPane=new FlowPane();
        setFitToHeight(true);
        setFitToWidth(true);
        getChildren().add(flowPane);
    }


    public void add(Node node){
        flowPane.getChildren().add(node);
    }


    public void addAll(Node... nodes){
        flowPane.getChildren().addAll(nodes);
    }
    public FlowPane getFlowPane() {
        return flowPane;
    }

    public ObservableList<T> getItems() {
        return items;
    }

    public void setItems(ObservableList<T> items) {
        this.items = items;
    }
}