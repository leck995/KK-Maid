package cn.tealc995.asmronline.ui;

import atlantafx.base.theme.Styles;
import cn.tealc995.asmronline.App;
import cn.tealc995.asmronline.api.model.Role;
import cn.tealc995.asmronline.api.model.Work;
import cn.tealc995.asmronline.api.model.playList.PlayList;
import cn.tealc995.asmronline.ui.component.FolderTableView;
import cn.tealc995.asmronline.util.CssLoader;
import cn.tealc995.teaFX.controls.notification.MessageType;
import cn.tealc995.teaFX.controls.notification.Notification;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.IndexedCheckModel;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 21:20
 */
public class DetailUi {
    private StackPane root;
    private DetailViewModel viewModel;

    public DetailUi(Work work) {
        viewModel=new DetailViewModel(work);
        root=new StackPane();
        BorderPane borderPane=new BorderPane();

        ImageView imageView = new ImageView();
        imageView.imageProperty().bind(viewModel.posterProperty());
        imageView.setFitHeight(210);
        imageView.setFitWidth(280);
        Rectangle rectangle=new Rectangle(280,210);
        rectangle.setArcHeight(15);
        rectangle.setArcWidth(15);
        imageView.setClip(rectangle);


        Button playListBtn=new Button("添加到歌单",new FontIcon(Material2AL.ADD));
        //playListBtn.setContentDisplay(ContentDisplay.RIGHT);
        CheckComboBox<PlayList> checkComboBox=new CheckComboBox<>(viewModel.getPlayLists());
        checkComboBox.setPrefWidth(150.0);
        checkComboBox.setTitle("添加到歌单");
        playListBtn.prefWidthProperty().bind(checkComboBox.widthProperty());
        checkComboBox.setVisible(false);
        checkComboBox.setConverter(new StringConverter<PlayList>() {
            @Override
            public String toString(PlayList playList) {
                if (playList != null){
                    return playList.getName();
                }else
                    return null;

            }
            @Override
            public PlayList fromString(String s) {
                return null;
            }
        });

        playListBtn.setOnAction(event -> {
            viewModel.getPlayList();
            checkComboBox.show();
        });

       checkComboBox.getItems().addListener((ListChangeListener<? super PlayList>) change -> {
           while (change.next()){
               if (change.wasAdded()){
                   for (PlayList playList : change.getAddedSubList()) {
                       if (playList.getExist()){
                           checkComboBox.getCheckModel().check(playList);
                       }

                   }
               }
           }

       });

        checkComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<? super PlayList>) change -> {
            while (change.next()){
                if (change.wasAdded()){
                    for (PlayList playList : change.getAddedSubList()) {
                        if (!playList.getExist()){
                            viewModel.updatePlayListWork(playList,false);
                            playList.setExist(true);
                        }
                    }
                }
                if (change.wasRemoved()){
                    for (PlayList playList : change.getRemoved()) {
                        if (playList.getExist()){
                            viewModel.updatePlayListWork(playList,true);
                            playList.setExist(false);
                        }

                    }
                }
            }
        });



        StackPane playListPane=new StackPane(checkComboBox,playListBtn);




        FlowPane tagsPane = new FlowPane();
        tagsPane.setHgap(8.0);
        tagsPane.setVgap(5.0);

        FlowPane actorsPane = new FlowPane();
        actorsPane.setHgap(8.0);
        actorsPane.setVgap(5.0);

        if (work.getTags() != null) {
            for (Role tag : work.getTags()) {
                Label label = new Label(tag.getName());
                label.getStyleClass().add("tag-label");
                label.setOnMouseClicked(mouseEvent -> {
                    Notification.show("世界线可能发生了变动，没有任何响应。", MessageType.SUCCESS,Pos.TOP_CENTER, App.mainStage);
                });
                tagsPane.getChildren().add(label);
            }
        }

        if (work.getVas() != null) {
            for (Role vas : work.getVas()) {
                Label label = new Label(vas.getName());
                label.getStyleClass().add("actor-label");
                label.setOnMouseClicked(mouseEvent -> {
                    Notification.show("世界线可能发生了变动，没有任何响应。", MessageType.SUCCESS,Pos.TOP_CENTER, App.mainStage);
                });
                actorsPane.getChildren().add(label);
            }
        }


        Label circleLabel = new Label();
        if (work.getCircle() != null) {
            circleLabel.setText(work.getCircle().getName());
        }else {
            circleLabel.setVisible(false);
        }

        circleLabel.getStyleClass().add("list-item-big-circle");
        circleLabel.setGraphic(new Region());
        circleLabel.setOnMouseClicked(mouseEvent -> {
            Notification.show("世界线可能发生了变动，没有任何响应。", MessageType.SUCCESS,Pos.TOP_CENTER, App.mainStage);
        });
        circleLabel.setPadding(new Insets(0,0,0,10));


        Label titleLabel=new Label();
        titleLabel.getStyleClass().add("detail-dialog-title");
        titleLabel.setText(work.getTitle());


        VBox left = new VBox(imageView,playListPane,circleLabel, tagsPane, actorsPane);
        left.getStyleClass().add("detail-dialog-left");
        left.setAlignment(Pos.TOP_LEFT);
        left.setSpacing(15);
        left.setPrefWidth(290);
        ScrollPane leftScrollPane = new ScrollPane(left);
        leftScrollPane.setPrefWidth(300);


        FolderTableView folderTableView = new FolderTableView(work,viewModel.getTracks());


        borderPane.setTop(titleLabel);
        borderPane.setLeft(leftScrollPane);
        borderPane.setCenter(folderTableView);
        borderPane.getStyleClass().add("detail-dialog");
        root.getChildren().add(borderPane);
        root.getStylesheets().add(CssLoader.getCss(CssLoader.detail));

    }


    public StackPane getRoot() {
        return root;
    }
}