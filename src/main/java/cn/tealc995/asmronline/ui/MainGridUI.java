package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.App;
import cn.tealc995.asmronline.api.model.MainWorks;
import cn.tealc995.asmronline.api.model.SortType;
import cn.tealc995.asmronline.api.model.Work;
import cn.tealc995.asmronline.ui.cell.WorkCell;
import cn.tealc995.asmronline.util.AnchorPaneUtil;
import cn.tealc995.asmronline.util.CssLoader;
import cn.tealc995.teaFX.controls.LoadingDot;
import cn.tealc995.teaFX.controls.notification.MessageType;
import cn.tealc995.teaFX.controls.notification.Notification;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import javafx.util.converter.FormatStringConverter;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 23:18
 */
public class MainGridUI {
    private StackPane root;
    private MainGridViewModel viewModel;



    public MainGridUI() {
        viewModel=new MainGridViewModel();
        init();
    }
    public MainGridUI(String searchKey) {
        viewModel=new MainGridViewModel();
        init();
    }

    private void  init(){
        root=new StackPane();
        ScrollPane scrollPane=new ScrollPane();

        Label allCountTitle=new Label();
        allCountTitle.textProperty().bind(Bindings.createStringBinding(() -> String.format("%s(%d)",viewModel.getTitle(),viewModel.getTotalCount()),viewModel.totalCountProperty()));
        allCountTitle.getStyleClass().add("all-count-title");

        ChoiceBox<SortType> sortChoiceBox=new ChoiceBox<>(viewModel.getSortItems());
        sortChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(SortType sortType) {
                if (sortType != null)
                    return sortType.getValue();
                else
                    return "";
            }

            @Override
            public SortType fromString(String s) {
                return null;
            }
        });

        sortChoiceBox.getSelectionModel().select(viewModel.getSelectSortType());
        sortChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, sortType, t1) -> viewModel.setSelectSortType(t1));


        CheckBox subtextCheckBox=new CheckBox("字幕");
        subtextCheckBox.selectedProperty().bindBidirectional(viewModel.subtextProperty());
        CheckBox descCheckBox=new CheckBox("倒序");
        descCheckBox.selectedProperty().bindBidirectional(viewModel.descOrderProperty());

        HBox centerPane=new HBox(subtextCheckBox,descCheckBox);
        centerPane.setSpacing(20);
        centerPane.setAlignment(Pos.CENTER);
        BorderPane topPane=new BorderPane();
        topPane.setTop(allCountTitle);
        topPane.setLeft(sortChoiceBox);

        topPane.setCenter(centerPane);
        topPane.setPadding(new Insets(5));

        SimpleListProperty<Work> works=new SimpleListProperty<>();

        ObservableList<Work> workItems = viewModel.getWorkItems();
        FlowPane flowPane=new FlowPane();
        flowPane.setHgap(10);
        flowPane.setVgap(10);
       // works.bind(viewModel.workItemsProperty());


        flowPane.setOnScroll(scrollEvent -> {
            double deltaY= scrollEvent.getDeltaY()*7;
            double height = scrollPane.getContent().getBoundsInLocal().getHeight();
            double vvalue = scrollPane.getVvalue();
            scrollPane.setVvalue(vvalue + -deltaY/height);
        });


        for (Work work : workItems) {
            flowPane.getChildren().add(new WorkCell(work));
        }

        workItems.addListener((ListChangeListener<? super Work>) change -> {
            flowPane.getChildren().clear();
            for (Work work : change.getList()) {
                flowPane.getChildren().add(new WorkCell(work));
            }
            scrollPane.setVvalue(0);
        });

        SimpleIntegerProperty removeIndex=new SimpleIntegerProperty();
        removeIndex.bind(viewModel.removeIndexProperty());
        removeIndex.addListener((observableValue, number, t1) -> {

            flowPane.getChildren().remove(t1.intValue());
        });




        Pagination pagination=new Pagination();
        pagination.currentPageIndexProperty().bindBidirectional(viewModel.currentPageProperty());
        pagination.pageCountProperty().bind(viewModel.countPageProperty());






        TextField pageField=new TextField();

        pageField.getStyleClass().add("page-field");
        pageField.promptTextProperty().bind(Bindings.createStringBinding(()->String.format("共%d页",viewModel.getCountPage()),viewModel.countPageProperty()));
        pageField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                pageField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        pageField.setOnAction(actionEvent -> {
            commit(pageField.getText());
        });


//        Button skipPageBtn=new Button();
//        skipPageBtn.getStyleClass().add("page-skip-btn");
        //skipPageBtn.setMaxSize(22,22);
        //skipPageBtn.setGraphic(new Region());
        AnchorPane anchorPane=new AnchorPane(pageField);

        AnchorPaneUtil.setPosition(pageField,21.0,null,null,null);
        // AnchorPaneUtil.setPosition(skipPageBtn,21.0,null,null,70.0);
        HBox bottomPane=new HBox(pagination,anchorPane);
        bottomPane.setSpacing(10.0);
        bottomPane.setAlignment(Pos.CENTER);



        scrollPane.setContent(flowPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);


        BorderPane borderPane=new BorderPane();
        borderPane.setTop(topPane);
        borderPane.setCenter(scrollPane);
        borderPane.setBottom(bottomPane);

        LoadingDot loadingDot=new LoadingDot();
        loadingDot.visibleProperty().bind(viewModel.loadingProperty());







        root.setPadding(new Insets(0,0,0,10));
        root.getChildren().addAll(borderPane,loadingDot);
        root.getStyleClass().add("background");
        root.getStylesheets().add(CssLoader.getCss(CssLoader.mainGrid));
    }

    /**
     * @description:
     * @name: commit
     * @author: Leck
     * @param:	s	
     * @return  void
     * @date:   2023/7/13
     */
    private void commit(String s){
        viewModel.setCurrentPage(Integer.valueOf(s)-1);
    }


    public StackPane getRoot() {
        return root;
    }
}