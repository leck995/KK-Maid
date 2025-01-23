package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.api.TrackApi;
import cn.tealc995.asmronline.api.model.Track;
import cn.tealc995.asmronline.api.model.Work;
import cn.tealc995.asmronline.api.model.playList.PlayList;
import cn.tealc995.asmronline.api.model.playList.PlayListRemoveWork;
import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.MainNotificationEvent;
import cn.tealc995.asmronline.model.Audio;
import cn.tealc995.asmronline.model.Music;
import cn.tealc995.asmronline.player.LcMediaPlayer;
import cn.tealc995.asmronline.player.MediaPlayerUtil;
import cn.tealc995.asmronline.service.PlayListRemoveWorkService;
import cn.tealc995.asmronline.service.PlayListWorkExistService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 21:20
 */
public class DetailViewModel {
    private ObservableList<Track> tracks;
    private SimpleObjectProperty<Work> work;

    private SimpleObjectProperty<Image> poster;
    private SimpleStringProperty title;
    private SimpleStringProperty id;
    private SimpleStringProperty data;

    private ObservableList<PlayList> playLists;
    private PlayListWorkExistService playListWorkExistService;
    private PlayListRemoveWorkService playListRemoveWorkService;

    private SimpleObjectProperty<Charset> charset = new SimpleObjectProperty<>(Charset.forName("GBK"));

    public DetailViewModel(Work work) {
        this.work=new SimpleObjectProperty<>(work);
        tracks= FXCollections.observableArrayList(TrackApi.track(Config.HOST.get(), work.getId()));
        poster=new SimpleObjectProperty<>(new Image(work.getMainCoverUrl(),400,300,true,true,true));
        title=new SimpleStringProperty(work.getTitle());
        id=new SimpleStringProperty(work.getId());
        data=new SimpleStringProperty(work.getRelease());
        playLists=FXCollections.observableArrayList();


    }



    public void play(){
        List<Audio> list=new ArrayList<>();
        for (Track track : tracks) {
            list.add(new Audio(track.getTitle(),track.getMediaDownloadUrl(),track.getMediaStreamUrl(),track.getStreamLowQualityUrl(),track.getDuration()));
        }

        MediaPlayerUtil.mediaPlayer().setMusic(new Music(work.get(),list),0);
    }


    public void getPlayList(){
        if (playLists.size() > 0)
            return;


        if (playListWorkExistService ==null){
            playListWorkExistService=new PlayListWorkExistService();
            playListWorkExistService.valueProperty().addListener((observableValue, mainPlayList, t1) -> {
                if (t1!=null){
                    playLists.setAll(t1.getPlayLists());
                }
            });
        }

        Map<String,String> params=new HashMap<>();
        params.put("version", "2");
        params.put("workID",work.get().getId());

        playListWorkExistService.setHost(Config.HOST.get());
        playListWorkExistService.setParams(params);
        playListWorkExistService.restart();

    }


    public void updatePlayListWork(PlayList playList,boolean remove){
        if (playListRemoveWorkService ==null){
            playListRemoveWorkService=new PlayListRemoveWorkService();
            playListRemoveWorkService.valueProperty().addListener((observableValue, mainPlayList, t1) -> {
                if (t1!=null && t1){
                    EventBusUtil.getDefault().post(new MainNotificationEvent("修改成功"));
                }
            });
        }

        PlayListRemoveWork playListRemoveWork=new PlayListRemoveWork(playList.getId(),List.of(work.get().getId()),remove);


        playListRemoveWorkService.setUrl(Config.HOST.get());
        playListRemoveWorkService.setWork(playListRemoveWork);
        playListRemoveWorkService.restart();


    }


    public ObservableList<PlayList> getPlayLists() {
        return playLists;
    }

    public ObservableList<Track> getTracks() {
        return tracks;
    }

    public Work getWork() {
        return work.get();
    }

    public SimpleObjectProperty<Work> workProperty() {
        return work;
    }

    public Image getPoster() {
        return poster.get();
    }

    public SimpleObjectProperty<Image> posterProperty() {
        return poster;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public String getData() {
        return data.get();
    }

    public SimpleStringProperty dataProperty() {
        return data;
    }

    public Charset getCharset() {
        return charset.get();
    }

    public SimpleObjectProperty<Charset> charsetProperty() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset.set(charset);
    }
}