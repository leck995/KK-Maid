package cn.tealc995.kkmaid.ui.component;


import atlantafx.base.theme.Styles;
import cn.tealc995.kikoreu.HttpUtils;
import cn.tealc995.kikoreu.model.Track;
import cn.tealc995.kikoreu.model.Work;
import cn.tealc995.kkmaid.App;
import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainDialogEvent;
import cn.tealc995.kkmaid.filter.SupportAudioFormat;
import cn.tealc995.kkmaid.filter.SupportImageFormat;
import cn.tealc995.kkmaid.filter.SupportSubtitleFormat;
import cn.tealc995.kkmaid.model.Audio;
import cn.tealc995.kkmaid.model.Music;
import cn.tealc995.kkmaid.model.lrc.LrcFile;
import cn.tealc995.kkmaid.model.lrc.LrcType;
import cn.tealc995.kkmaid.player.MediaPlayerUtil;
import cn.tealc995.kkmaid.ui.DownloadUI;
import cn.tealc995.kkmaid.ui.stage.ImageViewStage;
import cn.tealc995.kkmaid.ui.stage.LocalSubtitleStage;
import cn.tealc995.kkmaid.ui.stage.SubtitleStage;
import cn.tealc995.teaFX.controls.notification.MessageType;
import cn.tealc995.teaFX.controls.notification.Notification;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @description:
 * @author: Leck
 * @create: 2023-03-12 11:14
 */
public class FolderTableView extends BorderPane {
    public static final int ALL = 0;
    public static final int PICTURE = 1;
    public static final int VIDEO = 2;
    public static final int AUDIO = 3;

    private TableView<Track> tableView;
    //private ObservableList<AsmrFile> filterItems;
    private Work work;
    private ObservableList<Track> items;
    private ObservableList<Track> itemsBack;
    private StringProperty rootPath;
    private StringProperty currentPath;
    private PathViewPane pathViewPane;
    private Image audioImage, picImage, otherImage, videoImage, folderImage;

    public FolderTableView(Work work, ObservableList<Track> items) {
        this.work = work;
        this.items = items;

        this.items.addListener((ListChangeListener<? super Track>) observable -> {
            if (itemsBack == null){
                itemsBack = FXCollections.observableArrayList(items);
            }
        });
        rootPath = new SimpleStringProperty();
        currentPath = new SimpleStringProperty("根目录");
        initTableView();
        initPathView();
        setTop(pathViewPane);
        setCenter(tableView);
    }



    private void initPathView() {
        pathViewPane = new PathViewPane();
        pathViewPane.getStyleClass().add("path-view-pane");
        pathViewPane.setMaxWidth(800.0);
        pathViewPane.setPrefWidth(40.0);
        pathViewPane.setPath(currentPath.get());

        pathViewPane.onActionProperty().addListener(((observableValue, s, t1) -> {
            if (t1 != null) {
                if (t1.equals("根目录")) {
                    items.setAll(itemsBack);
                    currentPath.set("根目录");
                } else {
                    String[] title = t1.split("/");
                    Track child = null;
                    for (int i = 1; i < title.length; i++) {
                        if (i == 1) {
                            for (Track track : itemsBack) {
                                if (track.getTitle().equals(title[i])) {
                                    child = track;
                                    break;
                                }
                            }
                        } else {
                            for (Track track : child.getChildren()) {
                                if (track.getTitle().equals(title[i])) {
                                    child = track;
                                    break;
                                }
                            }
                        }
                        assert child != null;
                        items.setAll(child.getChildren());
                        currentPath.set(t1);
                    }
                }

            }
        }));
    }

    private void initTableView() {
        //rootPath.set(file.getPath());
        //currentPath.set(file.getPath());
        tableView = new TableView<>();
        tableView.getStyleClass().add("folder-table-view");
        tableView.setItems(items);
        TableColumn<Track, String> nameCol = new TableColumn<>("名称");
        nameCol.getStyleClass().add("detail-table-name");
        nameCol.setPrefWidth(500);
        TableColumn<Track, String> typeCol = new TableColumn<>("类型");
        nameCol.getStyleClass().add("detail-table-type");
        typeCol.setPrefWidth(60);


        tableView.getColumns().addAll(typeCol, nameCol);
        nameCol.setCellValueFactory(new PropertyValueFactory<Track, String>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<Track, String>("type"));
        typeCol.setCellFactory(AsmrFileStringTableColumn -> {
            TableCell<Track, String> tableCell = new TableCell<>() {
                @Override
                protected void updateItem(String s, boolean b) {
                    if (!b) {
                        ImageView imageView;
                        if (s.equals("folder")) {
                            imageView = new ImageView();
                            imageView.setFitHeight(30);
                            imageView.setPreserveRatio(true);
                            imageView.setImage(getFolderImage());
                            setGraphic(imageView);
                        } else if (s.equals("audio")) {
                            imageView = new ImageView();
                            imageView.setFitHeight(30);
                            imageView.setPreserveRatio(true);
                            imageView.setImage(getAudioImage());
                            setGraphic(imageView);
                        } else if (s.equals("image")) {
                            imageView = new ImageView();
                            imageView.setFitHeight(30);
                            imageView.setPreserveRatio(true);
                            imageView.setImage(getPicImage());
                            setGraphic(imageView);
                        } else {
                            imageView = new ImageView();
                            imageView.setFitHeight(30);
                            imageView.setPreserveRatio(true);
                            imageView.setImage(getOtherImage());
                            setGraphic(imageView);
                        }
                    } else {
                        setGraphic(null);
                    }
                }
            };
            return tableCell;
        });

        tableView.setRowFactory(view -> {
            TableRow<Track> row = new TableRow<>() {
                @Override
                protected void updateItem(Track asmrFile, boolean b) {
                    super.updateItem(asmrFile, b);
                    if (!b) {
                        setDisable(false);
                        setOnMouseClicked(mouseEvent -> {
                            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                                if (asmrFile == null) return;
                                String temp = asmrFile.getType();
                                if (temp.equals("folder")) {
                                    update(asmrFile);
                                } else if (temp.equals("audio")) {
                                    List<Audio> list = new ArrayList<>();
                                    int index = 0;
                                    int currentTrack = 0;
                                    boolean hasSubtitle = false;
                                    List<LrcFile> lrcList = new ArrayList<>();
                                    for (Track track : items) {
                                        if (track.getType().equals("audio")) {
                                            if (SupportAudioFormat.compareFile(track.getTitle())) {
                                                list.add(new Audio(track.getTitle(), track.getMediaDownloadUrl(), track.getMediaStreamUrl(), track.getStreamLowQualityUrl(), track.getDuration()));
                                                if (track != asmrFile) {
                                                    index += 1;
                                                } else {
                                                    currentTrack = index;
                                                }
                                            }
                                        } else if (track.getType().equals("text")) {
                                            if (SupportSubtitleFormat.compareFile(track.getTitle())) {
                                                lrcList.add(new LrcFile(track.getTitle(), track.getMediaDownloadUrl(), LrcType.NET));
                                                hasSubtitle = true; //当前目录有字幕文件
                                            }
                                        }
                                    }

                                    if (!hasSubtitle) { //当前文件夹内没有字幕时，去所有目录寻找
                                        for (Track track : itemsBack) {
                                            lrc(track, lrcList);
                                        }
                                        lrcList.sort(Comparator.comparing(LrcFile::getTitle));
                                    }

                                    if (lrcList.size() > 0) {
                                        MediaPlayerUtil.mediaPlayer().setMusic(new Music(work, list, lrcList), currentTrack);
                                    } else {
                                        MediaPlayerUtil.mediaPlayer().setMusic(new Music(work, list), currentTrack);
                                    }

                                } else if (temp.equals("image")) {
                                    if (SupportImageFormat.compareFile(asmrFile.getTitle())) {
                                        List<Track> list = new ArrayList<>();
                                        int index = 0;
                                        int currentTrack = 0;
                                        for (Track track : items) {
                                            if (track.getType().equals("image")) {
                                                list.add(track);
                                                if (track != asmrFile) {
                                                    index += 1;
                                                } else {
                                                    currentTrack = index;
                                                }
                                            }
                                        }
                                        ImageViewStage imageViewStage = ImageViewStage.getInstance(list, currentTrack);
                                        imageViewStage.setIconified(false);
                                        imageViewStage.requestFocus();
                                        imageViewStage.toFront();
                                        imageViewStage.show();
                                    } else { //不支持的图片格式的处理，通知消息线程进行提醒

                                    }
                                } else if (temp.equals("text")) { //对文本文件进行处理，查看文本文件
                                    boolean isSubtitle = SupportSubtitleFormat.compareFile(asmrFile.getTitle());
                                    if (isSubtitle) {
                                        int index = 0;
                                        List<LrcFile> lrcFileList = new ArrayList<>();
                                        List<Track> subtitleTrackList = getItems().filtered(track -> SupportSubtitleFormat.compareFile(track.getTitle())).stream().toList();
                                        for (int i = 0; i < subtitleTrackList.size(); i++) {
                                            Track track = subtitleTrackList.get(i);
                                            if (getItem() == track) {
                                                index = i;
                                            }
                                            lrcFileList.add(new LrcFile(track.getTitle(), track.getMediaDownloadUrl(), LrcType.NET));
                                        }

                                        LocalSubtitleStage stage = new LocalSubtitleStage(lrcFileList,index);
                                        stage.show();
                                    }else {
                                        String row = HttpUtils.download(asmrFile.getMediaDownloadUrl());
                                        SubtitleStage stage = new SubtitleStage(row);
                                        stage.show();
                                    }


                                }
                            }
                        });
            /*            MenuItem downloadItem=new MenuItem("下载");
                        downloadItem.setOnAction(event -> {
                            System.out.println("当前下载的url:"+asmrFile.getMediaDownloadUrl());
                        });
                        downloadItem.setDisable(true);
                        ContextMenu contextMenu=new ContextMenu(downloadItem);
                        setContextMenu(contextMenu);*/
                    } else {
                        setDisable(true);
                    }
                }
            };

            return row;
        });
    }


    private void lrc(Track track, List<LrcFile> list) {
        if (track.getChildren() != null) {
            for (Track child : track.getChildren()) {
                lrc(child, list);
            }
        } else {
            if (SupportSubtitleFormat.compareFile(track.getTitle())) {
                list.add(new LrcFile(track.getTitle(), track.getMediaDownloadUrl(), LrcType.NET));
            }
        }
    }


    /**
     * @return void
     * @description: 更新表格，同于在点击子目录时调用
     * @name: update
     * @author: Leck
     * @param: file
     * @date: 2023/3/12
     */
    public void update(Track track) {
        items.setAll(track.getChildren());
        currentPath.set(currentPath.get() + "/" + track.getTitle());
        pathViewPane.setPath(currentPath.get());
    }


    private Image getAudioImage() {
        if (audioImage == null) {
            audioImage = new Image(this.getClass().getResource("/cn/tealc995/kkmaid/image/suffix/file-audio.png").toExternalForm());
        }
        return audioImage;
    }

    private Image getPicImage() {
        if (picImage == null) {
            picImage = new Image(this.getClass().getResource("/cn/tealc995/kkmaid/image/suffix/file-image.png").toExternalForm());
        }
        return picImage;
    }

    private Image getOtherImage() {
        if (otherImage == null) {
            otherImage = new Image(this.getClass().getResource("/cn/tealc995/kkmaid/image/suffix/file-unknown.png").toExternalForm());
        }
        return otherImage;
    }


    private Image getVideoImage() {
        if (videoImage == null) {
            videoImage = new Image(this.getClass().getResource("/cn/tealc995/kkmaid/image/suffix/file-video.png").toExternalForm());
        }
        return videoImage;
    }

    public Image getFolderImage() {
        if (folderImage == null) {
            folderImage = new Image(this.getClass().getResource("/cn/tealc995/kkmaid/image/suffix/file-folder.png").toExternalForm());
        }
        return folderImage;
    }

    public String getRootPath() {
        return rootPath.get();
    }

    public StringProperty rootPathProperty() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath.set(rootPath);
    }

    public String getCurrentPath() {
        return currentPath.get();
    }

    public StringProperty currentPathProperty() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath.set(currentPath);
    }

    public ObservableList<Track> getItems() {
        return items;
    }

    public ObservableList<Track> getItemsBack() {
        return itemsBack;
    }
}