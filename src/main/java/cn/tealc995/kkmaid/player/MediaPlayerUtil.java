package cn.tealc995.kkmaid.player;

import cn.tealc995.kkmaid.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-09 02:48
 */
public class MediaPlayerUtil {
    private static final Logger logger = LoggerFactory.getLogger(MediaPlayerUtil.class);
    private static LcMediaPlayer baseMediaPlayer;
    private static VlcPlayer vlcPlayer;

    public static TeaMediaPlayer mediaPlayer(){
        if (Config.useVlcPlayer.get()){
            if (vlcPlayer == null){
                try {
                    vlcPlayer=new VlcPlayer();
                    logger.info("将使用VlcPlayer进行播放");
                }catch (RuntimeException e){
                    logger.info("VlC未安装，无法正常使用");





                    //EventBusUtil.getDefault().post(new MainNotificationEvent("VLC播放器未安装，将使用默认播放器进行播放"));
                    //Config.useVlcPlayer.set(false);
                    return new LcMediaPlayer();
                }

            }
            return vlcPlayer;
        }else {
            if (baseMediaPlayer == null){
                baseMediaPlayer=new LcMediaPlayer();
                logger.info("将使用默认MediaPlayer进行播放");
            }
            return baseMediaPlayer;
        }



    }
}