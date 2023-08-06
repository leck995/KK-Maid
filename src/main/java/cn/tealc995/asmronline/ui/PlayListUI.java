package cn.tealc995.asmronline.ui;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Popover;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import cn.tealc995.asmronline.api.model.playList.PlayList;
import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.MainDialogEvent;
import cn.tealc995.asmronline.ui.cell.PlayListCell;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

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

        Label title=new Label("我的歌单");
        title.getStyleClass().add(Styles.TITLE_2);

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


        Button addPlayListBtn=new Button(null,new FontIcon(Material2AL.ADD));
        addPlayListBtn.getStyleClass().add(Styles.BUTTON_ICON);
        addPlayListBtn.setTranslateX(-10);
        addPlayListBtn.setTranslateY(5);
        addPlayListBtn.setOnAction(event -> showAddPane());



        StackPane topPane=new StackPane(title,addPlayListBtn);
        topPane.setPadding(new Insets(0.0,0.0,0.0,10.0));
        StackPane.setAlignment(title, Pos.CENTER_LEFT);
        StackPane.setAlignment(addPlayListBtn, Pos.CENTER_RIGHT);
        VBox parent=new VBox(topPane,scrollPane);

        root.getStyleClass().add("background");
        root.getChildren().addAll(parent);



    }




    public void showAddPane(){
        JFXDialog dialog=new JFXDialog();
        Card addPane = createAddPane(dialog);
        dialog.setContent(addPane);
        dialog.setDialogContainer(root);
        dialog.show();
    }
    public Card createAddPane(JFXDialog dialog){
        Label title=new Label("新建歌单");
        title.getStyleClass().add(Styles.TITLE_3);

        Label nameLabel=new Label("名称:");
        nameLabel.setPrefWidth(100.0);
        TextField nameField=new TextField();
        nameField.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.length() == 0){
                nameField.pseudoClassStateChanged(Styles.STATE_DANGER,true);
            }else {
                nameField.pseudoClassStateChanged(Styles.STATE_DANGER,false);
            }
        });
        nameField.setPrefWidth(250.0);

        InputGroup nameGroup=new InputGroup(nameLabel,nameField);
        nameGroup.setSpacing(1);

        Label privateLabel=new Label("可见性:");
        privateLabel.setPrefWidth(100.0);
        ChoiceBox<String> privateBox=new ChoiceBox<>(FXCollections.observableArrayList("私有(仅自己可以浏览)","不公开(知道链接的人可以浏览)","公开(所有人可以浏览)"));
        privateBox.getSelectionModel().select(0);
        privateBox.setPrefWidth(250.0);
        InputGroup privateGroup=new InputGroup(privateLabel,privateBox);
        privateGroup.setSpacing(1);

        Label infoLabel=new Label("简介:");
        infoLabel.setPrefWidth(100.0);
        TextField infoField=new TextField();
        infoField.setPrefWidth(250.0);
        InputGroup infoGroup=new InputGroup(infoLabel,infoField);
        infoGroup.setSpacing(1);

        Popover popover=new Popover(new Label("识别本地字幕，将存在字幕的作品添加到歌单中，从而实现浏览含有本地带字幕的作品列表"));
        Hyperlink hyperlink=new Hyperlink("?");
        hyperlink.setOnAction(event -> popover.show(hyperlink));
        CheckBox subtextBox=new CheckBox("导入本地字幕作品");
        subtextBox.setGraphic(hyperlink);
        subtextBox.setContentDisplay(ContentDisplay.RIGHT);



        VBox bodyPane=new VBox(nameGroup,privateGroup,infoGroup,subtextBox);
        bodyPane.setSpacing(15.0);

        Button addBtn=new Button("添加");
        addBtn.getStyleClass().add(Styles.ACCENT);
        addBtn.setOnAction(event -> {
            if (nameField.getText().length() == 0){
                nameField.pseudoClassStateChanged(Styles.STATE_DANGER,true);
                return;
            }


            viewModel.createPlayList(nameField.getText(),privateBox.getSelectionModel().getSelectedIndex(),infoField.getText(),"",subtextBox.isSelected());
        });




        Button cancelBtn=new Button("取消");
        cancelBtn.setOnAction(event -> dialog.close());
        HBox footPane=new HBox(addBtn,cancelBtn);
        footPane.setSpacing(10.0);
        footPane.setAlignment(Pos.CENTER_RIGHT);

        Card card=new Card();
        card.setHeader(title);
        card.setBody(bodyPane);
        card.setFooter(footPane);

        return card;
    }

    public StackPane getRoot() {
        return root;
    }
}