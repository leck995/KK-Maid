package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.api.model.Work;
import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.MainDialogEvent;
import cn.tealc995.asmronline.model.Audio;
import cn.tealc995.asmronline.player.MediaPlayerUtil;
import cn.tealc995.asmronline.player.TeaMediaPlayer;
import cn.tealc995.asmronline.util.CssLoader;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Callback;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * @program: Asmr-Online
 * @description: 弹出式播放列表界面
 * @author: Leck
 * @create: 2023-07-14 06:08
 */
public class PlayingListUI {

    private final TeaMediaPlayer player;
    private JFXDialogLayout root;
    public PlayingListUI() {
        player = MediaPlayerUtil.mediaPlayer();
        root = new JFXDialogLayout();
        root.getStylesheets().add(CssLoader.getCss(CssLoader.playing_list));

        VBox headPane=new VBox();
        headPane.setSpacing(5);
        Label songSizeLabel=new Label(String.format("共%d首", player.getSongs().size()));
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
                            MenuItem playItem=new MenuItem("播放");
                            playItem.setOnAction(event -> {
                                player.init(listView.getItems().indexOf(songInformation),true);
                                listView.getSelectionModel().select(songInformation);
                            });
                            MenuItem deleteItem=new MenuItem("移除");
                            deleteItem.setOnAction(event -> {
                                player.removeAudio(listView.getItems().indexOf(songInformation));
                                listView.getSelectionModel().select(player.getPlayingIndexInList());
                                songSizeLabel.setText(String.format("共%d首", player.getSongs().size()));
                            });
                            ContextMenu contextMenu=new ContextMenu(playItem,deleteItem);
                            setContextMenu(contextMenu);
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
        if (player.getPlayingAudio() != null)
            listView.getSelectionModel().select(player.getPlayingAudio());


        anchorPane.getChildren().addAll(listView);
        AnchorPane.setTopAnchor(listView,0.0);
        AnchorPane.setBottomAnchor(listView,0.0);
        AnchorPane.setLeftAnchor(listView,0.0);
        AnchorPane.setRightAnchor(listView,0.0);

        root.setBody(anchorPane);

        Button filterMp3Btn=new Button("过滤Mp3");
        filterMp3Btn.getStyleClass().add("filter-btn");
        filterMp3Btn.setTooltip(new Tooltip("移除Mp3格式的待播放文件"));
        filterMp3Btn.setOnAction(actionEvent -> {
           filter(".mp3");
        });
        Button filterWavBtn=new Button("过滤Wav");
        filterWavBtn.getStyleClass().add("filter-btn");
        filterWavBtn.setTooltip(new Tooltip("移除Wav格式的待播放文件"));
        filterWavBtn.setOnAction(actionEvent -> {
            filter(".wav");
        });

        Button toFolderBtn=new Button("打开目录");
        toFolderBtn.setGraphic(new Region());
        toFolderBtn.setTooltip(new Tooltip("打开ASMR目录"));
        toFolderBtn.getStyleClass().addAll("to-folder-btn");
        toFolderBtn.setOnAction(actionEvent -> {
            Work work = player.getWork();
            if (work != null){
                DetailUI detailUi=new DetailUI(work);
                EventBusUtil.getDefault().post(new MainDialogEvent(detailUi.getRoot()));
            }
        });
        Button clearBtn=new Button("清空列表");
        clearBtn.setGraphic(new Region());
        clearBtn.setTooltip(new Tooltip("清空歌曲列表"));
        clearBtn.getStyleClass().addAll("clear-songs-btn");
        clearBtn.setOnAction(actionEvent -> {
            player.clearPlayingList();
            songSizeLabel.setText(String.format("共%d首",0));
        });

        root.setActions(filterMp3Btn,filterWavBtn,toFolderBtn,clearBtn);
    }


    /**
     * 过滤播放列表中指定文件类型的子项，通常用于处理Mp3与Wav
     * @param suffix
     */
    private void filter(String suffix){
        player.getSongs().removeIf(song -> song.getTitle().toLowerCase().endsWith(suffix.toLowerCase()));
        //当类别为空，暂替播放；对于格式不同的正在播放媒体，暂时不错处理
        if (player.getSongs().isEmpty()){
            player.dispose();
        }
    }


    public JFXDialogLayout getRoot() {
        return root;
    }

    public void setRoot(JFXDialogLayout root) {
        this.root = root;
    }
}