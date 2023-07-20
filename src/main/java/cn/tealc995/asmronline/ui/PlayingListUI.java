package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.api.model.Work;
import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.MainDialogEvent;
import cn.tealc995.asmronline.model.Audio;
import cn.tealc995.asmronline.player.LcMediaPlayer;
import cn.tealc995.asmronline.util.CssLoader;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-14 06:08
 */
public class PlayingListUI {

    private JFXDialogLayout root;
    public PlayingListUI() {
        LcMediaPlayer player=LcMediaPlayer.getInstance();
        root = new JFXDialogLayout();
        root.getStylesheets().add(CssLoader.getCss(CssLoader.playing_list));

        VBox headPane=new VBox();
        headPane.setSpacing(5);
        Label songSizeLabel=new Label(String.format("共%d首",player.getSongs().size()));
        songSizeLabel.getStyleClass().add("songs-size");
        Label title=new Label("歌曲列表");
        title.getStyleClass().add("songs-title");
        title.setFont(new Font(20));
        headPane.getChildren().addAll(title,songSizeLabel);
        root.setHeading(headPane);
        root.getStyleClass().add("song-list-view-background");
        AnchorPane anchorPane=new AnchorPane();
        anchorPane.setPrefWidth(300);
        anchorPane.setPrefHeight(560);
        anchorPane.getStyleClass().add("song-list-view-background");

        ListView<Audio> listView=new ListView<>();
        listView.getStyleClass().add("songs-list-view");
        listView.setItems(player.getSongs());
        listView.setCellFactory(new Callback<ListView<Audio>, ListCell<Audio>>() {
            @Override
            public ListCell<Audio> call(ListView<Audio> songInformationListView) {
                ListCell<Audio> cell=new ListCell<Audio>(){
                    @Override
                    protected void updateItem(Audio songInformation, boolean b) {
                        super.updateItem(songInformation, b);
                        if (songInformation!=null){
                            this.setDisable(false);
                            setText(songInformation.getTitle());
                        }else {
                            this.setText(null);
                            this.setDisable(true);
                        }
                    }
                };

                return cell;
            }
        });
        listView.addEventFilter(MouseEvent.ANY, click ->{
            if (click.getEventType()==MouseEvent.MOUSE_PRESSED && click.getClickCount()!=2){
                click.consume();
            }

            if (click.getClickCount()==2) {
                if (click.getEventType()==MouseEvent.MOUSE_CLICKED){
                    player.init(listView.getSelectionModel().getSelectedIndex(),true);
                }
            }
        });
        if (player.getPlayingAudio()!=null)
            listView.getSelectionModel().select(player.getPlayingAudio());


        anchorPane.getChildren().addAll(listView);
        AnchorPane.setTopAnchor(listView,0.0);
        AnchorPane.setBottomAnchor(listView,0.0);
        AnchorPane.setLeftAnchor(listView,0.0);
        AnchorPane.setRightAnchor(listView,0.0);

        root.setBody(anchorPane);

        Button toFolderBtn=new Button("打开目录");
        toFolderBtn.setGraphic(new Region());
        toFolderBtn.setTooltip(new Tooltip("打开ASMR目录"));
        toFolderBtn.getStyleClass().addAll("to-folder-btn");
        toFolderBtn.setOnAction(actionEvent -> {
        /*    if (LcMediaPlayer.getInstance().mediaPlayer ==null) return;
            if (SettingProperties.viewModel == 1){ //网格布局时显示
                MainGridController.getInstance().showDetail(URLUtils.getFolderParentPath(LcMediaPlayer.getInstance().getUrl()));
            }else{ //表格布局时显示

            }*/
            Work work = LcMediaPlayer.getInstance().getWork();
            if (work != null){
                DetailUi detailUi=new DetailUi(work);

                EventBusUtil.getDefault().post(new MainDialogEvent(detailUi.getRoot()));
            }


        });
        Button clearBtn=new Button("清空列表");
        clearBtn.setGraphic(new Region());
        clearBtn.setTooltip(new Tooltip("清空歌曲列表"));
        clearBtn.getStyleClass().addAll("clear-songs-btn");
        clearBtn.setOnAction(actionEvent -> {
          /*  vsongs.clear();
            songSizeLabel.setText(String.format("共%d首",songs.size()));
            setDispose();*/
        });
        root.setActions(toFolderBtn,clearBtn);

   /*     JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
        StackPane dialogContainer = (StackPane) dialog.getChildren().get(0);
        dialogContainer.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        dialogContainer.setPrefHeight(root.getHeight()-200);
        dialogContainer.setPrefWidth(350);
        double offsetX = root.getWidth()/2;
        dialogContainer.setTranslateX(offsetX-180);
        dialogContainer.setAlignment(Pos.BOTTOM_RIGHT);
        dialog.setAlignment(Pos.BOTTOM_RIGHT);
        dialog.show();*/

    /*    dialog.setOnDialogClosed(event -> {
            StackPane  node = (StackPane) event.getSource();
            node.getChildren().clear();
            node=null;
        });*/

    }

    public JFXDialogLayout getRoot() {
        return root;
    }

    public void setRoot(JFXDialogLayout root) {
        this.root = root;
    }
}