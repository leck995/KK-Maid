package cn.tealc995.asmronline;

import cn.tealc995.asmronline.api.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 21:08
 */
public class Config {
    /*========================基本========================*/
    public static SimpleStringProperty version=new SimpleStringProperty("1.2.1");//当前版本
    public static SimpleStringProperty configVersion=new SimpleStringProperty();//配置文件中的版本
    public static SimpleStringProperty ignoreVersion=new SimpleStringProperty();
    public static SimpleBooleanProperty autoCheckVersion=new SimpleBooleanProperty(false);//检查更新
    public static SimpleDoubleProperty stageWidth=new SimpleDoubleProperty(1600.0);
    public static SimpleDoubleProperty stageHeight=new SimpleDoubleProperty(960.0);


    public static SimpleBooleanProperty gridSortDescModel=new SimpleBooleanProperty(true);
    public static SimpleBooleanProperty gridSubtitleModel=new SimpleBooleanProperty(false);
    public static SimpleStringProperty gridOrder=new SimpleStringProperty("create_date");

    /*========================服务器========================*/
    public static final SimpleStringProperty HOST=new SimpleStringProperty();
    public static final SimpleStringProperty TOKEN=new SimpleStringProperty();
    public static SimpleBooleanProperty proxyModel=new SimpleBooleanProperty(false);//开启代理
    public static SimpleStringProperty proxyHost=new SimpleStringProperty();//代理地址
    public static SimpleStringProperty proxyPort=new SimpleStringProperty();//
    /*===============桌面歌词=================*/
    public static SimpleIntegerProperty desktopLRCStageWidth=new SimpleIntegerProperty(1000);//桌面歌词字体大小
    public static SimpleIntegerProperty desktopLRCFontSize=new SimpleIntegerProperty(30);//桌面歌词字体大小
    public static SimpleStringProperty desktopLRCFontColor=new SimpleStringProperty("#FFFFFF");//桌面歌词字体颜色
    public static SimpleBooleanProperty desktopLRCBorderModel=new SimpleBooleanProperty(true);//开启桌面歌词字体边框，默认开启
    public static SimpleStringProperty desktopLRCBorderColor=new SimpleStringProperty("#00b8d4");//桌面歌词字体边框颜色

    public static SimpleBooleanProperty desktopLRCStrokeModel=new SimpleBooleanProperty(true);//开启桌面歌词字体描边，默认开启
    public static SimpleStringProperty desktopLRCStrokeColor=new SimpleStringProperty("#00b8d4");//桌面歌词字体描边颜色
    public static SimpleBooleanProperty desktopLRCBoldModel=new SimpleBooleanProperty(false);//开启桌面歌词字体粗体,默认关闭




    /*==============播放界面=================*/
    public static SimpleBooleanProperty detailAlbumEffectModel=new SimpleBooleanProperty(true);//设置播放界面封面阴影，默认开启
    public static SimpleDoubleProperty detailAlbumEffectSize=new SimpleDoubleProperty(2);//播放界面封面圆角大小
    public static SimpleBooleanProperty detailAlbumRadiusModel=new SimpleBooleanProperty(true);//播放界面封面圆角
    public static SimpleDoubleProperty detailAlbumRadiusSize=new SimpleDoubleProperty(30);//播放界面封面圆角大小
    public static SimpleBooleanProperty detailLrcAlignment=new SimpleBooleanProperty(false);//设置播放界面歌词对其方式，默认左对齐false,true为居中
    public static SimpleIntegerProperty detailGaussianSize=new SimpleIntegerProperty(25);//高斯模糊大小
    public static SimpleDoubleProperty detailDarkerSize=new SimpleDoubleProperty(0.5f);//暗角
    /*==============字幕=================*/
    public static SimpleStringProperty lrcFileFolder=new SimpleStringProperty();
    public static SimpleStringProperty lrcZipFolder=new SimpleStringProperty();

    public static SimpleBooleanProperty lrcPriority=new SimpleBooleanProperty(false);

    /*==============黑名单=================*/
    public static ObservableSet<String> workBlackList= FXCollections.observableSet();
    public static ObservableSet<String> tagBlackList= FXCollections.observableSet();
    public static ObservableSet<String> textBlackList= FXCollections.observableSet();
    /*==============播放设置=================*/
    public static SimpleBooleanProperty useVlcPlayer=new SimpleBooleanProperty(false);
    public static SimpleBooleanProperty stopPlayOnEnd=new SimpleBooleanProperty(true);//作品播放到最后自动停止播放

    /*==============下载设置=================*/
    public static SimpleStringProperty downloadDir=new SimpleStringProperty();//下载目录
    public static SimpleStringProperty aria2Host=new SimpleStringProperty();//aria2
    public static SimpleStringProperty ariaRPCKey=new SimpleStringProperty();//aria2授权密钥
    public static SimpleStringProperty saveNameTemplate=new SimpleStringProperty("{RJ}");//命名模板


    public static Properties properties;

    static {
        if (TOKEN.get() != null && TOKEN.get().length() > 0){
            HttpUtils.TOKEN=TOKEN.get();
        }

        TOKEN.addListener((observableValue, s, t1) -> {
            if (t1.equals("")){
                HttpUtils.TOKEN=null;
            }else {
                HttpUtils.TOKEN=t1;
            }
        });



        InputStream input= null;
        ObjectMapper objectMapper=new ObjectMapper();

        try {

            File file=new File("config.properties");
            if (!file.exists()){
                file.createNewFile();
                properties=new Properties();
            }else {
                input = new FileInputStream(file);
                properties=new Properties();
                properties.load(input);
                input.close();

                /*=======================基本======================*/
                if (properties.containsKey("CONFIG_VERSION")){
                    String pathRow=properties.getProperty("CONFIG_VERSION");
                    if (pathRow.length() > 0){
                        configVersion.set(pathRow);
                    }
                }

                if (properties.containsKey("IGNORE_VERSION")){
                    String pathRow=properties.getProperty("IGNORE_VERSION");
                    if (pathRow.length() > 0){
                        ignoreVersion.set(pathRow);
                    }
                }

                if (properties.containsKey("AUTO_CHECK_VERSION")){
                    String pathRow=properties.getProperty("AUTO_CHECK_VERSION");
                    if (pathRow.length() > 0){
                        autoCheckVersion.set(Boolean.parseBoolean(pathRow));
                    }
                }

                if (properties.containsKey("STAGE_WIDTH")){
                    String pathRow=properties.getProperty("STAGE_WIDTH");
                    if (pathRow.length() > 0){
                        stageWidth.set(Double.parseDouble(pathRow));
                    }
                }
                if (properties.containsKey("STAGE_HEIGHT")){
                    String pathRow=properties.getProperty("STAGE_HEIGHT");
                    if (pathRow.length() > 0){
                        stageHeight.set(Double.parseDouble(pathRow));
                    }
                }


                if (properties.containsKey("GRID_ORDER")){
                    String pathRow=properties.getProperty("GRID_ORDER");
                    if (pathRow.length() > 0){
                        gridOrder.set(pathRow);
                    }
                }

                if (properties.containsKey("GRID_SORT_DESC_MODEL")){
                    String pathRow=properties.getProperty("GRID_SORT_DESC_MODEL");
                    if (pathRow.length() > 0){
                        gridSortDescModel.set(Boolean.parseBoolean(pathRow));
                    }
                }

                if (properties.containsKey("GRID_SUBTITLE_MODEL")){
                    String pathRow=properties.getProperty("GRID_SUBTITLE_MODEL");
                    if (pathRow.length() > 0){
                        gridSubtitleModel.set(Boolean.parseBoolean(pathRow));
                    }
                }


                /*=====================服务器========================*/
                if (properties.containsKey("HOST")){
                    String pathRow=properties.getProperty("HOST");
                    if (pathRow.length() > 0){
                        HOST.set(pathRow);
                    }
                }
                if (properties.containsKey("TOKEN")){
                    String pathRow=properties.getProperty("TOKEN");
                    if (pathRow.length() > 0){
                        TOKEN.set(pathRow);
                    }
                }


                if (properties.containsKey("PROXY_MODEL")){
                    String pathRow=properties.getProperty("PROXY_MODEL");
                    if (pathRow.length() > 0){
                        proxyModel.set(Boolean.parseBoolean(pathRow));
                    }
                }
                if (properties.containsKey("PROXY_HOST")){
                    String pathRow=properties.getProperty("PROXY_HOST");
                    if (pathRow.length() > 0){
                        proxyHost.set(pathRow);
                    }
                }
                if (properties.containsKey("PROXY_PORT")){
                    String pathRow=properties.getProperty("PROXY_PORT");
                    if (pathRow.length() > 0){
                        proxyPort.set(pathRow);
                    }
                }

                /*=====================播放界面========================*/
                if (properties.containsKey("DETAIL_ALBUM_RADIUS_MODEL")){
                    String pathRow=properties.getProperty("DETAIL_ALBUM_RADIUS_MODEL");
                    if (pathRow.length() > 0){
                        detailAlbumRadiusModel.set(Boolean.parseBoolean(pathRow));
                    }
                }
                if (properties.containsKey("DETAIL_ALBUM_RADIUS_SIZE")){
                    String pathRow=properties.getProperty("DETAIL_ALBUM_RADIUS_SIZE");
                    if (pathRow.length() > 0){
                        detailAlbumRadiusSize.set(Double.parseDouble(pathRow));
                    }
                }
                if (properties.containsKey("DETAIL_ALBUM_EFFECT_MODEL")){
                    String pathRow=properties.getProperty("DETAIL_ALBUM_EFFECT_MODEL");
                    if (pathRow.length() > 0){
                        detailAlbumEffectModel.set(Boolean.parseBoolean(pathRow));
                    }
                }
                if (properties.containsKey("DETAIL_ALBUM_EFFECT_SIZE")){
                    String pathRow=properties.getProperty("DETAIL_ALBUM_EFFECT_SIZE");
                    if (pathRow.length() > 0){
                        detailAlbumEffectSize.set(Double.parseDouble(pathRow));
                    }
                }
                if (properties.containsKey("DETAIL_LRC_ALIGNMENT")){
                    String pathRow=properties.getProperty("DETAIL_LRC_ALIGNMENT");
                    if (pathRow.length() > 0){
                        detailLrcAlignment.set(Boolean.parseBoolean(pathRow));
                    }
                }

                if (properties.containsKey("DETAIL_GAUSSIAN_SIZE")){
                    String pathRow=properties.getProperty("DETAIL_GAUSSIAN_SIZE");
                    if (pathRow.length() > 0){
                        detailGaussianSize.set(Integer.parseInt(pathRow));
                    }
                }

                if (properties.containsKey("DETAIL_DARKER_SIZE")){
                    String pathRow=properties.getProperty("DETAIL_DARKER_SIZE");
                    if (pathRow.length() > 0){
                        detailDarkerSize.set(Double.parseDouble(pathRow));
                    }
                }

                /*=====================桌面歌词========================*/
                if (properties.containsKey("DESKTOP_LRC_FONT_SIZE")){
                    String pathRow=properties.getProperty("DESKTOP_LRC_FONT_SIZE");
                    if (pathRow.length() > 0){
                        desktopLRCFontSize.set(Integer.parseInt(pathRow));
                    }
                }

                if (properties.containsKey("DESKTOP_LRC_FONT_COLOR")){
                    String pathRow=properties.getProperty("DESKTOP_LRC_FONT_COLOR");
                    if (pathRow.length() > 0){
                        desktopLRCFontColor.set(pathRow);
                    }
                }
                if (properties.containsKey("DESKTOP_LRC_BORDER_COLOR")){
                    String pathRow=properties.getProperty("DESKTOP_LRC_BORDER_COLOR");
                    if (pathRow.length() > 0){
                        desktopLRCBorderColor.set(pathRow);
                    }
                }
                if (properties.containsKey("DESKTOP_LRC_BORDER_MODEL")){
                    String pathRow=properties.getProperty("DESKTOP_LRC_BORDER_MODEL");
                    if (pathRow.length() > 0){
                        desktopLRCBorderModel.set(Boolean.parseBoolean(pathRow));
                    }
                }
                if (properties.containsKey("DESKTOP_LRC_STROKE_COLOR")){
                    String pathRow=properties.getProperty("DESKTOP_LRC_STROKE_COLOR");
                    if (pathRow.length() > 0){
                        desktopLRCStrokeColor.set(pathRow);
                    }
                }
                if (properties.containsKey("DESKTOP_LRC_STROKE_MODEL")){
                    String pathRow=properties.getProperty("DESKTOP_LRC_STROKE_MODEL");
                    if (pathRow.length() > 0){
                        desktopLRCStrokeModel.set(Boolean.parseBoolean(pathRow));
                    }
                }
                if (properties.containsKey("DESKTOP_LRC_BOLD_MODEL")){
                    String pathRow=properties.getProperty("DESKTOP_LRC_BOLD_MODEL");
                    if (pathRow.length() > 0){
                        desktopLRCBoldModel.set(Boolean.parseBoolean(pathRow));
                    }
                }

                /*=====================字幕========================*/
                if (properties.containsKey("LRC_FILE_FOLDER")){
                    String pathRow=properties.getProperty("LRC_FILE_FOLDER");
                    if (pathRow.length() > 0){
                        lrcFileFolder.set(pathRow);
                    }
                }

                if (properties.containsKey("LRC_ZIP_FOLDER")){
                    String pathRow=properties.getProperty("LRC_ZIP_FOLDER");
                    if (pathRow.length() > 0){
                        lrcZipFolder.set(pathRow);
                    }
                }
                if (properties.containsKey("LRC_PRIORITY")){
                    String pathRow=properties.getProperty("LRC_PRIORITY");
                    if (pathRow.length() > 0){
                        lrcPriority.set(Boolean.parseBoolean(pathRow));
                    }
                }

            }


            /*=====================黑名单========================*/
            File workBlacklistFile = new File("data/blacklist/works.json");
            if (workBlacklistFile.exists()){
                workBlackList.addAll(objectMapper.readValue(workBlacklistFile,new TypeReference<Set<String>>(){}));
            }
            File tagBlacklistFile = new File("data/blacklist/tags.json");
            if (tagBlacklistFile.exists()){
                tagBlackList.addAll(objectMapper.readValue(tagBlacklistFile,new TypeReference<Set<String>>(){}));
            }
            File textBlacklistFile = new File("data/blacklist/texts.json");
            if (textBlacklistFile.exists()){
                textBlackList.addAll(objectMapper.readValue(textBlacklistFile,new TypeReference<Set<String>>(){}));
            }

            /*=====================播放设置========================*/
            if (properties.containsKey("USE_VLC_PLAYER")){
                String pathRow=properties.getProperty("USE_VLC_PLAYER");
                if (pathRow.length() > 0){
                    useVlcPlayer.set(Boolean.parseBoolean(pathRow));
                }
            }

            if (properties.containsKey("STOP_PLAY_ON_END")){
                String pathRow=properties.getProperty("STOP_PLAY_ON_END");
                if (pathRow.length() > 0){
                    stopPlayOnEnd.set(Boolean.parseBoolean(pathRow));
                }
            }


            /*=====================下载设置========================*/
            if (properties.containsKey("DOWNLOAD_DIR")){
                String pathRow=properties.getProperty("DOWNLOAD_DIR");
                if (pathRow.length() > 0){
                    downloadDir.set(pathRow);
                }
            }

            if (properties.containsKey("ARIA2_HOST")){
                String pathRow=properties.getProperty("ARIA2_HOST");
                if (pathRow.length() > 0){
                    aria2Host.set(pathRow);
                }
            }

            if (properties.containsKey("ARIA2_RPC_KEY")){
                String pathRow=properties.getProperty("ARIA2_RPC_KEY");
                if (pathRow.length() > 0){
                    ariaRPCKey.set(pathRow);
                }
            }

            if (properties.containsKey("SAVE_NAME_TEMPLATE")){
                String pathRow=properties.getProperty("SAVE_NAME_TEMPLATE");
                if (pathRow.length() > 0){
                    saveNameTemplate.set(pathRow);
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }




    }




    /**
     * @description: 保存配置到本地
     * @name: saveProperties
     * @author: Leck
     * @param: close   程序退出
     * @return  void
     * @date:   2023/3/2
     */
    public static void saveProperties(){
        try( OutputStream outputStream=new FileOutputStream("config.properties")) {
            /*=======================基本======================*/
            properties.setProperty("CONFIG_VERSION", String.valueOf(version.get()));
            properties.setProperty("IGNORE_VERSION", String.valueOf(ignoreVersion.get()));
            properties.setProperty("AUTO_CHECK_VERSION", String.valueOf(autoCheckVersion.get()));
            properties.setProperty("STAGE_WIDTH", String.valueOf(stageWidth.get()));
            properties.setProperty("STAGE_HEIGHT", String.valueOf(stageHeight.get()));

            properties.setProperty("GRID_ORDER", String.valueOf(gridOrder.get()));
            properties.setProperty("GRID_SORT_DESC_MODEL", String.valueOf(gridSortDescModel.get()));
            properties.setProperty("GRID_SUBTITLE_MODEL", String.valueOf(gridSubtitleModel.get()));


            /*=====================服务器========================*/
            if (HOST.get() != null){
                properties.setProperty("HOST", HOST.get());
            }
            if (TOKEN.get() != null){
                properties.setProperty("TOKEN", TOKEN.get());
            }
            properties.setProperty("PROXY_MODEL", String.valueOf(proxyModel.get()));
            if (proxyHost.get() != null){
                properties.setProperty("PROXY_HOST", proxyHost.get());
            }
            if (proxyPort.get() != null){
                properties.setProperty("PROXY_PORT", proxyPort.get());
            }

            /*=====================播放界面========================*/
            properties.setProperty("DETAIL_ALBUM_RADIUS_MODEL", String.valueOf(detailAlbumRadiusModel.get()));
            properties.setProperty("DETAIL_ALBUM_RADIUS_SIZE", String.valueOf(detailAlbumRadiusSize.get()));
            properties.setProperty("DETAIL_ALBUM_EFFECT_MODEL", String.valueOf(detailAlbumEffectModel.get()));
            properties.setProperty("DETAIL_ALBUM_EFFECT_SIZE", String.valueOf(detailAlbumEffectSize.get()));
            properties.setProperty("DETAIL_LRC_ALIGNMENT", String.valueOf(detailLrcAlignment.get()));
            properties.setProperty("DETAIL_GAUSSIAN_SIZE", String.valueOf(detailGaussianSize.get()));
            properties.setProperty("DETAIL_DARKER_SIZE", String.valueOf(detailDarkerSize.get()));



            properties.setProperty("DESKTOP_LRC_FONT_SIZE", String.valueOf(desktopLRCFontSize.get()));
            properties.setProperty("DESKTOP_LRC_FONT_COLOR", String.valueOf(desktopLRCFontColor.get()));
            properties.setProperty("DESKTOP_LRC_BORDER_COLOR", String.valueOf(desktopLRCBorderColor.get()));
            properties.setProperty("DESKTOP_LRC_BORDER_MODEL", String.valueOf(desktopLRCBorderModel.get()));
            properties.setProperty("DESKTOP_LRC_STROKE_COLOR", String.valueOf(desktopLRCStrokeColor.get()));
            properties.setProperty("DESKTOP_LRC_STROKE_MODEL", String.valueOf(desktopLRCStrokeModel.get()));
            properties.setProperty("DESKTOP_LRC_BOLD_MODEL", String.valueOf(desktopLRCBoldModel.get()));

            /*=====================字幕========================*/
            if (lrcZipFolder.get() != null){
                properties.setProperty("LRC_ZIP_FOLDER", lrcZipFolder.get());
            }
            if (lrcFileFolder.get() != null){
                properties.setProperty("LRC_FILE_FOLDER", lrcFileFolder.get());
            }
            properties.setProperty("LRC_PRIORITY", String.valueOf(lrcPriority.get()));


            if (downloadDir.get() != null){
                properties.setProperty("DOWNLOAD_DIR", String.valueOf(downloadDir.get()));
            }

            if (aria2Host.get() != null){
                properties.setProperty("ARIA2_HOST", String.valueOf(aria2Host.get()));
            }
            if (ariaRPCKey.get() != null){
                properties.setProperty("ARIA2_RPC_KEY", String.valueOf(ariaRPCKey.get()));
            }
            if (saveNameTemplate.get() != null){
                properties.setProperty("SAVE_NAME_TEMPLATE", String.valueOf(saveNameTemplate.get()));
            }








            /*=====================黑名单========================*/
            ObjectMapper mapper=new ObjectMapper();
            File file = new File("data/blacklist/works.json");
            if (!file.exists()){
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            mapper.writeValue(file,workBlackList);

            File tagBlacklistFile = new File("data/blacklist/tags.json");
            mapper.writeValue(tagBlacklistFile,tagBlackList);

            File textBlacklistFile = new File("data/blacklist/texts.json");
            mapper.writeValue(textBlacklistFile,textBlackList);
            /*=====================播放========================*/
            properties.setProperty("USE_VLC_PLAYER", String.valueOf(useVlcPlayer.get()));
            properties.setProperty("STOP_PLAY_ON_END", String.valueOf(stopPlayOnEnd.get()));

            properties.store(outputStream,null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}