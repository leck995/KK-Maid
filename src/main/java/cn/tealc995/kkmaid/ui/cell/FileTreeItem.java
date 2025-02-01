package cn.tealc995.kkmaid.ui.cell;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-17 05:58
 */
public class FileTreeItem extends TreeItem<File> {

    private boolean ready=false;


    public FileTreeItem(File file) {
        super(file);
        valueProperty().addListener((observableValue, file1, t1) -> {
            ready=false;
            setExpanded(false);
        });
    }

    @Override
    public boolean isLeaf() {
        return !getValue().isDirectory();
    }


    @Override
    public ObservableList<TreeItem<File>> getChildren() {
        if (!ready){
            File[] files = getValue().listFiles();
            ObservableList<TreeItem<File>> list= FXCollections.observableArrayList();
            if (files != null){
                for (File child : files) {
                    list.add(new FileTreeItem(child));
                }
                super.getChildren().setAll(list);
                ready=true;
            }
        }
        return  super.getChildren();
    }
}