package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.api.model.playList.PlayList;
import cn.tealc995.asmronline.ui.cell.PlayListCell;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-02 08:40
 */
public class PlayListUI {
    private StackPane root;
    private PlayListViewModel viewModel;

    public PlayListUI() {
        viewModel=new PlayListViewModel();
        root=new StackPane();

        FlowPane flowPane=new FlowPane();
        ScrollPane scrollPane=new ScrollPane(flowPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        ObservableList<PlayList> items = viewModel.getItems();
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        flowPane.setPadding(new Insets(10.0));
        flowPane.setOnScroll(scrollEvent -> {
            double deltaY= scrollEvent.getDeltaY()*7;
            double height = scrollPane.getContent().getBoundsInLocal().getHeight();
            double vvalue = scrollPane.getVvalue();
            scrollPane.setVvalue(vvalue + -deltaY/height);
        });


        for (PlayList playList : items) {
            flowPane.getChildren().add(new PlayListCell(playList));
        }


        items.addListener((ListChangeListener<? super PlayList>) change -> {
            flowPane.getChildren().clear();
            for (PlayList playList : change.getList()) {
                flowPane.getChildren().add(new PlayListCell(playList));
            }
            scrollPane.setVvalue(0);
        });

        ListView<PlayList> listView=new ListView<>();
        listView.setItems(viewModel.getItems());
        listView.setCellFactory(new Callback<ListView<PlayList>, ListCell<PlayList>>() {
            @Override
            public ListCell<PlayList> call(ListView<PlayList> playListListView) {
                ListCell<PlayList> cell=new ListCell<>(){
                    @Override
                    protected void updateItem(PlayList playList, boolean b) {
                        super.updateItem(playList, b);
                        if (playList != null && !b){
                            setText(playList.getName());
                        }else {
                            setText("");
                        }
                    }
                };
                return cell;
            }
        });




        root.getStyleClass().add("background");
        root.getChildren().add(scrollPane);


    }

    public StackPane getRoot() {
        return root;
    }
}