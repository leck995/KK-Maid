package cn.tealc995.kkmaid.player;

import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kikoreu.model.Work;
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
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

import java.util.Collections;
import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-09 03:05
 */
public class VlcPlayer implements TeaMediaPlayer {
    private static final Logger logger = LoggerFactory.getLogger(VlcPlayer.class);
    private MediaPlayer mediaPlayer;
    private SimpleObjectProperty<Music> music;
    private BooleanProperty playing,disorder,loop,isStar,mute,desktopLrcShow;
    private SimpleDoubleProperty currentTime,totalTime,bufferedTime;
    private ObjectProperty<Image> album;
    private DoubleProperty volume;
    private StringProperty title,artist;
    private ObservableList<Audio> songs;//播放列表
    private ObservableList<LrcFile> lrcFiles;
    private ObjectProperty<ObservableList<LrcBean>> lrcBeans;//歌词列
    private SimpleIntegerProperty lrcSelectedIndex;
    private SimpleStringProperty lrcSelectedText;
    private SimpleObjectProperty<Audio> playingAudio;
    private int index=0;
    private SeekSubtitleFileService seekSubtitleFileService;
    private final MediaPlayerFactory mediaPlayerFactory;
    private final AudioPlayerComponent playerComponent;




    public VlcPlayer() throws RuntimeException{
        logger.info("初始化VlcPlayer");



        this.mediaPlayerFactory = new MediaPlayerFactory("--clock-synchro=0");
        playerComponent=new AudioPlayerComponent(mediaPlayerFactory);
        mediaPlayer = playerComponent.mediaPlayer();

/*        NativeLog log = mediaPlayerFactory.application().newLog();
        log.setLevel(LogLevel.DEBUG);
        log.addLogListener(new LogEventListener() {
            @Override
            public void log(LogLevel logLevel, String s, String s1, Integer integer, String s2, String s3, Integer integer1, String s4) {

            }

        });*/



        music=new SimpleObjectProperty<>();
        playingAudio=new SimpleObjectProperty<>();

        playing=new SimpleBooleanProperty(false);//
        disorder=new SimpleBooleanProperty(false); //
        loop=new SimpleBooleanProperty(false);
        isStar=new SimpleBooleanProperty(false);
        mute=new SimpleBooleanProperty(false);//
        album=new SimpleObjectProperty<Image>(new Image(this.getClass().getResource("/cn/tealc995/kkmaid/image/album.jpg").toExternalForm()));
        title=new SimpleStringProperty("音乐随心");
        artist=new SimpleStringProperty("Pure");

        totalTime=new SimpleDoubleProperty(0.0);
        currentTime=new SimpleDoubleProperty(0.0);
        bufferedTime=new SimpleDoubleProperty(0.0);
        volume=new SimpleDoubleProperty(100.0);
        songs= FXCollections.observableArrayList();
        lrcFiles= FXCollections.observableArrayList();
        lrcBeans=new SimpleObjectProperty<>(FXCollections.observableArrayList());
        lrcSelectedIndex=new SimpleIntegerProperty(0);
        lrcSelectedText=new SimpleStringProperty("无台词");
        desktopLrcShow=new SimpleBooleanProperty(false);

        playing.addListener((observableValue, aBoolean, t1) -> {
            if (mediaPlayer == null) return;
            if (t1){
                mediaPlayer.controls().play();
            }else{
                mediaPlayer.controls().pause();
            }
        });
        disorder.addListener((observableValue, aBoolean, t1) -> {
            if (t1){
                Collections.shuffle(songs);
            }else {
                Collections.sort(songs, (o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
            }
        });
        volume.addListener((observable, oldValue, newValue) -> {
            int i = (int) (newValue.doubleValue() * 100);
            if (i == 0){
                mute.set(true);
            }else{
                mute.set(false);
            }
            if (mediaPlayer != null){
                mediaPlayer.audio().setVolume(i);
            }

        });

        mute.addListener((observableValue, aBoolean, t1) -> mediaPlayer.audio().setMute(t1));


        music.addListener((observableValue, music1, t1) -> {
            if (t1 != null){
                if (lrcBeans.get() != null){
                    lrcBeans.get().clear();
                    lrcSelectedText.set("当前无字幕");
                }

                songs.setAll(t1.getAudios());
                if (t1.getLrcFiles() != null){
                    lrcFiles.setAll(t1.getLrcFiles());
                }else {
                    if (lrcFiles.size() > 0)
                        lrcFiles.clear();
                }
                init(index,true);
            }
        });


        playingAudio.addListener((observableValue, media1, t1) -> {
            if (t1 != null){
                changeSubtitle(t1.getTitleWithoutSuffix());
            }
        });


        desktopLrcShow.addListener((observableValue, aBoolean, t1) -> DesktopLrcDialog.getInstance().setVisible(t1));

        //对歌词进行选择
        currentTime.addListener((observableValue, number, t1) -> {
            if (lrcBeans ==null || t1 == null || !isDesktopLrcShow()) return;//无歌词时,桌面歌词不显示时不调用
            if (lrcBeans!=null && lrcBeans.get().size()>0){
                long millis= (long) t1.doubleValue() * 1000;
                if (millis < lrcBeans.get().get(0).getLongTime()) {
                    lrcSelectedIndex.set(0);
                    if (lrcBeans.get().get(0).getTransText() == null) {
                        lrcSelectedText.set(lrcBeans.get().get(0).getRowText());
                    } else {
                        lrcSelectedText.set(lrcBeans.get().get(0).getRowText() +"\n"+lrcBeans.get().get(0).getTransText());
                    }
                }
                int size=lrcBeans.get().size()-1;
                for (int i = 0; i < size; i++) {
                    if (millis > lrcBeans.get().get(i).getLongTime() && millis < lrcBeans.get().get(i+1).getLongTime()){
                        lrcSelectedIndex.set(i);
                        if (lrcBeans.get().get(i).getTransText() == null) {
                            lrcSelectedText.set(lrcBeans.get().get(i).getRowText());
                        } else {
                            lrcSelectedText.set(lrcBeans.get().get(i).getRowText() +"\n"+lrcBeans.get().get(i).getTransText());
                        }
                    }
                }
                if (millis >lrcBeans.get().get(size).getLongTime()){
                    lrcSelectedIndex.set(size);
                    if (lrcBeans.get().get(size).getTransText() == null) {
                        lrcSelectedText.set(lrcBeans.get().get(size).getRowText());
                    } else {
                        lrcSelectedText.set(lrcBeans.get().get(size).getRowText() +"\n"+lrcBeans.get().get(size).getTransText());
                    }
                }
            }
        });
        seekSubtitleFileService =new SeekSubtitleFileService();
        seekSubtitleFileService.valueProperty().addListener((observableValue, list, t1) -> {
            if (t1!= null){
                updateLrcFile(t1);
                EventBusUtil.getDefault().post(new MainNotificationEvent("检测到本地字幕文件"));
            }
        });


        mediaPlayer.audio().setVolume((int) volume.get() * 100);
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter(){
            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                super.timeChanged(mediaPlayer, newTime);
                Platform.runLater(() -> currentTime.set(newTime / 1000));

            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                super.finished(mediaPlayer);
                logger.info("播放完成，下一曲");
                Platform.runLater(()-> {
                    if (loop.get()) {
                        mediaPlayer.controls().stop();
                        mediaPlayer.controls().play();
                    }else {
                        if (Config.setting.isStopPlayOnEnd() && index==songs.size()-1){
                            playing.set(false);
                        }else {
                            next();
                        }
                    }
                } );
            }

            @Override
            public void playing(MediaPlayer mediaPlayer) {
                super.playing(mediaPlayer);
            }

            @Override
            public void opening(MediaPlayer mediaPlayer) {
            }

            @Override
            public void mediaPlayerReady(MediaPlayer mediaPlayer) {
                super.mediaPlayerReady(mediaPlayer);
                Platform.runLater(()-> {
                    totalTime.set(mediaPlayer.media().info().duration()/1000);
                    title.set(playingAudio.get().getTitle());
                    album.set(new Image(music.get().getWork().getThumbnailCoverUrl(),60,60,true,true,true));
                });
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                super.error(mediaPlayer);

                logger.error("无法播放歌曲:"+mediaPlayer.media().info().mrl());
            }

            @Override
            public void buffering(MediaPlayer mediaPlayer, float newCache) {
                super.buffering(mediaPlayer, newCache);
                Platform.runLater(() -> bufferedTime.set(newCache));
                System.out.println("缓存："+newCache);
            }
        });


    }


    @Override
    public void init(int index, boolean autoPlay) {
        this.index=index;
        load(songs.get(index),autoPlay);
    }
    private void load(Audio audio, boolean autoPlay){
        playingAudio.set(audio);
        logger.debug("当前播放流：{}",audio.getStreamUrl());
        logger.debug("当前下载流：{}",audio.getDownloadUrl());
        System.out.println(audio.getStreamUrl());
        mediaPlayer.media().play(audio.getStreamUrl() != null ? audio.getStreamUrl() :audio.getDownloadUrl(),"--clock-synchro=0");
        //mediaPlayer.audio().setVolume((int) volume.get());
        playing.set(true);
    }
    @Override
    public void setMusic(Music music, int index) {
        this.index=index;
        this.music.set(music);
        if (music.getLrcFiles() == null || Config.setting.isLrcPriority()){
            if (music.getWork().hasLanguages()){
                seekSubtitleFileService.setIds(music.getWork().getAllId());
            }else {
                seekSubtitleFileService.setId(music.getWork().getFullId());
            }
            seekSubtitleFileService.restart();
        }
    }

    @Override
    public void pre() {
        if (songs.size() == 0) return;
        if (index==0){
            index=songs.size()-1;
            init(index,true);
        } else {
            index--;
            init(index, true);
        }
    }

    @Override
    public void next() {
        if (songs.size() == 0) return;
        if (index==songs.size()-1){
            init(0,true);
            index=0;
        } else{
            index++;
            init(index,true);
        }
    }

    @Override
    public void seek(double time) {
        if (mediaPlayer!=null){
            mediaPlayer.controls().setTime((long) time * 1000);
        }
    }

    @Override
    public void dispose() {
        playing.set(false);
        currentTime.set(0.0);
        album.set(new Image(this.getClass().getResource("/cn/tealc995/kkmaid/image/album.jpg").toExternalForm()));
        title.set("音乐随心");
        artist.set("Pure");
        totalTime.set(0);
        playingAudio.set(null);
    }

    @Override
    public ObservableList<LrcFile> getLrcFiles() {
        return lrcFiles;
    }

    @Override
    public void updateLrcFile(List<LrcFile> list, int index) {
        lrcFiles.setAll(list);
        loadSubtitleFile(list.get(index));
        
    }

    @Override
    public void updateLrcFile(List<LrcFile> list) {
        lrcFiles.setAll(list);
        loadSubtitleFile(list.get(index));
    }

    @Override
    public Work getWork() {
        if (music.get() != null){
            return music.get().getWork();
        }else {
            return null;
        }
    }

    @Override
    public boolean ready() {
        return mediaPlayer.status().isPlayable();
    }

    @Override
    public String mainCover() {
        if (ready()){
            return music.get().getWork().getMainCoverUrl();
        }else {
            return null;
        }
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
        if (songs.size() == 1){
            clearPlayingList();
        }else {
            if (index == this.index){
                next();
                this.index--;
            }else if (index < this.index){
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

    private void loadSubtitleFile(LrcFile lrcFile) {
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
                    }else {
                        lrcBeans.get().clear();
                        lrcSelectedText.set("当前无字幕");
                    }
                }
            });
            Thread.startVirtualThread(task);
        }
    }



    /**
     * @description: 切换字幕
     * @param:	subtitleTitle	
     * @return  void
     * @date:   2025/2/3
     */
    private void changeSubtitle(String subtitleTitle) {
        if (!lrcFiles.isEmpty()){
            List<LrcFile> list = lrcFiles.stream()
                    .filter(lrcFile -> lrcFile.getTitleWithoutSuffix().equals(subtitleTitle)).toList();
            if (!list.isEmpty()){//当有名称对应的歌词时
                loadSubtitleFile(list.getFirst());
            }else {//如果没有匹配的歌词，
                if (lrcFiles.size() == music.get().getAudios().size()){ //判断歌词列表size和歌曲列表size，相同则获取对应index的歌词。
                    loadSubtitleFile(lrcFiles.get(index));
                }else {
                    lrcBeans.get().clear();
                    lrcSelectedText.set("当前无字幕");
                }
            }
        }
    }


}