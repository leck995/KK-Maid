package cn.tealc995.kkmaid.ui.item;

import cn.tealc995.kkmaid.ui.cell.FileTreeItem;
import cn.tealc995.kkmaid.util.AnchorPaneUtil;
import cn.tealc995.kkmaid.util.CssLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.io.File;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-18 14:56
 */
public class LrcFolderDialogUI {
    private final TreeView<File> treeView;
    private StackPane root;
    private LrcFolderDialogViewModel viewModel;

    public LrcFolderDialogUI(File folder) {
        root=new StackPane();
        root.getStylesheets().add(CssLoader.getCss(CssLoader.lrc_zip_dialog));
        viewModel=new LrcFolderDialogViewModel(folder);

        Label title=new Label("导入字幕");
        title.getStyleClass().add("title-2");

        FileTreeItem rootTreeItem=new FileTreeItem(folder);
        treeView = new TreeView<>(rootTreeItem);

        treeView.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
            @Override
            public TreeCell<File> call(TreeView<File> fileTreeView) {
                TreeCell<File> cell=new TreeCell<>(){
                    @Override
                    protected void updateItem(File file, boolean b) {
                        super.updateItem(file, b);
                        if (file !=null && !b){
                            setText(file.getName());
                            Region region=new Region();
                            setGraphicTextGap(10);
                            setGraphic(region);
                            if (file.isDirectory()){
                                region.getStyleClass().add("folder");
                            }else if (isLrc(file)){
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
                return cell;
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

        AnchorPane anchorPane=new AnchorPane(title,treeView,functionPane);
        AnchorPaneUtil.setPosition(title,0.0,null,null,0.0);

        AnchorPaneUtil.setPosition(treeView,40.0,0.0,60.0,0.0);
        AnchorPaneUtil.setPosition(functionPane,null,0.0,0.0,0.0);

        root.setPrefSize(550,650);
        root.setPadding(new Insets(15.0));
        root.getChildren().addAll(anchorPane);
        root.getStyleClass().add("background");


    }


    private boolean isLrc(File file){
        return file.getName().toLowerCase().endsWith(".lrc");
    }
    private void getLrc(){
        File child = treeView.getSelectionModel().getSelectedItem().getValue();
        if (isLrc(child)){
            File parent = treeView.getSelectionModel().getSelectedItem().getParent().getValue();

            File[] files = parent.listFiles(file -> file.isFile() && file.getName().toLowerCase().endsWith(".lrc"));
            int index=0;
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().equals(child.getName())){
                    index=i;
                }
            }

            viewModel.update(parent,index);
        }
    }

    public StackPane getRoot() {
        return root;
    }
}