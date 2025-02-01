package cn.tealc995.kkmaid.player;


import cn.tealc995.kkmaid.Config;
import cn.tealc995.kikoreu.model.Work;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainNotificationEvent;
import cn.tealc995.kkmaid.model.Audio;
import cn.tealc995.kkmaid.model.lrc.LrcBean;
import cn.tealc995.kkmaid.model.Music;
import cn.tealc995.kkmaid.model.lrc.LrcFile;
import cn.tealc995.kkmaid.model.lrc.LrcType;
import cn.tealc995.kkmaid.service.SeekLrcFileService;
import cn.tealc995.kkmaid.ui.component.DesktopLrcDialog;
import cn.tealc995.kkmaid.util.LrcImportUtil;
import cn.tealc995.kkmaid.zip.ZipEntityFile;
import cn.tealc995.kkmaid.zip.ZipUtil;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;


/**
 * @program: AmsrPlayer-old
 * @description:
 * @author: Leck
 * @create: 2023-02-06 22:25
 */
public class LcMediaPlayer implements TeaMediaPlayer {
    private static final Logger logger = LoggerFactory.getLogger(LcMediaPlayer.class);
    private MediaPlayer mediaPlayer;
    private SimpleObjectProperty<Music> music;
    private BooleanProperty playing, disorder, loop, isStar, mute, desktopLrcShow;
    private SimpleDoubleProperty currentTime, totalTime, bufferedTime;
    private ObjectProperty<Image> album;
    private DoubleProperty volume;
    private StringProperty title, artist;
    private ObservableList<Audio> songs;//播放列表
    private ObservableList<LrcFile> lrcFiles;
    private ObjectProperty<ObservableList<LrcBean>> lrcBeans;//歌词列
    private SimpleIntegerProperty lrcSelectedIndex;
    private SimpleStringProperty lrcSelectedText;
    private SimpleObjectProperty<Audio> playingAudio;
    private int index = 0;
    private SeekLrcFileService seekLrcFileService;


    public LcMediaPlayer() {
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

        playing.addListener((observableValue, aBoolean, t1) -> {
            if (mediaPlayer == null) return;
            if (t1) {
                mediaPlayer.play();
            } else {
                mediaPlayer.pause();
            }
        });
        disorder.addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                Collections.shuffle(songs);
            } else {
                Collections.sort(songs, (o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
            }
        });
        volume.addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() == 0) {
                mute.set(true);
            } else {
                mute.set(false);
            }
            if (mediaPlayer != null)
                mediaPlayer.setVolume(newValue.doubleValue());
        });


        music.addListener((observableValue, music1, t1) -> {
            if (t1 != null) {
                if (lrcBeans.get() != null) {
                    lrcBeans.get().clear();
                    lrcSelectedText.set("当前无字幕");
                }

                songs.setAll(t1.getAudios());
                if (t1.getLrcFiles() != null) {
                    lrcFiles.setAll(t1.getLrcFiles());
                } else {
                    if (lrcFiles.size() > 0)
                        lrcFiles.clear();
                }
                init(index, true);
            }
        });

        playingAudio.addListener((observableValue, media1, t1) -> {
            if (t1 != null) {
                if (lrcFiles.size() > 0) {
                    List<LrcFile> list = lrcFiles.stream().filter(lrcFile -> lrcFile.getTitleWithoutSuffix().equals(t1.getTitleWithoutSuffix())).toList();
                    if (list.size() > 0) {//当有名称对应的歌词时
                        lrcBeans.set(FXCollections.observableArrayList(lrcFileToBeans(list.get(0))));
                    } else {
                        if (lrcFiles.size() == music.get().getAudios().size()) { //如果没有匹配的歌词，判断歌词列表size和歌曲列表size，相同则获取对应index的歌词。
                            List<LrcBean> lrcBeans1 = lrcFileToBeans(lrcFiles.get(index));
                            if (lrcBeans1 != null) {
                                lrcBeans.set(FXCollections.observableArrayList(lrcBeans1));
                            } else {
                                lrcBeans.get().clear();
                                lrcSelectedText.set("当前无字幕");
                            }
                        } else {
                            lrcBeans.get().clear();
                            lrcSelectedText.set("当前无字幕");
                        }
                    }
                }
            }
        });


        desktopLrcShow.addListener((observableValue, aBoolean, t1) -> DesktopLrcDialog.getInstance().setVisible(t1));

        //对歌词进行选择
        currentTime.addListener((observableValue, number, t1) -> {
            if (lrcBeans == null || t1 == null || !isDesktopLrcShow()) return;//无歌词时,桌面歌词不显示时不调用
            if (lrcBeans != null && lrcBeans.get().size() > 0) {
                long millis = (long) t1.doubleValue() * 1000;
                if (millis < lrcBeans.get().get(0).getLongTime()) {
                    lrcSelectedIndex.set(0);
                    if (lrcBeans.get().get(0).getTransText() == null) {
                        lrcSelectedText.set(lrcBeans.get().get(0).getRowText());
                    } else {
                        lrcSelectedText.set(lrcBeans.get().get(0).getRowText() + "\n" + lrcBeans.get().get(0).getTransText());
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
        seekLrcFileService = new SeekLrcFileService();
        seekLrcFileService.valueProperty().addListener((observableValue, list, t1) -> {
            if (t1 != null) {
                updateLrcFile(t1);
                EventBusUtil.getDefault().post(new MainNotificationEvent("检测到本地字幕文件"));
            }
        });

    }


    @Override
    public void init(int index, boolean autoPlay) {
        this.index = index;
        load(songs.get(index), autoPlay);
    }

    private void load(Audio audio, boolean autoPlay) {
        playingAudio.set(audio);
        Media media;
        try {
            media = new Media(audio.getStreamUrl() != null ? audio.getStreamUrl() : audio.getDownloadUrl());
            if (mediaPlayer != null)
                mediaPlayer.dispose();
            mediaPlayer = new MediaPlayer(media);
        } catch (MediaException mediaException) {
            if (mediaException.getType() == MediaException.Type.MEDIA_UNSUPPORTED) {
                System.out.println("目前软件不支持该音频类型，不支持压缩的wav格式");
            } else if (mediaException.getType() == MediaException.Type.MEDIA_UNAVAILABLE) {
                System.out.println("找不到指定文件");
            } else {
                System.out.println("播放器未知错误");
            }
            return;
        }
        System.gc();
        //歌曲加载完成
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                if (autoPlay) {
                    mediaPlayer.play();
                    playing.set(true);
                }
                mediaPlayer.muteProperty().bind(mute);
                totalTime.set(mediaPlayer.getTotalDuration().toSeconds());

                mediaPlayer.volumeProperty().bindBidirectional(volume);
                String tempTitle = (String) media.getMetadata().get("title");
                String tempArtist = (String) media.getMetadata().get("artist");
                title.set(tempTitle != null ? tempTitle : audio.getTitle());


                if (tempArtist != null) artist.set(tempArtist);
                else artist.set(null);


                album.set(new Image(music.get().getWork().getThumbnailCoverUrl(), 60, 60, true, true, true));

                mediaPlayer.currentTimeProperty().addListener((observableValue, duration, t1) -> {
                    currentTime.set(t1.toSeconds());

                });

                mediaPlayer.bufferProgressTimeProperty().addListener((observableValue, duration, t1) -> {
                    bufferedTime.set(t1.toSeconds() / totalTime.get() * 100);
                });

            }
        });

        //歌曲播放结束
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                if (loop.get()) {
                    mediaPlayer.stop();
                    mediaPlayer.play();
                } else {
                    //播放下一曲
                    if (Config.stopPlayOnEnd.get() && index == songs.size() - 1) {
                        playing.set(false);
                    } else {
                        next();
                    }

                }
            }
        });
    }

    @Override
    public void next() {
        if (songs.size() == 0) return;
        if (index == songs.size() - 1) {
            init(0, true);
            index = 0;
        } else {
            index++;
            init(index, true);
        }
    }

    @Override
    public void pre() {
        if (songs.size() == 0) return;
        if (index == 0) {
            index = songs.size() - 1;
            init(index, true);
        } else {
            index--;
            init(index, true);
        }
    }

    @Override
    public void seek(double time) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(time));
        }
    }

    private List<LrcBean> lrcFileToBeans(LrcFile lrcFile) {
        List<LrcBean> lrcBeans = null;
        if (lrcFile.getType() == LrcType.NET) {
            lrcBeans = LrcImportUtil.getLrcFromNet(lrcFile.getPath());
        } else if (lrcFile.getType() == LrcType.FOLDER) {
            lrcBeans = LrcImportUtil.getLrcFromFolder(lrcFile.getPath());
        } else if (lrcFile.getType() == LrcType.ZIP) {
            lrcBeans = LrcImportUtil.getLrcFromZip(lrcFile);
        }
        if (lrcBeans != null) {
            lrcBeans.removeIf(lrcBean -> {
                String row = lrcBean.getRowText();
                for (String s : Config.textBlackList) {
                    if (row.contains(s)) {
                        return true;
                    }
                }
                return false;
            });
            return lrcBeans;
        } else {
            return new ArrayList<>();
        }
    }


    @Override
    public ObservableList<LrcFile> getLrcFiles() {
        return lrcFiles;
    }

    /**
     * @return void
     * @description: 更新歌词文件列表
     * @name: updateLrcFile
     * @author: Leck
     * @param: list    新的歌词文件列表
     * @param: index    要加载的歌词文件
     * @date: 2023/7/18
     */
    @Override
    public void updateLrcFile(List<LrcFile> list, int index) {
        lrcFiles.setAll(list);
        lrcBeans.set(FXCollections.observableArrayList(lrcFileToBeans(list.get(index))));
    }

    @Override
    public void updateLrcFile(List<LrcFile> list) {
        lrcFiles.setAll(list);
        lrcBeans.set(FXCollections.observableArrayList(lrcFileToBeans(list.get(index))));
    }


    @Override
    public String getTitle() {
        return title.get();
    }

    @Override
    public StringProperty titleProperty() {
        return title;
    }

    @Override
    public ObservableList<LrcBean> getLrcBeans() {
        return lrcBeans.get();
    }

    @Override
    public ObjectProperty<ObservableList<LrcBean>> lrcBeansProperty() {
        return lrcBeans;
    }

    @Override
    public String getArtist() {
        return artist.get();
    }

    @Override
    public StringProperty artistProperty() {
        return artist;
    }

    @Override
    public Double getCurrentTime() {
        return currentTime.get();
    }

    @Override
    public SimpleDoubleProperty currentTimeProperty() {
        return currentTime;
    }

    @Override
    public double getTotalTime() {
        return totalTime.get();
    }

    @Override
    public DoubleProperty totalTimeProperty() {
        return totalTime;
    }

    @Override
    public boolean isPlaying() {
        return playing.get();
    }

    @Override
    public BooleanProperty playingProperty() {
        return playing;
    }

    @Override
    public boolean isDisorder() {
        return disorder.get();
    }

    @Override
    public BooleanProperty disorderProperty() {
        return disorder;
    }

    @Override
    public boolean isLoop() {
        return loop.get();
    }

    @Override
    public BooleanProperty loopProperty() {
        return loop;
    }

    @Override
    public boolean isMute() {
        return mute.get();
    }

    @Override
    public BooleanProperty muteProperty() {
        return mute;
    }

    @Override
    public double getVolume() {
        return volume.get();
    }

    @Override
    public DoubleProperty volumeProperty() {
        return volume;
    }

    @Override
    public Image getAlbum() {
        return album.get();
    }

    @Override
    public ObjectProperty<Image> albumProperty() {
        return album;
    }

    @Override
    public int getLrcSelectedIndex() {
        return lrcSelectedIndex.get();
    }

    @Override
    public SimpleIntegerProperty lrcSelectedIndexProperty() {
        return lrcSelectedIndex;
    }

    @Override
    public String getLrcSelectedText() {
        return lrcSelectedText.get();
    }

    @Override
    public SimpleStringProperty lrcSelectedTextProperty() {
        return lrcSelectedText;
    }

    @Override
    public boolean isDesktopLrcShow() {
        return desktopLrcShow.get();
    }

    @Override
    public BooleanProperty desktopLrcShowProperty() {
        return desktopLrcShow;
    }

    @Override
    public ObservableList<Audio> getSongs() {
        return songs;
    }

    @Override
    public Audio getPlayingAudio() {
        return playingAudio.get();
    }

    @Override
    public SimpleObjectProperty<Audio> playingAudioProperty() {
        return playingAudio;
    }


    @Override
    public Work getWork() {
        if (music.get() != null) {
            return music.get().getWork();
        } else {
            return null;
        }
    }

    @Override
    public boolean ready() {
        if (mediaPlayer != null && mediaPlayer.getMedia() != null)
            return true;
        else
            return false;
    }

    @Override
    public void setMusic(Music music, int index) {
        this.index = index;
        this.music.set(music);
        if (music.getLrcFiles() == null || Config.lrcPriority.get()) {
            if (music.getWork().hasLanguages()) {
                seekLrcFileService.setIds(music.getWork().getAllId());
            } else {
                seekLrcFileService.setId(music.getWork().getFullId());
            }
            seekLrcFileService.restart();
        }
    }


    /**
     * @return void
     * @description:
     * @name: seekLrcFile
     * @author: Leck
     * @param: id    记住这个id传过来7位的id变成8位
     * @date: 2023/7/19
     */
    private void seekLrcFile(String id) {
        System.out.println("AA:" + id);
        String folder = Config.lrcFileFolder.get();
        if (folder != null && folder.length() > 0) {
            System.out.println("ssssssss");
            File file = new File(folder);
            if (file.exists() && file.isDirectory()) {
                String finalId = "rj" + id;
                File[] files = file.listFiles(file1 -> file1.isDirectory() && file1.getName().toLowerCase().contains(finalId));
                if (files != null && files.length > 0) {

                    File rjFolder = files[0];
                    File[] files1 = rjFolder.listFiles(file1 -> file1.isFile() && file1.getName().toLowerCase().endsWith(".lrc"));
                    List<LrcFile> list = new ArrayList<>();
                    if (files1 != null && files1.length > 0) {
                        for (File file1 : files1) {
                            list.add(new LrcFile(file1.getName(), file1.getPath(), LrcType.FOLDER));
                        }
                    }
                    updateLrcFile(list);
                    EventBusUtil.getDefault().post(new MainNotificationEvent("检测到本地字幕文件"));
                    return;
                }
            }
        }

        folder = Config.lrcZipFolder.get();
        if (folder != null && folder.length() > 0) {
            System.out.println("ssssssss");
            File rootFolder = new File(folder);
            if (rootFolder.exists() && rootFolder.isDirectory()) {
                String finalId = "rj" + id;
                File[] files = rootFolder.listFiles(file -> {
                    String name = file.getName().toLowerCase();
                    return file.isFile() && name.contains(finalId) && name.endsWith(".zip");
                });
                if (files != null && files.length > 0) {
                    File file = files[0];
                    List<ZipEntityFile> allLrcFile = ZipUtil.getAllLrcFile(file);
                    List<LrcFile> list = new ArrayList<>();
                    for (ZipEntityFile zipEntityFile : allLrcFile) {
                        list.add(new LrcFile(zipEntityFile.getName(), zipEntityFile.getPath(), LrcType.ZIP, file.getPath()));
                    }
                    updateLrcFile(list);
                    EventBusUtil.getDefault().post(new MainNotificationEvent("检测到本地字幕文件"));
                    return;
                }
            }


        }


    }

    /**
     * @return void
     * @description: 设置播放取消
     * @name: setDispose
     * @author: Leck
     * @param:
     * @date: 2023/2/24
     */
    @Override
    public void dispose() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        playing.set(false);
        currentTime.set(0.0);
        album.set(new Image(this.getClass().getResource("/cn/tealc995/kkmaid/image/album.jpg").toExternalForm()));
        title.set("音乐随心");
        artist.set("Pure");
        totalTime.set(0);
        playingAudio.set(null);
        //currentTime.set(new Duration(0.0));
    }

    @Override
    public void clearPlayingList() {
        dispose();
        songs.clear();
        lrcBeans.get().clear();
        lrcFiles.clear();
        music.set(null);
    }

    @Override
    public int getPlayingIndexInList() {
        return songs.indexOf(getPlayingAudio());
    }


    @Override
    public void removeAudio(Audio audio) {

    }

    @Override
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
            lrcFiles.remove(index);
        }
    }

    @Override
    public Double getBufferedTime() {
        return bufferedTime.get();
    }

    @Override
    public SimpleDoubleProperty bufferedTimeProperty() {
        return bufferedTime;
    }

    public String mainCover() {
        if (ready()) {
            return music.get().getWork().getMainCoverUrl();
        } else {
            return null;
        }
    }


}


