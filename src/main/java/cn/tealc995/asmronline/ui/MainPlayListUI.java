package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.api.model.playList.PlayList;
import cn.tealc995.asmronline.api.model.Work;
import cn.tealc995.asmronline.ui.cell.PlayListWorkCell;
import cn.tealc995.asmronline.util.AnchorPaneUtil;
import cn.tealc995.asmronline.util.CssLoader;
import cn.tealc995.teaFX.controls.LoadingDot;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 23:18
 */
public class MainPlayListUI {
    private StackPane root;
    private MainPlayListViewModel viewModel;



    public MainPlayListUI(PlayList playList) {
        viewModel=new MainPlayListViewModel(playList);
        init();
    }


    private void  init(){
        root=new StackPane();
        ScrollPane scrollPane=new ScrollPane();

        Label allCountTitle=new Label();
        allCountTitle.textProperty().bind(Bindings.createStringBinding(() -> String.format("%s (%d)",viewModel.getTitle(),viewModel.getTotalCount()),viewModel.totalCountProperty()));
        allCountTitle.getStyleClass().add("all-count-title");


        BorderPane topPane=new BorderPane();
        topPane.setTop(allCountTitle);
        topPane.setPadding(new Insets(5));



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
            flowPane.getChildren().add(new PlayListWorkCell(work));
        }

        workItems.addListener((ListChangeListener<? super Work>) change -> {
            flowPane.getChildren().clear();
            for (Work work : change.getList()) {
                flowPane.getChildren().add(new PlayListWorkCell(work));
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
        pageField.promptTextProperty().bind(
                Bindings.createStringBinding(
                        ()->
                                String.format("共%d页",viewModel.getCountPage()
                        )
                        ,viewModel.countPageProperty())
        );

        pageField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                pageField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        pageField.setOnAction(actionEvent -> {
            commit(pageField.getText());
        });






        ChoiceBox<Integer> pageSizeChoice=new ChoiceBox<>(viewModel.getPageSizeItems());
        pageSizeChoice.getStyleClass().add("page-size-choice-box");
        pageSizeChoice.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer integer) {
                return String.valueOf(integer);
            }

            @Override
            public Integer fromString(String s) {
                return null;
            }
        });
        pageSizeChoice.getSelectionModel().select(1);
        pageSizeChoice.getSelectionModel().selectedItemProperty().addListener((observableValue, sortType, t1) -> viewModel.setPageSize(t1));



        AnchorPane anchorPane=new AnchorPane(pageField,pageSizeChoice);
        AnchorPaneUtil.setPosition(pageField,21.0,null,null,null);
        AnchorPaneUtil.setPosition(pageSizeChoice,21.0,null,null,110.0);
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