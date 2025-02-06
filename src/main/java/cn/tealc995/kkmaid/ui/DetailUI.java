package cn.tealc995.kkmaid.ui;

import atlantafx.base.theme.Styles;
import cn.tealc995.kikoreu.model.Role;
import cn.tealc995.kikoreu.model.Track;
import cn.tealc995.kikoreu.model.Work;
import cn.tealc995.kikoreu.model.playList.PlayList;
import cn.tealc995.kkmaid.App;
import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainDialogEvent;
import cn.tealc995.kkmaid.model.lrc.LrcFile;
import cn.tealc995.kkmaid.service.subtitle.SeekSubtitleFileService;
import cn.tealc995.kkmaid.ui.component.FolderTableView;
import cn.tealc995.kkmaid.ui.stage.LocalSubtitleStage;
import cn.tealc995.kkmaid.util.CssLoader;
import cn.tealc995.teaFX.controls.notification.MessageType;
import cn.tealc995.teaFX.controls.notification.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-13 21:20
 */
public class DetailUI {
    private final BorderPane borderPane;
    private StackPane root;
    private DetailViewModel viewModel;
    private Work work;
    private FolderTableView folderTableView;

    public DetailUI(Work work) {
        this.work = work;
        viewModel = new DetailViewModel(work);
        root = new StackPane();
        borderPane = new BorderPane();
        borderPane.getStyleClass().add("detail-dialog");

        initTop();
        initLeft();
        initCenter();
        if (work.isHasLocalSubtitle()) {
            initRight();
        }

        root.getChildren().add(borderPane);
        root.getStylesheets().add(CssLoader.getCss(CssLoader.detail));

    }


    private void initTop(){
        Label titleLabel = new Label();
        titleLabel.getStyleClass().add("detail-dialog-title");
        titleLabel.setText(work.getTitle());
        borderPane.setTop(titleLabel);
    }

    private void initLeft(){
        //封面
        ImageView posterIV = new ImageView();
        posterIV.imageProperty().bind(viewModel.posterProperty());
        posterIV.setFitHeight(210);
        posterIV.setFitWidth(280);
        Rectangle rectangle = new Rectangle(280, 210);
        rectangle.setArcHeight(15);
        rectangle.setArcWidth(15);
        posterIV.setClip(rectangle);

        //控制按钮，歌单，下载
        Button playListBtn = new Button("添加到歌单", new FontIcon(Material2AL.ADD));
        CheckComboBox<PlayList> checkComboBox = new CheckComboBox<>(viewModel.getPlayLists());
        checkComboBox.setPrefWidth(150.0);
        checkComboBox.setTitle("添加到歌单");
        playListBtn.prefWidthProperty().bind(checkComboBox.widthProperty());
        checkComboBox.setVisible(false);
        checkComboBox.setConverter(new StringConverter<PlayList>() {
            @Override
            public String toString(PlayList playList) {
                if (playList != null) {
                    return playList.getName();
                } else
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
            while (change.next()) {
                if (change.wasAdded()) {
                    for (PlayList playList : change.getAddedSubList()) {
                        if (playList.getExist()) {
                            checkComboBox.getCheckModel().check(playList);
                        }
                    }
                }
            }
        });

        checkComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<? super PlayList>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (PlayList playList : change.getAddedSubList()) {
                        if (!playList.getExist()) {
                            viewModel.updatePlayListWork(playList, false);
                            playList.setExist(true);
                        }
                    }
                }
                if (change.wasRemoved()) {
                    for (PlayList playList : change.getRemoved()) {
                        if (playList.getExist()) {
                            viewModel.updatePlayListWork(playList, true);
                            playList.setExist(false);
                        }
                    }
                }
            }
        });
        StackPane playListPane = new StackPane(checkComboBox, playListBtn);

        Button downloadBtn = new Button(null, new FontIcon(Material2AL.ARROW_DOWNWARD));
        downloadBtn.getStyleClass().add(Styles.BUTTON_ICON);
        downloadBtn.setOnAction(event -> {
            if (Config.setting.getDownloadDir() == null || Config.setting.getAria2Host() == null || Config.setting.getAriaRPCKey() == null) {
                Notification.show("先在设置中配置下载目录和Aria2", MessageType.WARNING, 2000, Pos.TOP_CENTER, App.mainStage);
            } else {
                Track track = new Track();
                track.setTitle(work.getFullId());
                track.setType("folder");
                track.setChildren(folderTableView.getItemsBack());
                DownloadUI downloadUI = new DownloadUI(work, track);
                EventBusUtil.getDefault().post(new MainDialogEvent(downloadUI.getRoot()));
            }
        });
        HBox funcPane = new HBox(15.0,playListPane,downloadBtn);
        funcPane.setAlignment(Pos.CENTER);

        //标签，声优
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
                    Notification.show("世界线可能发生了变动，没有任何响应。", MessageType.SUCCESS, Pos.TOP_CENTER, App.mainStage);
                });
                tagsPane.getChildren().add(label);
            }
        }

        if (work.getVas() != null) {
            for (Role vas : work.getVas()) {
                Label label = new Label(vas.getName());
                label.getStyleClass().add("actor-label");
                label.setOnMouseClicked(mouseEvent -> {
                    Notification.show("世界线可能发生了变动，没有任何响应。", MessageType.SUCCESS, Pos.TOP_CENTER, App.mainStage);
                });
                actorsPane.getChildren().add(label);
            }
        }

        Label circleLabel = new Label();
        if (work.getCircle() != null) {
            circleLabel.setText(work.getCircle().getName());
        } else {
            circleLabel.setVisible(false);
        }

        circleLabel.getStyleClass().add("list-item-big-circle");
        circleLabel.setGraphic(new Region());
        circleLabel.setOnMouseClicked(mouseEvent -> {
            Notification.show("世界线可能发生了变动，没有任何响应。", MessageType.SUCCESS, Pos.TOP_CENTER, App.mainStage);
        });
        circleLabel.setPadding(new Insets(0, 0, 0, 10));


        VBox left = new VBox(posterIV, funcPane, circleLabel, tagsPane, actorsPane);
        left.getStyleClass().add("detail-dialog-left");
        left.setAlignment(Pos.TOP_LEFT);
        left.setSpacing(15);
        left.setPrefWidth(290);
        ScrollPane leftScrollPane = new ScrollPane(left);
        //leftScrollPane.setPrefWidth(300);
        //leftScrollPane.setPrefHeight(500);
        leftScrollPane.setFitToWidth(true);
        leftScrollPane.setFitToHeight(true);
        borderPane.setLeft(leftScrollPane);
    }

    private void initCenter(){
        folderTableView = new FolderTableView(work, viewModel.getTracks());
        borderPane.setCenter(folderTableView);
    }

    private void initRight() {
        VBox lrcPane = new VBox();
        ListView<LrcFile> lrcFileListView = new ListView<>();
        lrcFileListView.setPrefWidth(300);
        Button showLrcFileBtn = new Button("打开字幕");

        SeekSubtitleFileService seekSubtitleFileService = new SeekSubtitleFileService();
        seekSubtitleFileService.setIds(work.getAllId());
        seekSubtitleFileService.setOnSucceeded(event -> {
            List<LrcFile> items = seekSubtitleFileService.getValue();
            if (items != null && !items.isEmpty()) {
                lrcFileListView.setItems(FXCollections.observableList(items));
            } else {
                showLrcFileBtn.setDisable(true);
            }
        });
        seekSubtitleFileService.start();

        lrcFileListView.setCellFactory(lrcFileListView1 -> new LocalLrcCell());
        showLrcFileBtn.setOnAction(event1 -> {
            if (!lrcFileListView.getItems().isEmpty()) {
                LrcFile first = lrcFileListView.getItems().getFirst();
                File file = new File(first.getZipPath());
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        lrcPane.getChildren().addAll(showLrcFileBtn,lrcFileListView);
        VBox.setVgrow(lrcFileListView, Priority.ALWAYS);
        Separator separator = new Separator(Orientation.VERTICAL);
        HBox buttonsPane = new HBox(10.0,separator,lrcPane);
        buttonsPane.getStyleClass().add("right");
        borderPane.setRight(buttonsPane);
    }


    public StackPane getRoot() {
        return root;
    }


    class LocalLrcCell extends ListCell<LrcFile> {
        public LocalLrcCell() {
            setPadding(new Insets(3, 0, 3, 5));
            setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    LocalSubtitleStage stage = new LocalSubtitleStage(getListView().getItems(), getIndex());
                    stage.show();
                }
            });
        }

        @Override
        protected void updateItem(LrcFile lrcFile, boolean b) {
            super.updateItem(lrcFile, b);
            if (!b) {
                setText(lrcFile.getTitle());
                setDisable(false);
            } else {
                setText(null);
                setDisable(true);
            }
        }
    }
}