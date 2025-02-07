package cn.tealc995.kkmaid.player;


import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kkmaid.model.Audio;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @description:
 * @author: Leck
 * @create: 2023-02-06 22:25
 */
public class LcMediaPlayer extends TeaMediaPlayer {
    private static final Logger LOG = LoggerFactory.getLogger(LcMediaPlayer.class);
    private MediaPlayer mediaPlayer;

    public LcMediaPlayer() {
        playing.addListener((observableValue, aBoolean, t1) -> {
            if (mediaPlayer == null) return;
            if (t1) {
                mediaPlayer.play();
            } else {
                mediaPlayer.pause();
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
                    if (Config.setting.isStopPlayOnEnd() && index == songs.size() - 1) {
                        playing.set(false);
                    } else {
                        next();
                    }

                }
            }
        });
    }


    @Override
    public void seek(double time) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(time));
        }
    }






    @Override
    public boolean ready() {
        if (mediaPlayer != null && mediaPlayer.getMedia() != null)
            return true;
        else
            return false;
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
    public void release() {
        if (mediaPlayer != null) {
            playing.set(false);
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        LOG.debug("释放播放器资源");
    }


}


