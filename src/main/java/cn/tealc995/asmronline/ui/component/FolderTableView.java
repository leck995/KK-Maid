package cn.tealc995.asmronline.ui.component;


import cn.tealc995.asmronline.api.model.Track;
import cn.tealc995.asmronline.api.model.Work;
import cn.tealc995.asmronline.model.Audio;
import cn.tealc995.asmronline.model.Music;
import cn.tealc995.asmronline.model.lrc.LrcFile;
import cn.tealc995.asmronline.model.lrc.LrcType;
import cn.tealc995.asmronline.player.LcMediaPlayer;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.util.Callback;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

/**
 * @program: AsmrPlayer
 * @description:
 * @author: Leck
 * @create: 2023-03-12 11:14
 */
public class FolderTableView extends BorderPane {
    public static final int ALL=0;
    public static final int PICTURE=1;
    public static final int VIDEO=2;
    public static final int AUDIO=3;

    private TableView<Track> tableView;
    //private ObservableList<AsmrFile> filterItems;
    private Work work;
    private ObservableList<Track> items;
    private ObservableList<Track> itemsBack;
    private StringProperty rootPath;
    private StringProperty currentPath;
    private PathViewPane pathViewPane;
    private Image audioImage,picImage,otherImage,videoImage,folderImage;

    public FolderTableView(Work work,ObservableList<Track> items) {
        this.work=work;
        this.items=items;
        itemsBack=FXCollections.observableArrayList(items);
        rootPath=new SimpleStringProperty();
        currentPath=new SimpleStringProperty("根目录");
        initTableView();
        initPathView();

        setTop(pathViewPane);
        setCenter(tableView);
        //setBottom(initFilterPane());
    }




    private void initPathView(){
        pathViewPane=new PathViewPane();
        pathViewPane.getStyleClass().add("path-view-pane");
        pathViewPane.setMaxWidth(800.0);
        pathViewPane.setPrefWidth(40.0);
        pathViewPane.setPath(currentPath.get());

        pathViewPane.onActionProperty().addListener(((observableValue, s, t1) -> {
            if (t1!=null){
               // String s1 = URLUtils.getParent(rootPath.get(),null) + File.separator + t1;
                System.out.println(t1);
                if (t1.equals("根目录")){
                    items.setAll(itemsBack);
                    currentPath.set("根目录");
                }else {
                    String[] title = t1.split("/");
                    Track child=null;
                    for (int i = 1; i < title.length; i++) {
                        if (i == 1){
                            for (Track track : itemsBack) {
                                if (track.getTitle().equals(title[i])){
                                    child=track;
                                    break;
                                }
                            }
                        }else {
                            for (Track track : child.getChildren()) {
                                if (track.getTitle().equals(title[i])){
                                    child=track;
                                    break;
                                }
                            }
                        }
                        items.setAll(child.getChildren());
                        currentPath.set(t1);
                    }
                }

            }
        }));
    }

/*    private Pane initFilterPane(){
        ToggleGroup filterBtnToggleGroup=new ToggleGroup();

        ToggleButton allBtn=new ToggleButton("全部");
        allBtn.setToggleGroup(filterBtnToggleGroup);
        allBtn.getStyleClass().addAll("lc-label-btn","lc-btn-dark");

        ToggleButton audioBtn=new ToggleButton("音频");
        audioBtn.setToggleGroup(filterBtnToggleGroup);
        audioBtn.getStyleClass().addAll("lc-label-btn","lc-btn-dark");

        ToggleButton videoBtn=new ToggleButton("视频");
        videoBtn.setToggleGroup(filterBtnToggleGroup);
        videoBtn.getStyleClass().addAll("lc-label-btn","lc-btn-dark");

        ToggleButton picBtn=new ToggleButton("图片");
        picBtn.setToggleGroup(filterBtnToggleGroup);
        picBtn.getStyleClass().addAll("lc-label-btn","lc-btn-dark");

        allBtn.setOnAction(actionEvent -> filter(FolderTableView.ALL));
        audioBtn.setOnAction(actionEvent -> filter(FolderTableView.AUDIO));
        videoBtn.setOnAction(actionEvent -> filter(FolderTableView.VIDEO));
        picBtn.setOnAction(actionEvent -> filter(FolderTableView.PICTURE));

        HBox filterPane=new HBox(allBtn,audioBtn,videoBtn,picBtn);
        filterPane.setPadding(new Insets(10.0,10.0,0.0,0.0));
        filterPane.setSpacing(8.0);
        filterPane.setAlignment(Pos.CENTER_RIGHT);
        return filterPane;
    }*/

    private void initTableView(){
        //rootPath.set(file.getPath());
        //currentPath.set(file.getPath());
        tableView=new TableView<>();
        tableView.getStyleClass().add("folder-table-view");
        tableView.setItems(items);
        TableColumn<Track,String> nameCol = new TableColumn<>("名称");
        nameCol.getStyleClass().add("detail-table-name");
        nameCol.setPrefWidth(550);
        TableColumn<Track, String> typeCol = new TableColumn<>("类型");
        nameCol.getStyleClass().add("detail-table-type");
        typeCol.setPrefWidth(60);
        tableView.getColumns().addAll(typeCol, nameCol);
        nameCol.setCellValueFactory(new PropertyValueFactory<Track, String>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<Track, String>("type"));
        typeCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Track, String> call(TableColumn<Track, String> AsmrFileStringTableColumn) {
                TableCell<Track, String> tableCell = new TableCell<>() {
                    @Override
                    protected void updateItem(String s, boolean b) {
                        if (!b) {
                            ImageView imageView;
                            if (s.equals("folder")){
                                imageView = new ImageView();
                                imageView.setFitHeight(30);
                                imageView.setPreserveRatio(true);
                                imageView.setImage(getFolderImage());
                                setGraphic(imageView);
                            }else if (s.equals("audio")){
                                imageView = new ImageView();
                                imageView.setFitHeight(30);
                                imageView.setPreserveRatio(true);
                                imageView.setImage(getAudioImage());
                                setGraphic(imageView);
                            }else if (s.equals("image")){
                                imageView = new ImageView();
                                imageView.setFitHeight(30);
                                imageView.setPreserveRatio(true);
                                imageView.setImage(getPicImage());
                                setGraphic(imageView);
                            }else {
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
            }
        });

/*        tableView.setSortPolicy(table -> {
            Comparator<AsmrFile> comparator = new Comparator<AsmrFile>() {
                @Override
                public int compare(AsmrFile o1, AsmrFile o2) {
                    if (o1.isDir() && !o2.isDir()) {
                        return -1;
                    }
                    if (!o1.isDir() && o2.isDir()) {
                        return 1;
                    }

                    return o1.getFilename().compareTo(o2.getFilename());
                }
            };
            FXCollections.sort(table.getItems(), comparator);
            return true;
        });*/

        tableView.setRowFactory(view -> {
            TableRow<Track> row = new TableRow<>(){
                @Override
                protected void updateItem(Track AsmrFile, boolean b) {
                    super.updateItem(AsmrFile, b);
                    if (!b) {
                        setOnMouseClicked(mouseEvent -> {
                            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                                if (AsmrFile == null) return;
                                String temp = AsmrFile.getType();
                                if (temp.equals("folder")) {
                                    update(AsmrFile);
                                } else if (temp.equals("audio")) {
                                    List<Audio> list = new ArrayList<>();
                                    int index=0;
                                    int currentTrack=0;
                                    for (Track track : items) {
                                        if (track.getType().equals("audio")){
                                            list.add(new Audio(track.getTitle(), track.getMediaDownloadUrl(), track.getMediaStreamUrl(), track.getStreamLowQualityUrl(), track.getDuration()));
                                            if (track != AsmrFile){
                                                index+=1;
                                            }else {
                                                currentTrack=index;
                                            }
                                        }
                                    }

                                    List<LrcFile> lrcList=new ArrayList<>();
                                    for (Track track : itemsBack) {
                                       lrc(track,lrcList);
                                    }
                                    lrcList.sort(Comparator.comparing(LrcFile::getTitle));


                                    if (lrcList.size() > 0){
                                        LcMediaPlayer.getInstance().setMusic(new Music(work, list,lrcList),currentTrack);
                                    }else {
                                        LcMediaPlayer.getInstance().setMusic(new Music(work, list),currentTrack);
                                    }




                                } else {

                                }
                            }
                        });
                    }

                }
            };


            return row;
        });




    }


    private void lrc(Track track,List<LrcFile> list){
       if (track.getChildren() != null){
           for (Track child : track.getChildren()) {
               lrc(child,list);
           }
       }else {
           if (track.getTitle().toLowerCase().endsWith(".lrc")){
               list.add(new LrcFile(track.getTitle(),track.getMediaDownloadUrl(), LrcType.NET));
           }
       }
    }


    /**
     * @description: 更新表格，同于在点击子目录时调用
     * @name: update
     * @author: Leck
     * @param:	file
     * @return  void
     * @date:   2023/3/12
     */
    public void update(Track track){
        items.setAll(track.getChildren());
        currentPath.set(currentPath.get()+"/"+track.getTitle());
        pathViewPane.setPath(currentPath.get());
        //tableView.sort();
    }
    public void update(){

        //tableView.sort();
    }

/*    public void filter(int filterType){
        if (filterItems == null){
            if (ALL==filterType) return;
            filterItems=FXCollections.observableArrayList();
            filterItems.setAll(tableView.getItems());
        }
        if (ALL==filterType){
            tableView.setItems(filterItems);
            return;
        }

        FilteredList<AsmrFile> filteredData = new FilteredList<>(filterItems, new Predicate<AsmrFile>() {
            @Override
            public boolean test(AsmrFile AsmrFile) {
                if (PICTURE == filterType){
                    return SupportImageFormat.contains(AsmrFile.getType());
                }else if (AUDIO ==filterType){
                    return SupportAudioFormat.contains(AsmrFile.getType());
                }else if (VIDEO==filterType){
                    return SupportVideoFormat.contains(AsmrFile.getType());
                }
                return false;
            }
        });
        tableView.setItems(filteredData);
    }*/

    private Image getAudioImage() {
        if (audioImage == null){
            audioImage=new Image(this.getClass().getResource("/cn/tealc995/asmronline/image/suffix/file-audio.png").toExternalForm());
        }
        return audioImage;
    }

    private Image getPicImage() {
        if (picImage == null){
            picImage=new Image(this.getClass().getResource("/cn/tealc995/asmronline/image/suffix/file-image.png").toExternalForm());
        }
        return picImage;
    }

    private Image getOtherImage() {
        if (otherImage == null){
            otherImage=new Image(this.getClass().getResource("/cn/tealc995/asmronline/image/suffix/file-unknown.png").toExternalForm());
        }
        return otherImage;
    }


    private Image getVideoImage() {
        if (videoImage == null){
            videoImage=new Image(this.getClass().getResource("/cn/tealc995/asmronline/image/suffix/file-video.png").toExternalForm());
        }
        return videoImage;
    }

    public Image getFolderImage() {
        if (folderImage == null){
            folderImage=new Image(this.getClass().getResource("/cn/tealc995/asmronline/image/suffix/file-folder.png").toExternalForm());
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
}