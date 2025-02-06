package cn.tealc995.kkmaid.player;

import cn.tealc995.kkmaid.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: Leck
 * @create: 2023-08-09 02:48
 */
public class MediaPlayerUtil {
    private static final Logger LOG = LoggerFactory.getLogger(MediaPlayerUtil.class);
    private static LcMediaPlayer baseMediaPlayer;
    private static VlcPlayer vlcPlayer;

    public static TeaMediaPlayer mediaPlayer(){
        if (Config.setting.isUseVlcPlayer()){
            if (vlcPlayer == null){
                try {
                    vlcPlayer=new VlcPlayer();
                    LOG.info("将使用VlcPlayer进行播放");
                }catch (RuntimeException e){
                    LOG.info("VlC未安装，无法正常使用");
                    return new LcMediaPlayer();
                }
            }
            return vlcPlayer;
        }else {
            if (baseMediaPlayer == null){
                baseMediaPlayer=new LcMediaPlayer();
                LOG.info("将使用默认MediaPlayer进行播放");
            }
            return baseMediaPlayer;
        }
    }
}