package cn.tealc995.kkmaid.player;

import cn.tealc995.kikoreu.model.Work;
import cn.tealc995.kkmaid.model.Audio;
import cn.tealc995.kkmaid.model.Music;
import cn.tealc995.kkmaid.model.lrc.LrcBean;
import cn.tealc995.kkmaid.model.lrc.LrcFile;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.List;

public interface TeaMediaPlayer {
    void init(int index, boolean autoPlay);
    void setMusic(Music music, int index);
    void pre();
    void next();
    void seek(double time);
    void dispose();
    ObservableList<LrcFile> getLrcFiles();
    void updateLrcFile(List<LrcFile> list, int index);
    void updateLrcFile(List<LrcFile> list);
    Work getWork();
    boolean ready();
    String mainCover();

    String getTitle();
    StringProperty titleProperty();
    ObservableList<LrcBean> getLrcBeans();
    ObjectProperty<ObservableList<LrcBean>> lrcBeansProperty();
    String getArtist();
    StringProperty artistProperty();
    Double getCurrentTime();
    SimpleDoubleProperty currentTimeProperty();
    double getTotalTime();
    DoubleProperty totalTimeProperty();
    boolean isPlaying();
    BooleanProperty playingProperty();
    boolean isDisorder();
    BooleanProperty disorderProperty();
    boolean isLoop();
    BooleanProperty loopProperty();
    boolean isMute();
    BooleanProperty muteProperty();
    double getVolume();
    DoubleProperty volumeProperty();
    Image getAlbum();
    ObjectProperty<Image> albumProperty();
    int getLrcSelectedIndex();
    SimpleIntegerProperty lrcSelectedIndexProperty();
    String getLrcSelectedText();
    SimpleStringProperty lrcSelectedTextProperty();
    boolean isDesktopLrcShow();
    BooleanProperty desktopLrcShowProperty();
    ObservableList<Audio> getSongs();
    Audio getPlayingAudio();
    SimpleObjectProperty<Audio> playingAudioProperty();

    void clearPlayingList();
    int getPlayingIndexInList();

    void removeAudio(Audio audio);
    void removeAudio(int index);
    Double getBufferedTime();
    SimpleDoubleProperty bufferedTimeProperty();
}
