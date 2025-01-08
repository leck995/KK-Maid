package cn.tealc995.asmronline.ui.cell;

import cn.tealc995.asmronline.api.model.SortType;
import cn.tealc995.asmronline.zip.ZipEntityFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Region;

import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-17 05:58
 */
public class ZipTreeItem extends TreeItem<ZipEntityFile> {

    private boolean ready=false;


    public ZipTreeItem(ZipEntityFile zipEntityFile) {
        super(zipEntityFile);
        valueProperty().addListener((observableValue, zipEntityFile1, t1) -> {
            ready=false;
            setExpanded(false);
        });
    }

    @Override
    public boolean isLeaf() {
        return !getValue().isFolder();
    }


    @Override
    public ObservableList<TreeItem<ZipEntityFile>> getChildren() {
        if (!ready){
            List<ZipEntityFile> children = getValue().getChildren();
            ObservableList<TreeItem<ZipEntityFile>> list= FXCollections.observableArrayList();
            if (children != null){
                for (ZipEntityFile child : children) {
                    list.add(new ZipTreeItem(child));
                }
                super.getChildren().setAll(list);
                ready=true;
            }
        }
        return  super.getChildren();
    }
}