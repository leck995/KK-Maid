package cn.tealc995.kkmaid.ui.item;

import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainDialogEvent;
import cn.tealc995.kkmaid.model.lrc.LrcFile;
import cn.tealc995.kkmaid.model.lrc.LrcType;
import cn.tealc995.kkmaid.player.MediaPlayerUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-18 14:56
 */
public class LrcFolderDialogViewModel {
    private ObservableList<File> items;
    private SimpleObjectProperty<File> folder;
    public LrcFolderDialogViewModel(File file) {
        items= FXCollections.observableArrayList();
        folder=new SimpleObjectProperty<>();
        folder.addListener((observableValue, file1, t1) -> {
            importFolder(t1);
        });
        folder.set(file);
    }

    private void importFolder(File folder){
        File[] files = folder.listFiles();
        items.addAll(files);
    }

    public void update(File file,int index){
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().toLowerCase().endsWith(".lrc");
            }
        });
        List<LrcFile> lrcFiles=new ArrayList<>();
        for (File file1 : files) {
            lrcFiles.add(new LrcFile(file1.getName(),file1.getPath(), LrcType.FOLDER));
        }
        MediaPlayerUtil.mediaPlayer().updateLrcFile(lrcFiles,index);
        cancel();
    }

    public void cancel(){
        EventBusUtil.getDefault().post(new MainDialogEvent(null));
    }

    public ObservableList<File> getItems() {
        return items;
    }
}