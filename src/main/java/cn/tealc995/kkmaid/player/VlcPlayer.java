package cn.tealc995.kkmaid.player;

import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kkmaid.model.Audio;
import javafx.application.Platform;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-09 03:05
 */
public class VlcPlayer extends TeaMediaPlayer {
    private static final Logger LOG = LoggerFactory.getLogger(VlcPlayer.class);
    private final MediaPlayer mediaPlayer;

    public VlcPlayer() throws RuntimeException {
        LOG.info("初始化VlcPlayer");
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory("--clock-synchro=0");
        AudioPlayerComponent playerComponent = new AudioPlayerComponent(mediaPlayerFactory);
        mediaPlayer = playerComponent.mediaPlayer();

        playing.addListener((_, _, status) -> {
            if (mediaPlayer == null) return;
            if (status) {
                mediaPlayer.controls().play();
            } else {
                mediaPlayer.controls().pause();
            }
        });

        volume.addListener((_, _, newValue) -> {
            int i = (int) (newValue.doubleValue() * 100);
            mute.set(i == 0);
            if (mediaPlayer != null) {
                mediaPlayer.audio().setVolume(i);
            }
        });

        mute.addListener((_, _, t1) -> mediaPlayer.audio().setMute(t1));

        mediaPlayer.audio().setVolume((int) volume.get() * 100);
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                super.timeChanged(mediaPlayer, newTime);
                Platform.runLater(() -> currentTime.set((double) newTime / 1000));
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                super.finished(mediaPlayer);
                LOG.info("播放完成，下一曲");
                Platform.runLater(() -> {
                    if (loop.get()) {
                        mediaPlayer.controls().stop();
                        mediaPlayer.controls().play();
                    } else {
                        if (Config.setting.isStopPlayOnEnd() && index == songs.size() - 1) {
                            playing.set(false);
                        } else {
                            next();
                        }
                    }
                });
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
                Platform.runLater(() -> {
                    totalTime.set((double) mediaPlayer.media().info().duration() / 1000);
                    title.set(playingAudio.get().getTitle());
                    album.set(new Image(music.get().getWork().getThumbnailCoverUrl(), 60, 60, true, true, true));
                });
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                super.error(mediaPlayer);
                LOG.error("播放歌曲:{} 遇到错误", mediaPlayer.media().info().mrl());
            }

            @Override
            public void buffering(MediaPlayer mediaPlayer, float newCache) {
                super.buffering(mediaPlayer, newCache);
                Platform.runLater(() -> bufferedTime.set(newCache));
            }
        });
    }

    private void load(Audio audio, boolean autoPlay) {
        playingAudio.set(audio);
        LOG.debug("当前播放流：{}", audio.getStreamUrl());
        LOG.debug("当前下载流：{}", audio.getDownloadUrl());
        System.out.println(audio.getStreamUrl());
        mediaPlayer.media().play(audio.getStreamUrl() != null ? audio.getStreamUrl() : audio.getDownloadUrl(), "--clock-synchro=0");
        //mediaPlayer.audio().setVolume((int) volume.get());
        playing.set(true);
    }

    @Override
    public void init(int index, boolean autoPlay) {
        this.index = index;
        load(songs.get(index), autoPlay);
    }

    @Override
    public void seek(double time) {
        if (mediaPlayer != null) {
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
    public boolean ready() {
        return mediaPlayer.status().isPlayable();
    }


}