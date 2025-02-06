package cn.tealc995.kkmaid.ui.item;

import cn.tealc995.kkmaid.App;
import cn.tealc995.kkmaid.model.Audio;
import cn.tealc995.kkmaid.model.lrc.LrcFile;
import cn.tealc995.kkmaid.ui.stage.LocalSubtitleStage;
import cn.tealc995.kkmaid.util.CssLoader;
import cn.tealc995.teaFX.controls.notification.MessageType;
import cn.tealc995.teaFX.controls.notification.Notification;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-18 10:03
 */
public class LrcFileDialogUI {
    private LrcFileDialogViewModel viewModel;
    private StackPane root;

    public LrcFileDialogUI() {
        viewModel=new LrcFileDialogViewModel();
        root=new StackPane();
        root.getStylesheets().add(CssLoader.getCss(CssLoader.lrc_file_dialog));


        Label title=new Label("歌词管理");
        title.getStyleClass().add("title-2");
        title.setPadding(new Insets(0.0,0.0,15.0,0.0));

        ListView<Audio> songListView=new ListView<>();
        songListView.setItems(viewModel.getSongs());
        //songListView.setDisable(true);
        songListView.setCellFactory(new Callback<ListView<Audio>, ListCell<Audio>>() {
            @Override
            public ListCell<Audio> call(ListView<Audio> audioListView) {
                ListCell<Audio> cell=new ListCell<>(){
                    @Override
                    protected void updateItem(Audio audio, boolean b) {
                        super.updateItem(audio, b);
                        if (audio != null && !b){
                            setText(audio.getTitle());
                        }else {
                            setText("");
                        }
                    }
                };
                return cell;
            }
        });


        ListView<LrcFile> lrcFileListView=new ListView<>(viewModel.getLrcFiles());
        lrcFileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lrcFileListView.setCellFactory(new Callback<ListView<LrcFile>, ListCell<LrcFile>>() {
            @Override
            public ListCell<LrcFile> call(ListView<LrcFile> lrcFileListView) {
                ListCell<LrcFile> cell=new ListCell<>(){
                    @Override
                    protected void updateItem(LrcFile lrcFile, boolean b) {
                        super.updateItem(lrcFile, b);
                        if (lrcFile != null && !b){
                            setText(lrcFile.getTitle());
                        }else {
                            setText("");
                        }
                    }
                };
                return cell;
            }
        });


        Button readBtn=new Button("查看");
        readBtn.setOnAction(event -> {
            ObservableList<Integer> selectedIndices = lrcFileListView.getSelectionModel().getSelectedIndices();
            if (!selectedIndices.isEmpty()){
                LocalSubtitleStage stage=new LocalSubtitleStage(lrcFileListView.getItems(),lrcFileListView.getSelectionModel().getSelectedIndices().getFirst());
                stage.show();
            }else {
                Notification.show("请先选择要操作的歌词", MessageType.WARNING,Pos.TOP_CENTER,App.mainStage);
            }
        });

        Button addBtn=new Button("添加空项");
        addBtn.setOnAction(event -> {
            ObservableList<Integer> selectedIndices = lrcFileListView.getSelectionModel().getSelectedIndices();
            if (selectedIndices.size() > 0){
                viewModel.add(selectedIndices.get(0));
                lrcFileListView.getSelectionModel().clearAndSelect(selectedIndices.get(0));
            }else {
                viewModel.add(0);
                lrcFileListView.getSelectionModel().clearAndSelect(0);
            }
        });


        Button upBtn=new Button("上移");
        upBtn.setOnAction(event -> {
            ObservableList<Integer> selectedIndices = lrcFileListView.getSelectionModel().getSelectedIndices();
            if (selectedIndices.size() > 1){
                Notification.show("上下移动仅支持单选", MessageType.WARNING,Pos.TOP_CENTER,App.mainStage);
            }else if (selectedIndices.size() == 0){
                Notification.show("请先选择要操作的歌词", MessageType.WARNING,Pos.TOP_CENTER,App.mainStage);
            }else {
                int index= selectedIndices.get(0);
                if (index != -1){
                    lrcFileListView.getSelectionModel().clearAndSelect(viewModel.up(index));
                }
            }
        });
        Button downBtn=new Button("下移");
        downBtn.setOnAction(event -> {
            ObservableList<Integer> selectedIndices = lrcFileListView.getSelectionModel().getSelectedIndices();
            if (selectedIndices.size() > 1){
                Notification.show("上下移动仅支持单选", MessageType.WARNING,Pos.TOP_CENTER,App.mainStage);
            }else if (selectedIndices.size() == 0){
                Notification.show("请先选择要操作的歌词", MessageType.WARNING,Pos.TOP_CENTER,App.mainStage);
            }else {
                int index= selectedIndices.get(0);
                if (index != -1){
                    lrcFileListView.getSelectionModel().clearAndSelect(viewModel.down(index));
                }
            }
        });
        Button deleteBtn=new Button("删除");
        deleteBtn.setOnAction(event -> {
            ObservableList<LrcFile> selectedItems = lrcFileListView.getSelectionModel().getSelectedItems();
            if (selectedItems.size() == 0){
                Notification.show("请先选择要操作的歌词", MessageType.WARNING,Pos.TOP_CENTER,App.mainStage);
            }else {
                viewModel.delete(selectedItems);
            }
        });
        VBox controlPane=new VBox(readBtn,addBtn,deleteBtn,upBtn,downBtn);
        controlPane.setSpacing(10.0);


        Label songTitle=new Label("音声");
        songTitle.setAlignment(Pos.CENTER);
        songTitle.getStyleClass().add("list-title");
        Label lrcTitle=new Label("字幕(支持多选)");
        lrcTitle.getStyleClass().add("list-title");
        lrcTitle.setAlignment(Pos.CENTER);


        GridPane gridPane=new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10.0);
        gridPane.setVgap(10);
        gridPane.add(songTitle,0,0);
        gridPane.add(lrcTitle,1,0);

        gridPane.add(songListView,0,1);
        gridPane.add(lrcFileListView,1,1);
        gridPane.add(controlPane,2,1);

        Button okBtn=new Button("更新");
        okBtn.getStyleClass().add("accent");
        okBtn.setOnAction(event -> {
            if (viewModel.getSongs().size() != viewModel.getLrcFiles().size()){
                Notification.show("歌词数量需与歌曲数量一致且对应", MessageType.WARNING,2000,Pos.TOP_CENTER,App.mainStage);
            }else {
                viewModel.update();
            }

        });
        Button cancelBtn=new Button("取消");
        cancelBtn.setOnAction(event -> viewModel.cancel());
        HBox functionPane=new HBox(okBtn,cancelBtn);
        functionPane.setAlignment(Pos.CENTER);
        functionPane.setSpacing(20.0);
        functionPane.setPadding(new Insets(10));


        BorderPane parent=new BorderPane();
        parent.setTop(title);
        parent.setCenter(gridPane);
        parent.setBottom(functionPane);

        root.getChildren().add(parent);
        root.getStyleClass().add("background");
        root.setPadding(new Insets(20));


    }

    public StackPane getRoot() {
        return root;
    }
}