package cn.tealc995.kkmaid.config;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.io.File;
import java.util.Set;

/**
 * @program: KK-Maid
 * @description:
 * @author: Leck
 * @create: 2025-02-03 23:00
 */
public class Setting {
    /*========================基本========================*/
    private final SimpleStringProperty version=new SimpleStringProperty("1.3");//当前版本
    private final SimpleStringProperty configVersion=new SimpleStringProperty();//配置文件中的版本
    private final SimpleStringProperty ignoreVersion=new SimpleStringProperty();
    private final SimpleBooleanProperty autoCheckVersion=new SimpleBooleanProperty(false);//检查更新
    private final SimpleDoubleProperty stageWidth=new SimpleDoubleProperty(1600.0);
    private final SimpleDoubleProperty stageHeight=new SimpleDoubleProperty(960.0);

    private final SimpleBooleanProperty gridSortDescModel=new SimpleBooleanProperty(true);
    private final SimpleBooleanProperty gridSubtitleModel=new SimpleBooleanProperty(false);
    private final SimpleStringProperty gridOrder=new SimpleStringProperty("create_date");

    /*========================服务器========================*/
    private final SimpleStringProperty HOST=new SimpleStringProperty();
    private final SimpleStringProperty TOKEN=new SimpleStringProperty();
    private final SimpleBooleanProperty proxyModel=new SimpleBooleanProperty(false);//开启代理
    private final SimpleStringProperty proxyHost=new SimpleStringProperty();//代理地址
    private final SimpleStringProperty proxyPort=new SimpleStringProperty();//
    /*===============桌面歌词=================*/
    private final SimpleIntegerProperty desktopLRCStageWidth=new SimpleIntegerProperty(1000);//桌面歌词字体大小
    private final SimpleIntegerProperty desktopLRCFontSize=new SimpleIntegerProperty(30);//桌面歌词字体大小
    private final SimpleStringProperty desktopLRCFontColor=new SimpleStringProperty("#FFFFFF");//桌面歌词字体颜色
    private final SimpleBooleanProperty desktopLRCBorderModel=new SimpleBooleanProperty(true);//开启桌面歌词字体边框，默认开启
    private final SimpleStringProperty desktopLRCBorderColor=new SimpleStringProperty("#00b8d4");//桌面歌词字体边框颜色
    private final SimpleBooleanProperty desktopLRCStrokeModel=new SimpleBooleanProperty(true);//开启桌面歌词字体描边，默认开启
    private final SimpleStringProperty desktopLRCStrokeColor=new SimpleStringProperty("#00b8d4");//桌面歌词字体描边颜色
    private final SimpleBooleanProperty desktopLRCBoldModel=new SimpleBooleanProperty(false);//开启桌面歌词字体粗体,默认关闭




    /*==============播放界面=================*/
    private final SimpleBooleanProperty detailAlbumEffectModel=new SimpleBooleanProperty(true);//设置播放界面封面阴影，默认开启
    private final SimpleDoubleProperty detailAlbumEffectSize=new SimpleDoubleProperty(2);//播放界面封面圆角大小
    private final SimpleBooleanProperty detailAlbumRadiusModel=new SimpleBooleanProperty(true);//播放界面封面圆角
    private final SimpleDoubleProperty detailAlbumRadiusSize=new SimpleDoubleProperty(30);//播放界面封面圆角大小
    private final SimpleBooleanProperty detailLrcAlignment=new SimpleBooleanProperty(false);//设置播放界面歌词对其方式，默认左对齐false,true为居中
    private final SimpleIntegerProperty detailGaussianSize=new SimpleIntegerProperty(25);//高斯模糊大小
    private final SimpleDoubleProperty detailDarkerSize=new SimpleDoubleProperty(0.5f);//暗角
    /*==============字幕=================*/
    private final SimpleStringProperty lrcFileFolder=new SimpleStringProperty();
    private final SimpleStringProperty lrcZipFolder=new SimpleStringProperty();
    private final SimpleBooleanProperty lrcPriority=new SimpleBooleanProperty(false);

    /*==============播放设置=================*/
    private final SimpleBooleanProperty useVlcPlayer=new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty stopPlayOnEnd=new SimpleBooleanProperty(true);//作品播放到最后自动停止播放

    /*==============下载设置=================*/
    private final SimpleStringProperty downloadDir=new SimpleStringProperty();//下载目录
    private final SimpleStringProperty aria2Host=new SimpleStringProperty();//aria2
    private final SimpleStringProperty ariaRPCKey=new SimpleStringProperty();//aria2授权密钥
    private final SimpleStringProperty saveNameTemplate=new SimpleStringProperty("{RJ}");//命名模板





















    public String getVersion() {
        return version.get();
    }

    public SimpleStringProperty versionProperty() {
        return version;
    }

    public void setVersion(String version) {
        this.version.set(version);
    }

    public String getConfigVersion() {
        return configVersion.get();
    }

    public SimpleStringProperty configVersionProperty() {
        return configVersion;
    }

    public void setConfigVersion(String configVersion) {
        this.configVersion.set(configVersion);
    }

    public String getIgnoreVersion() {
        return ignoreVersion.get();
    }

    public SimpleStringProperty ignoreVersionProperty() {
        return ignoreVersion;
    }

    public void setIgnoreVersion(String ignoreVersion) {
        this.ignoreVersion.set(ignoreVersion);
    }

    public boolean isAutoCheckVersion() {
        return autoCheckVersion.get();
    }

    public SimpleBooleanProperty autoCheckVersionProperty() {
        return autoCheckVersion;
    }

    public void setAutoCheckVersion(boolean autoCheckVersion) {
        this.autoCheckVersion.set(autoCheckVersion);
    }

    public double getStageWidth() {
        return stageWidth.get();
    }

    public SimpleDoubleProperty stageWidthProperty() {
        return stageWidth;
    }

    public void setStageWidth(double stageWidth) {
        this.stageWidth.set(stageWidth);
    }

    public double getStageHeight() {
        return stageHeight.get();
    }

    public SimpleDoubleProperty stageHeightProperty() {
        return stageHeight;
    }

    public void setStageHeight(double stageHeight) {
        this.stageHeight.set(stageHeight);
    }

    public boolean isGridSortDescModel() {
        return gridSortDescModel.get();
    }

    public SimpleBooleanProperty gridSortDescModelProperty() {
        return gridSortDescModel;
    }

    public void setGridSortDescModel(boolean gridSortDescModel) {
        this.gridSortDescModel.set(gridSortDescModel);
    }

    public boolean isGridSubtitleModel() {
        return gridSubtitleModel.get();
    }

    public SimpleBooleanProperty gridSubtitleModelProperty() {
        return gridSubtitleModel;
    }

    public void setGridSubtitleModel(boolean gridSubtitleModel) {
        this.gridSubtitleModel.set(gridSubtitleModel);
    }

    public String getGridOrder() {
        return gridOrder.get();
    }

    public SimpleStringProperty gridOrderProperty() {
        return gridOrder;
    }

    public void setGridOrder(String gridOrder) {
        this.gridOrder.set(gridOrder);
    }

    public String getHOST() {
        return HOST.get();
    }

    public SimpleStringProperty HOSTProperty() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST.set(HOST);
    }

    public String getTOKEN() {
        return TOKEN.get();
    }

    public SimpleStringProperty TOKENProperty() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN.set(TOKEN);
    }

    public boolean isProxyModel() {
        return proxyModel.get();
    }

    public SimpleBooleanProperty proxyModelProperty() {
        return proxyModel;
    }

    public void setProxyModel(boolean proxyModel) {
        this.proxyModel.set(proxyModel);
    }

    public String getProxyHost() {
        return proxyHost.get();
    }

    public SimpleStringProperty proxyHostProperty() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost.set(proxyHost);
    }

    public String getProxyPort() {
        return proxyPort.get();
    }

    public SimpleStringProperty proxyPortProperty() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort.set(proxyPort);
    }

    public int getDesktopLRCStageWidth() {
        return desktopLRCStageWidth.get();
    }

    public SimpleIntegerProperty desktopLRCStageWidthProperty() {
        return desktopLRCStageWidth;
    }

    public void setDesktopLRCStageWidth(int desktopLRCStageWidth) {
        this.desktopLRCStageWidth.set(desktopLRCStageWidth);
    }

    public int getDesktopLRCFontSize() {
        return desktopLRCFontSize.get();
    }

    public SimpleIntegerProperty desktopLRCFontSizeProperty() {
        return desktopLRCFontSize;
    }

    public void setDesktopLRCFontSize(int desktopLRCFontSize) {
        this.desktopLRCFontSize.set(desktopLRCFontSize);
    }

    public String getDesktopLRCFontColor() {
        return desktopLRCFontColor.get();
    }

    public SimpleStringProperty desktopLRCFontColorProperty() {
        return desktopLRCFontColor;
    }

    public void setDesktopLRCFontColor(String desktopLRCFontColor) {
        this.desktopLRCFontColor.set(desktopLRCFontColor);
    }

    public boolean isDesktopLRCBorderModel() {
        return desktopLRCBorderModel.get();
    }

    public SimpleBooleanProperty desktopLRCBorderModelProperty() {
        return desktopLRCBorderModel;
    }

    public void setDesktopLRCBorderModel(boolean desktopLRCBorderModel) {
        this.desktopLRCBorderModel.set(desktopLRCBorderModel);
    }

    public String getDesktopLRCBorderColor() {
        return desktopLRCBorderColor.get();
    }

    public SimpleStringProperty desktopLRCBorderColorProperty() {
        return desktopLRCBorderColor;
    }

    public void setDesktopLRCBorderColor(String desktopLRCBorderColor) {
        this.desktopLRCBorderColor.set(desktopLRCBorderColor);
    }

    public boolean isDesktopLRCStrokeModel() {
        return desktopLRCStrokeModel.get();
    }

    public SimpleBooleanProperty desktopLRCStrokeModelProperty() {
        return desktopLRCStrokeModel;
    }

    public void setDesktopLRCStrokeModel(boolean desktopLRCStrokeModel) {
        this.desktopLRCStrokeModel.set(desktopLRCStrokeModel);
    }

    public String getDesktopLRCStrokeColor() {
        return desktopLRCStrokeColor.get();
    }

    public SimpleStringProperty desktopLRCStrokeColorProperty() {
        return desktopLRCStrokeColor;
    }

    public void setDesktopLRCStrokeColor(String desktopLRCStrokeColor) {
        this.desktopLRCStrokeColor.set(desktopLRCStrokeColor);
    }

    public boolean isDesktopLRCBoldModel() {
        return desktopLRCBoldModel.get();
    }

    public SimpleBooleanProperty desktopLRCBoldModelProperty() {
        return desktopLRCBoldModel;
    }

    public void setDesktopLRCBoldModel(boolean desktopLRCBoldModel) {
        this.desktopLRCBoldModel.set(desktopLRCBoldModel);
    }

    public boolean isDetailAlbumEffectModel() {
        return detailAlbumEffectModel.get();
    }

    public SimpleBooleanProperty detailAlbumEffectModelProperty() {
        return detailAlbumEffectModel;
    }

    public void setDetailAlbumEffectModel(boolean detailAlbumEffectModel) {
        this.detailAlbumEffectModel.set(detailAlbumEffectModel);
    }

    public double getDetailAlbumEffectSize() {
        return detailAlbumEffectSize.get();
    }

    public SimpleDoubleProperty detailAlbumEffectSizeProperty() {
        return detailAlbumEffectSize;
    }

    public void setDetailAlbumEffectSize(double detailAlbumEffectSize) {
        this.detailAlbumEffectSize.set(detailAlbumEffectSize);
    }

    public boolean isDetailAlbumRadiusModel() {
        return detailAlbumRadiusModel.get();
    }

    public SimpleBooleanProperty detailAlbumRadiusModelProperty() {
        return detailAlbumRadiusModel;
    }

    public void setDetailAlbumRadiusModel(boolean detailAlbumRadiusModel) {
        this.detailAlbumRadiusModel.set(detailAlbumRadiusModel);
    }

    public double getDetailAlbumRadiusSize() {
        return detailAlbumRadiusSize.get();
    }

    public SimpleDoubleProperty detailAlbumRadiusSizeProperty() {
        return detailAlbumRadiusSize;
    }

    public void setDetailAlbumRadiusSize(double detailAlbumRadiusSize) {
        this.detailAlbumRadiusSize.set(detailAlbumRadiusSize);
    }

    public boolean isDetailLrcAlignment() {
        return detailLrcAlignment.get();
    }

    public SimpleBooleanProperty detailLrcAlignmentProperty() {
        return detailLrcAlignment;
    }

    public void setDetailLrcAlignment(boolean detailLrcAlignment) {
        this.detailLrcAlignment.set(detailLrcAlignment);
    }

    public int getDetailGaussianSize() {
        return detailGaussianSize.get();
    }

    public SimpleIntegerProperty detailGaussianSizeProperty() {
        return detailGaussianSize;
    }

    public void setDetailGaussianSize(int detailGaussianSize) {
        this.detailGaussianSize.set(detailGaussianSize);
    }

    public double getDetailDarkerSize() {
        return detailDarkerSize.get();
    }

    public SimpleDoubleProperty detailDarkerSizeProperty() {
        return detailDarkerSize;
    }

    public void setDetailDarkerSize(double detailDarkerSize) {
        this.detailDarkerSize.set(detailDarkerSize);
    }

    public String getLrcFileFolder() {
        return lrcFileFolder.get();
    }

    public SimpleStringProperty lrcFileFolderProperty() {
        return lrcFileFolder;
    }

    public void setLrcFileFolder(String lrcFileFolder) {
        this.lrcFileFolder.set(lrcFileFolder);
    }

    public String getLrcZipFolder() {
        return lrcZipFolder.get();
    }

    public SimpleStringProperty lrcZipFolderProperty() {
        return lrcZipFolder;
    }

    public void setLrcZipFolder(String lrcZipFolder) {
        this.lrcZipFolder.set(lrcZipFolder);
    }

    public boolean isLrcPriority() {
        return lrcPriority.get();
    }

    public SimpleBooleanProperty lrcPriorityProperty() {
        return lrcPriority;
    }

    public void setLrcPriority(boolean lrcPriority) {
        this.lrcPriority.set(lrcPriority);
    }

    public boolean isUseVlcPlayer() {
        return useVlcPlayer.get();
    }

    public SimpleBooleanProperty useVlcPlayerProperty() {
        return useVlcPlayer;
    }

    public void setUseVlcPlayer(boolean useVlcPlayer) {
        this.useVlcPlayer.set(useVlcPlayer);
    }

    public boolean isStopPlayOnEnd() {
        return stopPlayOnEnd.get();
    }

    public SimpleBooleanProperty stopPlayOnEndProperty() {
        return stopPlayOnEnd;
    }

    public void setStopPlayOnEnd(boolean stopPlayOnEnd) {
        this.stopPlayOnEnd.set(stopPlayOnEnd);
    }

    public String getDownloadDir() {
        return downloadDir.get();
    }

    public SimpleStringProperty downloadDirProperty() {
        return downloadDir;
    }

    public void setDownloadDir(String downloadDir) {
        this.downloadDir.set(downloadDir);
    }

    public String getAria2Host() {
        return aria2Host.get();
    }

    public SimpleStringProperty aria2HostProperty() {
        return aria2Host;
    }

    public void setAria2Host(String aria2Host) {
        this.aria2Host.set(aria2Host);
    }

    public String getAriaRPCKey() {
        return ariaRPCKey.get();
    }

    public SimpleStringProperty ariaRPCKeyProperty() {
        return ariaRPCKey;
    }

    public void setAriaRPCKey(String ariaRPCKey) {
        this.ariaRPCKey.set(ariaRPCKey);
    }

    public String getSaveNameTemplate() {
        return saveNameTemplate.get();
    }

    public SimpleStringProperty saveNameTemplateProperty() {
        return saveNameTemplate;
    }

    public void setSaveNameTemplate(String saveNameTemplate) {
        this.saveNameTemplate.set(saveNameTemplate);
    }
}