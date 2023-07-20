package cn.tealc995.asmronline.ui.item;

import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.MainDialogEvent;
import cn.tealc995.asmronline.model.Audio;
import cn.tealc995.asmronline.model.lrc.LrcFile;
import cn.tealc995.asmronline.model.lrc.LrcType;
import cn.tealc995.asmronline.player.LcMediaPlayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-18 10:03
 */
public class LrcFileDialogViewModel {
    private ObservableList<Audio> songs;
    private ObservableList<LrcFile> lrcFiles;
    public LrcFileDialogViewModel() {
        songs = FXCollections.observableArrayList(LcMediaPlayer.getInstance().getSongs());
        lrcFiles = FXCollections.observableArrayList(LcMediaPlayer.getInstance().getLrcFiles());
    }


    public int up(int index){
        int i;
        if (index == 0){
          i=lrcFiles.size()-1;
        }else{
          i=index-1;
        }
        Collections.swap(lrcFiles,index,i);
        return i;
    }

    public int down(int index){
        int i;
        if (index == lrcFiles.size()-1){
            i=0;
        }else{
            i=index+1;
        }
        Collections.swap(lrcFiles,index,i);
        return i;
    }
    public void delete(List<LrcFile> list){
        lrcFiles.removeAll(list);
    }


    public void add(int index){
        lrcFiles.add(index,new LrcFile("空项","", LrcType.EMPTY));
    }

    public void update(){
        LcMediaPlayer.getInstance().updateLrcFile(lrcFiles);
        cancel();
    }

    public void cancel(){
        EventBusUtil.getDefault().post(new MainDialogEvent(null));
    }
    public ObservableList<Audio> getSongs() {
        return songs;
    }

    public ObservableList<LrcFile> getLrcFiles() {
        return lrcFiles;
    }
}