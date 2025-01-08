package cn.tealc995.asmronline.ui.cell;

import cn.tealc995.asmronline.api.model.Track;
import cn.tealc995.asmronline.zip.ZipEntityFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;

import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-17 05:58
 */
public class DownloadTreeItem extends CheckBoxTreeItem<Track> {

    private boolean ready=false;


    public DownloadTreeItem(Track track) {
        super(track);
        valueProperty().addListener((observableValue, zipEntityFile1, t1) -> {
            ready=false;
            setExpanded(false);
        });

    }

    @Override
    public boolean isLeaf() {
        return !getValue().getType().equals("folder");
    }


    @Override
    public ObservableList<TreeItem<Track>> getChildren() {
        if (!ready){
            List<Track> children = getValue().getChildren();
            ObservableList<TreeItem<Track>> list= FXCollections.observableArrayList();
            if (children != null){
                for (Track child : children) {
                    list.add(new DownloadTreeItem(child));
                }
                super.getChildren().setAll(list);
                ready=true;
            }
        }
        return  super.getChildren();
    }


    public String getPath(){
        StringBuilder path=new StringBuilder();
        return getAbsolutePath(this,path);
    }


    private String getAbsolutePath(TreeItem treeItem,StringBuilder path){
        TreeItem parent = treeItem.getParent();
        if (parent!=null){
            Track value = (Track) treeItem.getValue();
            path.insert(0,value.getTitle()).insert(0,"/");
            return getAbsolutePath(parent,path);
        }else{
            return path.toString();
        }
    }
}