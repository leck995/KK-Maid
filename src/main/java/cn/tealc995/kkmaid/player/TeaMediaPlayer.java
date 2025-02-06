package cn.tealc995.kkmaid.player;

import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.Work;
import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainNotificationEvent;
import cn.tealc995.kkmaid.model.Audio;
import cn.tealc995.kkmaid.model.Music;
import cn.tealc995.kkmaid.model.lrc.LrcBean;
import cn.tealc995.kkmaid.model.lrc.LrcFile;
import cn.tealc995.kkmaid.model.lrc.LrcType;
import cn.tealc995.kkmaid.service.subtitle.SeekSubtitleFileService;
import cn.tealc995.kkmaid.service.subtitle.beans.SubtitleBeansBaseTask;
import cn.tealc995.kkmaid.service.subtitle.beans.SubtitleBeansByFolderTask;
import cn.tealc995.kkmaid.service.subtitle.beans.SubtitleBeansByNetTask;
import cn.tealc995.kkmaid.service.subtitle.beans.SubtitleBeansByZipTask;
import cn.tealc995.kkmaid.ui.component.DesktopLrcDialog;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public abstract class TeaMediaPlayer {
    private static final Logger LOG = LoggerFactory.getLogger(TeaMediaPlayer.class);
    protected final SimpleObjectProperty<Music> music;
    protected ObservableList<Audio> songs;//播放列表
    protected ObservableList<LrcFile> lrcFiles;
    protected final SimpleObjectProperty<Audio> playingAudio;
    protected final BooleanProperty playing;
    protected final BooleanProperty disorder;
    protected final BooleanProperty loop;
    protected final BooleanProperty isStar;
    protected final BooleanProperty mute;
    protected final BooleanProperty desktopLrcShow;
    protected final SimpleDoubleProperty currentTime;
    protected final SimpleDoubleProperty totalTime;
    protected final SimpleDoubleProperty bufferedTime;
    protected final ObjectProperty<Image> album;
    protected final DoubleProperty volume;
    protected final StringProperty title;
    protected final StringProperty artist;

    protected final ObjectProperty<ObservableList<LrcBean>> lrcBeans;//歌词列
    protected final SimpleIntegerProperty lrcSelectedIndex;
    protected final SimpleStringProperty lrcSelectedText;

    protected int index = 0;
    protected SeekSubtitleFileService seekSubtitleFileService;


    public TeaMediaPlayer() {
        music = new SimpleObjectProperty<>();
        playingAudio = new SimpleObjectProperty<>();

        playing = new SimpleBooleanProperty(false);//
        disorder = new SimpleBooleanProperty(false); //
        loop = new SimpleBooleanProperty(false);
        isStar = new SimpleBooleanProperty(false);
        mute = new SimpleBooleanProperty(false);//
        album = new SimpleObjectProperty<Image>(new Image(this.getClass().getResource("/cn/tealc995/kkmaid/image/album.jpg").toExternalForm()));
        title = new SimpleStringProperty("音乐随心");
        artist = new SimpleStringProperty("Pure");

        totalTime = new SimpleDoubleProperty(0.0);
        currentTime = new SimpleDoubleProperty(0.0);
        bufferedTime = new SimpleDoubleProperty(0.0);
        volume = new SimpleDoubleProperty(100.0);
        songs = FXCollections.observableArrayList();
        lrcFiles = FXCollections.observableArrayList();
        lrcBeans = new SimpleObjectProperty<>(FXCollections.observableArrayList());
        lrcSelectedIndex = new SimpleIntegerProperty(0);
        lrcSelectedText = new SimpleStringProperty("无台词");
        desktopLrcShow = new SimpleBooleanProperty(false);

        disorder.addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                Collections.shuffle(songs);
            } else {
                Collections.sort(songs, (o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
            }
        });

        music.addListener((_, _, t1) -> {
            if (t1 != null) {
                if (lrcBeans.get() != null) {
                    lrcBeans.get().clear();
                    lrcSelectedText.set("当前无字幕");
                }
                songs.setAll(t1.getAudios());
                if (t1.getLrcFiles() != null) {
                    lrcFiles.setAll(t1.getLrcFiles());
                } else {
                    if (!lrcFiles.isEmpty())
                        lrcFiles.clear();
                }
                init(index, true);
            }
        });

        playingAudio.addListener((_, _, newAudio) -> {
            if (newAudio != null) {
                changeSubtitle(newAudio.getTitleWithoutSuffix());
            }
        });

        desktopLrcShow.addListener((_, _, t1) -> DesktopLrcDialog.getInstance().setVisible(t1));



        //对歌词进行选择
        currentTime.addListener((_, _, t1) -> {
            if (t1 == null || !isDesktopLrcShow()) return;//无歌词时,桌面歌词不显示时不调用
            if (!lrcBeans.get().isEmpty()) {
                long millis = (long) t1.doubleValue() * 1000;
                if (millis < lrcBeans.get().getFirst().getLongTime()) {
                    lrcSelectedIndex.set(0);
                    if (lrcBeans.get().getFirst().getTransText() == null) {
                        lrcSelectedText.set(lrcBeans.get().getFirst().getRowText());
                    } else {
                        lrcSelectedText.set(lrcBeans.get().getFirst().getRowText() + "\n" + lrcBeans.get().getFirst().getTransText());
                    }
                }
                int size = lrcBeans.get().size() - 1;
                for (int i = 0; i < size; i++) {
                    if (millis > lrcBeans.get().get(i).getLongTime() && millis < lrcBeans.get().get(i + 1).getLongTime()) {
                        lrcSelectedIndex.set(i);
                        if (lrcBeans.get().get(i).getTransText() == null) {
                            lrcSelectedText.set(lrcBeans.get().get(i).getRowText());
                        } else {
                            lrcSelectedText.set(lrcBeans.get().get(i).getRowText() + "\n" + lrcBeans.get().get(i).getTransText());
                        }
                    }
                }
                if (millis > lrcBeans.get().get(size).getLongTime()) {
                    lrcSelectedIndex.set(size);
                    if (lrcBeans.get().get(size).getTransText() == null) {
                        lrcSelectedText.set(lrcBeans.get().get(size).getRowText());
                    } else {
                        lrcSelectedText.set(lrcBeans.get().get(size).getRowText() + "\n" + lrcBeans.get().get(size).getTransText());
                    }
                }
            }
        });

        seekSubtitleFileService = new SeekSubtitleFileService();
        seekSubtitleFileService.valueProperty().addListener((_, _, t1) -> {
            if (t1 != null) {
                updateLrcFile(t1);
                EventBusUtil.getDefault().post(new MainNotificationEvent("检测到本地字幕文件"));
            }
        });
    }

    public abstract void init(int index, boolean autoPlay);

    public abstract void seek(double time);

    public abstract void dispose();

    public abstract boolean ready();



    public void setMusic(Music music, int index) {
        this.index = index;
        this.music.set(music);
        if (music.getLrcFiles() == null || Config.setting.isLrcPriority()) {
            if (music.getWork().hasLanguages()) {
                seekSubtitleFileService.setIds(music.getWork().getAllId());
            } else {
                seekSubtitleFileService.setId(music.getWork().getFullId());
            }
            seekSubtitleFileService.restart();
        }
    }


    public void pre() {
        if (songs.isEmpty()) return;
        if (index == 0) {
            index = songs.size() - 1;
            init(index, true);
        } else {
            index--;
            init(index, true);
        }
    }

    public void next() {
        if (songs.isEmpty()) return;
        if (index == songs.size() - 1) {
            init(0, true);
            index = 0;
        } else {
            index++;
            init(index, true);
        }
    }

    public void updateLrcFile(List<LrcFile> list, int index) {
        lrcFiles.setAll(list);
        loadSubtitleFile(list.get(index));
    }

    public void updateLrcFile(List<LrcFile> list) {
        lrcFiles.setAll(list);
        loadSubtitleFile(list.get(index));
    }

    public Work getWork() {
        if (music.get() != null) {
            return music.get().getWork();
        } else {
            return null;
        }
    }

    public String mainCover() {
        if (ready()) {
            return music.get().getWork().getMainCoverUrl();
        } else {
            return null;
        }
    }

    public int getPlayingIndexInList() {
        return songs.indexOf(getPlayingAudio());
    }
    public void clearPlayingList() {
        dispose();
        songs.clear();
        lrcBeans.get().clear();
        lrcFiles.clear();
        music.set(null);
    }

    public void removeAudio(int index) {
        if (songs.size() == 1) {
            clearPlayingList();
        } else {
            if (index == this.index) {
                next();
                this.index--;
            } else if (index < this.index) {
                this.index--;
            }
            songs.remove(index);
        }
    }

    protected void loadSubtitleFile(LrcFile lrcFile) {
        SubtitleBeansBaseTask task = null;
        if (lrcFile.getType() == LrcType.NET) {
            task = new SubtitleBeansByNetTask(lrcFile.getPath());
        } else if (lrcFile.getType() == LrcType.FOLDER) {
            task = new SubtitleBeansByFolderTask(lrcFile.getPath());
        } else if (lrcFile.getType() == LrcType.ZIP) {
            task = new SubtitleBeansByZipTask(lrcFile);
        }
        if (task != null) {
            task.setOnSucceeded(workerStateEvent -> {
                SubtitleBeansBaseTask source = (SubtitleBeansBaseTask) workerStateEvent.getSource();
                ResponseBody<List<LrcBean>> value = source.getValue();
                if (value.isSuccess()) {
                    List<LrcBean> list = value.getData();
                    if (list != null) {
                        //移除黑名单数据
                        list.removeIf(lrcBean -> {
                            String row = lrcBean.getRowText();
                            for (String s : Config.blackList.getTextBlackList()) {
                                if (row.contains(s)) {
                                    return true;
                                }
                            }
                            return false;
                        });
                        //加载到lrcBeans中
                        lrcBeans.set(FXCollections.observableArrayList(list));
                    } else {
                        lrcBeans.get().clear();
                        lrcSelectedText.set("当前无字幕");
                    }
                }
            });
            Thread.startVirtualThread(task);
        }
    }


    /**
     * @return void
     * @description: 切换字幕
     * @param: subtitleTitle
     * @date: 2025/2/3
     */
    protected void changeSubtitle(String subtitleTitle) {
        if (!lrcFiles.isEmpty()) {
            List<LrcFile> list = lrcFiles.stream()
                    .filter(lrcFile -> lrcFile.getTitleWithoutSuffix().equals(subtitleTitle)).toList();
            if (!list.isEmpty()) {//当有名称对应的歌词时
                loadSubtitleFile(list.getFirst());
            } else {//如果没有匹配的歌词，
                if (lrcFiles.size() == songs.size()) { //判断歌词列表size和歌曲列表size，相同则获取对应index的歌词。
                    loadSubtitleFile(lrcFiles.get(index));
                } else {
                    lrcBeans.get().clear();
                    lrcSelectedText.set("当前无字幕");
                }
            }
        }
    }


    public Music getMusic() {
        return music.get();
    }

    public SimpleObjectProperty<Music> musicProperty() {
        return music;
    }

    public ObservableList<Audio> getSongs() {
        return songs;
    }

    public void setSongs(ObservableList<Audio> songs) {
        this.songs = songs;
    }

    public ObservableList<LrcFile> getLrcFiles() {
        return lrcFiles;
    }

    public void setLrcFiles(ObservableList<LrcFile> lrcFiles) {
        this.lrcFiles = lrcFiles;
    }

    public Audio getPlayingAudio() {
        return playingAudio.get();
    }

    public SimpleObjectProperty<Audio> playingAudioProperty() {
        return playingAudio;
    }

    public void setPlayingAudio(Audio playingAudio) {
        this.playingAudio.set(playingAudio);
    }

    public boolean isPlaying() {
        return playing.get();
    }

    public BooleanProperty playingProperty() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing.set(playing);
    }

    public boolean isDisorder() {
        return disorder.get();
    }

    public BooleanProperty disorderProperty() {
        return disorder;
    }

    public void setDisorder(boolean disorder) {
        this.disorder.set(disorder);
    }

    public boolean isLoop() {
        return loop.get();
    }

    public BooleanProperty loopProperty() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop.set(loop);
    }

    public boolean isIsStar() {
        return isStar.get();
    }

    public BooleanProperty isStarProperty() {
        return isStar;
    }

    public void setIsStar(boolean isStar) {
        this.isStar.set(isStar);
    }

    public boolean isMute() {
        return mute.get();
    }

    public BooleanProperty muteProperty() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute.set(mute);
    }

    public boolean isDesktopLrcShow() {
        return desktopLrcShow.get();
    }

    public BooleanProperty desktopLrcShowProperty() {
        return desktopLrcShow;
    }

    public void setDesktopLrcShow(boolean desktopLrcShow) {
        this.desktopLrcShow.set(desktopLrcShow);
    }

    public double getCurrentTime() {
        return currentTime.get();
    }

    public SimpleDoubleProperty currentTimeProperty() {
        return currentTime;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime.set(currentTime);
    }

    public double getTotalTime() {
        return totalTime.get();
    }

    public SimpleDoubleProperty totalTimeProperty() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime.set(totalTime);
    }

    public double getBufferedTime() {
        return bufferedTime.get();
    }

    public SimpleDoubleProperty bufferedTimeProperty() {
        return bufferedTime;
    }

    public void setBufferedTime(double bufferedTime) {
        this.bufferedTime.set(bufferedTime);
    }

    public Image getAlbum() {
        return album.get();
    }

    public ObjectProperty<Image> albumProperty() {
        return album;
    }

    public void setAlbum(Image album) {
        this.album.set(album);
    }

    public double getVolume() {
        return volume.get();
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume.set(volume);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getArtist() {
        return artist.get();
    }

    public StringProperty artistProperty() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist.set(artist);
    }

    public ObservableList<LrcBean> getLrcBeans() {
        return lrcBeans.get();
    }

    public ObjectProperty<ObservableList<LrcBean>> lrcBeansProperty() {
        return lrcBeans;
    }

    public void setLrcBeans(ObservableList<LrcBean> lrcBeans) {
        this.lrcBeans.set(lrcBeans);
    }

    public int getLrcSelectedIndex() {
        return lrcSelectedIndex.get();
    }

    public SimpleIntegerProperty lrcSelectedIndexProperty() {
        return lrcSelectedIndex;
    }

    public void setLrcSelectedIndex(int lrcSelectedIndex) {
        this.lrcSelectedIndex.set(lrcSelectedIndex);
    }

    public String getLrcSelectedText() {
        return lrcSelectedText.get();
    }

    public SimpleStringProperty lrcSelectedTextProperty() {
        return lrcSelectedText;
    }

    public void setLrcSelectedText(String lrcSelectedText) {
        this.lrcSelectedText.set(lrcSelectedText);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public SeekSubtitleFileService getSeekSubtitleFileService() {
        return seekSubtitleFileService;
    }

    public void setSeekSubtitleFileService(SeekSubtitleFileService seekSubtitleFileService) {
        this.seekSubtitleFileService = seekSubtitleFileService;
    }
}
