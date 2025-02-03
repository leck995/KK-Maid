package cn.tealc995.kkmaid.ui.item;

import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainDialogEvent;
import cn.tealc995.kkmaid.model.lrc.LrcFile;
import cn.tealc995.kkmaid.model.lrc.LrcType;
import cn.tealc995.kkmaid.player.MediaPlayerUtil;
import cn.tealc995.kkmaid.zip.NewZipUtil;
import cn.tealc995.kkmaid.zip.ZipEntityFile;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-17 05:52
 */
public class LrcZipDialogViewModel {
    private static final Logger LOG = LoggerFactory.getLogger(LrcZipDialogViewModel.class);
    private SimpleStringProperty title;
    private SimpleObjectProperty<File> lrcFile;
    private SimpleObjectProperty<ZipEntityFile> rootItem;

    public LrcZipDialogViewModel(File zipFile) {
        lrcFile=new SimpleObjectProperty<>();
        title=new SimpleStringProperty();
        rootItem=new SimpleObjectProperty<>();
        lrcFile.addListener((observableValue, file, t1) -> importZipFile(t1));
        lrcFile.set(zipFile);
    }



    private void importZipFile(File file){
        try {
            List<ZipEntityFile> list = NewZipUtil.getAllFile(file);
            ZipEntityFile zipEntityFile = new ZipEntityFile("ROOT", list);
            zipEntityFile.setType("folder");
            rootItem.set(zipEntityFile);
            title.set(file.getName());
        } catch (IOException e) {
            LOG.error("读取ZIP出现错误",e);
        }
    }

    /**
     * @description: 加载选定的歌词，并设置MediaPlayer的歌词列表
     * @name: importLrcFile
     * @author: Leck
     * @param:	parent
     * @param:	index
     * @return  void
     * @date:   2023/7/18
     */
    public void importLrcFile(ZipEntityFile parent,int index){
        List<LrcFile> lrcFiles=new ArrayList<>();
        for (ZipEntityFile item : parent.getChildren()) {
            if (item.isLrc()){
                lrcFiles.add(new LrcFile(item.getName(), item.getPath(), LrcType.ZIP,lrcFile.get().getPath()));
            }
        }
        MediaPlayerUtil.mediaPlayer().updateLrcFile(lrcFiles,index);
        cancel();



    }

    public void cancel(){
        EventBusUtil.getDefault().post(new MainDialogEvent(null));
    }


    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setLrcFile(File lrcFile) {
        this.lrcFile.set(lrcFile);
    }

    public ZipEntityFile getRootItem() {
        return rootItem.get();
    }

    public SimpleObjectProperty<ZipEntityFile> rootItemProperty() {
        return rootItem;
    }
}