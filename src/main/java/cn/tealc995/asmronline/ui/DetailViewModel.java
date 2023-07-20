package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.api.TrackApi;
import cn.tealc995.asmronline.api.model.Track;
import cn.tealc995.asmronline.api.model.Work;
import cn.tealc995.asmronline.model.Audio;
import cn.tealc995.asmronline.model.Music;
import cn.tealc995.asmronline.player.LcMediaPlayer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

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

    public DetailViewModel(Work work) {
        this.work=new SimpleObjectProperty<>(work);
        tracks= FXCollections.observableArrayList(TrackApi.track(Config.HOST.get(), work.getId()));
        poster=new SimpleObjectProperty<>(new Image(work.getMainCoverUrl(),400,300,true,true,true));
        title=new SimpleStringProperty(work.getTitle());
        id=new SimpleStringProperty(work.getId());
        data=new SimpleStringProperty(work.getRelease());

    }



    public void play(){
        List<Audio> list=new ArrayList<>();
        for (Track track : tracks) {
            list.add(new Audio(track.getTitle(),track.getMediaDownloadUrl(),track.getMediaStreamUrl(),track.getStreamLowQualityUrl(),track.getDuration()));
        }

        LcMediaPlayer.getInstance().setMusic(new Music(work.get(),list),0);
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
}