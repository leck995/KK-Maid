package cn.tealc995.asmronline.ui.item;

import cn.tealc995.asmronline.App;
import cn.tealc995.asmronline.ui.cell.ZipTreeItem;
import cn.tealc995.asmronline.util.AnchorPaneUtil;
import cn.tealc995.asmronline.util.CssLoader;
import cn.tealc995.asmronline.zip.ZipEntityFile;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-17 05:52
 */
public class LrcZipDialogUI {
    private LrcZipDialogViewModel viewModel;
    private StackPane root;
    private TreeView<ZipEntityFile> treeView;

    public LrcZipDialogUI(File zipFile) {
        viewModel=new LrcZipDialogViewModel(zipFile);
        root=new StackPane();
        root.getStylesheets().add(CssLoader.getCss(CssLoader.lrc_zip_dialog));

        Label title=new Label();
        title.getStyleClass().add("title-2");
        title.textProperty().bind(viewModel.titleProperty());
        Button importBtn=new Button("导入");
        importBtn.setOnAction(event -> {
            FileChooser fileChooser=new FileChooser();
            fileChooser.setTitle("选择字幕包");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("压缩包","*.zip","*.ZIP"));
            File file = fileChooser.showOpenDialog(App.mainStage);
            if (file != null){
                viewModel.setLrcFile(file);
            }
        });


        treeView=new TreeView<>();
        ZipTreeItem rootCell=new ZipTreeItem(viewModel.getRootItem());
        rootCell.valueProperty().bind(viewModel.rootItemProperty());
        treeView.setRoot(rootCell);
        treeView.setCellFactory(new Callback<TreeView<ZipEntityFile>, TreeCell<ZipEntityFile>>() {
            @Override
            public TreeCell<ZipEntityFile> call(TreeView<ZipEntityFile> zipEntityFileTreeView) {
                TreeCell<ZipEntityFile>  treeCell=new TreeCell<ZipEntityFile>(){
                    @Override
                    protected void updateItem(ZipEntityFile zipEntityFile, boolean b) {
                        super.updateItem(zipEntityFile, b);
                        if (zipEntityFile !=null && !b){
                            setText(zipEntityFile.getName());
                            Region region=new Region();
                            setGraphicTextGap(10);
                            setGraphic(region);
                            if (zipEntityFile.isFolder()){
                                region.getStyleClass().add("folder");
                            }else if (zipEntityFile.isLrc()){
                                region.getStyleClass().add("lrc");
                                setOnMouseClicked(mouseEvent -> getLrc());
                            }else {
                                region.getStyleClass().add("unknown");
                                setDisable(true);
                            }
                        }else {
                            setText("");
                            setGraphic(null);
                        }
                    }
                };
                return treeCell;
            }
        });

        Button okBtn=new Button("确定");
        okBtn.getStyleClass().add("accent");
        okBtn.setOnAction(event -> getLrc());
        Button cancelBtn=new Button("取消");
        cancelBtn.setOnAction(event -> viewModel.cancel());
        HBox functionPane=new HBox(okBtn,cancelBtn);
        functionPane.setAlignment(Pos.CENTER);
        functionPane.setSpacing(20.0);

        AnchorPane anchorPane=new AnchorPane(title,importBtn,treeView,functionPane);
        AnchorPaneUtil.setPosition(title,0.0,null,null,0.0);
        AnchorPaneUtil.setPosition(importBtn,0.0,0.0,null,null);
        AnchorPaneUtil.setPosition(treeView,40.0,0.0,60.0,0.0);
        AnchorPaneUtil.setPosition(functionPane,null,0.0,0.0,0.0);

        root.setPrefSize(550,650);
        root.setPadding(new Insets(15.0));
        root.getChildren().addAll(anchorPane);
        root.getStyleClass().add("background");




    }



    private void getLrc(){
        ZipEntityFile child = treeView.getSelectionModel().getSelectedItem().getValue();
        if (child.isLrc()){
            ZipEntityFile parent = treeView.getSelectionModel().getSelectedItem().getParent().getValue();
            int index = parent.getChildren().indexOf(child);
            viewModel.importLrcFile(parent,index);
        }
    }

    public StackPane getRoot() {
        return root;
    }
}