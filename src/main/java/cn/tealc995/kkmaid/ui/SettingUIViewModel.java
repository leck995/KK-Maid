package cn.tealc995.kkmaid.ui;

import cn.tealc995.kkmaid.Config;
import cn.tealc995.api.UserApi;
import cn.tealc995.api.model.SortType;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainCenterEvent;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-15 03:46
 */
public class SettingUIViewModel {

    /*========================基本========================*/
    private SimpleBooleanProperty autoCheckVersion;
    private SimpleDoubleProperty stageWidth;
    private SimpleDoubleProperty stageHeight;


    private ObservableList<SortType> sortItems;
    private SimpleObjectProperty<SortType> gridOrder;
    private SimpleBooleanProperty gridSubtitle;
    private SimpleBooleanProperty gridDesc;


    /*=================播放界面======================*/
    private SimpleIntegerProperty gaussianSize;//高斯模糊

    private SimpleDoubleProperty darkerSize;;//暗角
    private SimpleBooleanProperty detailAlbumEffectModel;//设置播放界面封面阴影，默认开启
    private  SimpleDoubleProperty detailAlbumEffectSize;//播放界面封面圆角大小
    private SimpleBooleanProperty detailAlbumRadiusModel;//播放界面封面圆角
    private SimpleDoubleProperty detailAlbumRadiusSize;//播放界面封面圆角大小
    private  SimpleBooleanProperty detailLrcAlignment;//设置播放界面歌词对其方式，默认左对齐false,true为居中



    /*=================服务器======================*/
    private SimpleStringProperty host;
    private SimpleStringProperty token;
    private  SimpleBooleanProperty proxyModel;
    private  SimpleStringProperty proxyHost;
    private  SimpleStringProperty proxyPort;

    /*===============桌面歌词=================*/
    private SimpleIntegerProperty desktopLRCStageWidth;
    private SimpleIntegerProperty desktopLRCFontSize;
    private SimpleStringProperty desktopLRCFontColor;
    private SimpleBooleanProperty desktopLRCBorderModel;
    private SimpleStringProperty desktopLRCBorderColor;

    private SimpleBooleanProperty desktopLRCStrokeModel;
    private SimpleStringProperty desktopLRCStrokeColor;
    private SimpleBooleanProperty desktopLRCBoldModel;



    private SimpleStringProperty lrcZipFolder;
    private SimpleStringProperty lrcFileFolder;
    private SimpleStringProperty workBlackList;
    private SimpleStringProperty tagBlackList;
    private SimpleStringProperty textBlackList;
    private SimpleIntegerProperty selectPlayerIndex;
    private ObservableList<String> players;
    private SimpleBooleanProperty stopPlayOnEnd;


    /*===============下载=================*/
    private SimpleStringProperty downloadDir;//下载目录
    private SimpleStringProperty aria2Host;//aria2
    private SimpleStringProperty ariaRPCKey;//aria2授权密钥
    private SimpleStringProperty saveNameTemplate;//命名模板


    public SettingUIViewModel() {
        /*========================基本========================*/
        autoCheckVersion=new SimpleBooleanProperty();
        stageWidth=new SimpleDoubleProperty();
        stageHeight=new SimpleDoubleProperty();

        autoCheckVersion.bindBidirectional(Config.autoCheckVersion);
        stageWidth.bindBidirectional(Config.stageWidth);
        stageHeight.bindBidirectional(Config.stageHeight);

        sortItems= FXCollections.observableArrayList(SortType.release,SortType.create_date,SortType.nsfw,SortType.rate_average_2dp,SortType.dl_count,SortType.price,SortType.review_count,SortType.random);
        gridOrder=new SimpleObjectProperty<>(SortType.valueOf(Config.gridOrder.get()));
        gridOrder.addListener((observableValue, sortType, t1) -> Config.gridOrder.set(t1.name()));


        gridDesc=new SimpleBooleanProperty();
        gridSubtitle=new SimpleBooleanProperty();
        gridSubtitle.bindBidirectional(Config.gridSubtitleModel);
        gridDesc.bindBidirectional(Config.gridSortDescModel);
        /*=================播放界面======================*/

        gaussianSize=new SimpleIntegerProperty();
        gaussianSize.bindBidirectional(Config.detailGaussianSize);

        darkerSize=new SimpleDoubleProperty();
        darkerSize.bindBidirectional(Config.detailDarkerSize);

        detailAlbumEffectSize=new SimpleDoubleProperty();
        detailAlbumEffectSize.bindBidirectional(Config.detailAlbumEffectSize);

        detailAlbumEffectModel=new SimpleBooleanProperty();
        detailAlbumEffectModel.bindBidirectional(Config.detailAlbumEffectModel);

        detailAlbumRadiusModel=new SimpleBooleanProperty();
        detailAlbumRadiusModel.bindBidirectional(Config.detailAlbumRadiusModel);

        detailAlbumRadiusSize=new SimpleDoubleProperty();
        detailAlbumRadiusSize.bindBidirectional(Config.detailAlbumRadiusSize);


        /*=================服务器======================*/
        host=new SimpleStringProperty();
        token=new SimpleStringProperty();
        proxyModel=new SimpleBooleanProperty();
        proxyHost=new SimpleStringProperty();
        proxyPort=new SimpleStringProperty();
        host.bindBidirectional(Config.HOST);
        token.bindBidirectional(Config.TOKEN);
        proxyModel.bindBidirectional(Config.proxyModel);
        proxyHost.bindBidirectional(Config.proxyHost);
        proxyPort.bindBidirectional(Config.proxyPort);

        /*===============桌面歌词=================*/
        desktopLRCStageWidth=new SimpleIntegerProperty();
        desktopLRCFontSize=new SimpleIntegerProperty();
        desktopLRCFontColor=new SimpleStringProperty();
        desktopLRCBorderModel=new SimpleBooleanProperty();
        desktopLRCBorderColor=new SimpleStringProperty();

        desktopLRCStrokeModel=new SimpleBooleanProperty();
        desktopLRCStrokeColor=new SimpleStringProperty();
        desktopLRCBoldModel=new SimpleBooleanProperty();

        desktopLRCStageWidth.bindBidirectional(Config.desktopLRCStageWidth);
        desktopLRCFontSize.bindBidirectional(Config.desktopLRCFontSize);
        desktopLRCFontColor.bindBidirectional(Config.desktopLRCFontColor);
        desktopLRCBorderModel.bindBidirectional(Config.desktopLRCBorderModel);
        desktopLRCBorderColor.bindBidirectional(Config.desktopLRCBorderColor);
        desktopLRCStrokeModel.bindBidirectional(Config.desktopLRCStrokeModel);

        desktopLRCStrokeColor.bindBidirectional(Config.desktopLRCStrokeColor);
        desktopLRCBoldModel.bindBidirectional(Config.desktopLRCBoldModel);


        /*===============字幕=================*/
        lrcFileFolder=new SimpleStringProperty();
        lrcZipFolder=new SimpleStringProperty();
        lrcFileFolder.bindBidirectional(Config.lrcFileFolder);
        lrcZipFolder.bindBidirectional(Config.lrcZipFolder);


        /*===============黑名单=================*/
        workBlackList=new SimpleStringProperty();
        StringBuilder workBuilder=new StringBuilder();
        for (String s : Config.workBlackList) {
            workBuilder.append("RJ").append(s).append(" ");
        }
        workBlackList.set(workBuilder.toString());

        tagBlackList=new SimpleStringProperty();
        StringBuilder tagBuilder=new StringBuilder();
        for (String s : Config.tagBlackList) {
            tagBuilder.append(s).append(" ");
        }
        tagBlackList.set(tagBuilder.toString());

        textBlackList=new SimpleStringProperty();
        StringBuilder textBuilder=new StringBuilder();
        for (String s : Config.textBlackList) {
            textBuilder.append(s).append(System.lineSeparator());
        }
        textBlackList.set(textBuilder.toString());

        /*===============播放=================*/
        selectPlayerIndex=new SimpleIntegerProperty();
        if (Config.useVlcPlayer.get()){
            selectPlayerIndex.set(1);
        }else {
            selectPlayerIndex.set(0);
        }
        players=FXCollections.observableArrayList("默认","VLC播放器");
        stopPlayOnEnd=new SimpleBooleanProperty();
        stopPlayOnEnd.bindBidirectional(Config.stopPlayOnEnd);



        downloadDir=new SimpleStringProperty();//下载目录
        downloadDir.bindBidirectional(Config.downloadDir);
        aria2Host=new SimpleStringProperty();//aria2
        aria2Host.bindBidirectional(Config.aria2Host);
        ariaRPCKey=new SimpleStringProperty();//aria2授权密钥
        ariaRPCKey.bindBidirectional(Config.ariaRPCKey);
        saveNameTemplate=new SimpleStringProperty("{RJ}");//命名模板
        saveNameTemplate.bindBidirectional(Config.saveNameTemplate);

    }





    public void save(){
        updateBlackList();
        updateSelectPlayer();

        Config.saveProperties();
        EventBusUtil.getDefault().post(new MainCenterEvent(null,false,false));
    }







    public void cancel(){
        EventBusUtil.getDefault().post(new MainCenterEvent(null,false,false));
    }

    public String login(String username,String password){
        String login = UserApi.login(Config.HOST.get(), username, password);
        if (login != null){
            token.set(login);
        }
        return login;
    }



    private void updateBlackList(){
        String rj = workBlackList.get().toUpperCase().replaceAll("RJ", "");
        String[] s = rj.split(" ");
        Config.workBlackList.clear();
        Config.workBlackList.addAll(Arrays.stream(s).toList());
        String tag = tagBlackList.get().toUpperCase().replaceAll("RJ", "");
        String[] tags = tag.split(" ");
        Config.tagBlackList.clear();
        Config.tagBlackList.addAll(Arrays.stream(tags).toList());

        String[] texts = textBlackList.get().split(System.lineSeparator());
        Config.textBlackList.clear();
        Config.textBlackList.addAll(Arrays.stream(texts).toList());
    }



    /**
     * @description: 设置播放器内核
     * @name: updateSelectPlayer
     * @author: Leck
     * @param:	index
     * @return  void
     * @date:   2023/8/13
     */
    private void updateSelectPlayer(){
        Config.useVlcPlayer.set(selectPlayerIndex.get() == 1);
    }


    public void setLrcZipFolder(String lrcZipFolder) {
        this.lrcZipFolder.set(lrcZipFolder);
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

    public String getLrcFileFolder() {
        return lrcFileFolder.get();
    }

    public SimpleStringProperty lrcFileFolderProperty() {
        return lrcFileFolder;
    }

    public boolean isGridSubtitle() {
        return gridSubtitle.get();
    }

    public SimpleBooleanProperty gridSubtitleProperty() {
        return gridSubtitle;
    }

    public boolean isGridDesc() {
        return gridDesc.get();
    }

    public SimpleBooleanProperty gridDescProperty() {
        return gridDesc;
    }

    public SortType getGridOrder() {
        return gridOrder.get();
    }

    public SimpleObjectProperty<SortType> gridOrderProperty() {
        return gridOrder;
    }

    public void setGridOrder(SortType gridOrder) {
        this.gridOrder.set(gridOrder);
    }

    public ObservableList<SortType> getSortItems() {
        return sortItems;
    }

    public boolean isAutoCheckVersion() {
        return autoCheckVersion.get();
    }

    public SimpleBooleanProperty autoCheckVersionProperty() {
        return autoCheckVersion;
    }

    public boolean isProxyModel() {
        return proxyModel.get();
    }

    public SimpleBooleanProperty proxyModelProperty() {
        return proxyModel;
    }

    public String getProxyHost() {
        return proxyHost.get();
    }

    public SimpleStringProperty proxyHostProperty() {
        return proxyHost;
    }

    public String getProxyPort() {
        return proxyPort.get();
    }

    public SimpleStringProperty proxyPortProperty() {
        return proxyPort;
    }

    public String getHost() {
        return host.get();
    }

    public SimpleStringProperty hostProperty() {
        return host;
    }

    public String getToken() {
        return token.get();
    }

    public SimpleStringProperty tokenProperty() {
        return token;
    }

    public int getDesktopLRCStageWidth() {
        return desktopLRCStageWidth.get();
    }

    public SimpleIntegerProperty desktopLRCStageWidthProperty() {
        return desktopLRCStageWidth;
    }

    public int getDesktopLRCFontSize() {
        return desktopLRCFontSize.get();
    }

    public SimpleIntegerProperty desktopLRCFontSizeProperty() {
        return desktopLRCFontSize;
    }

    public String getDesktopLRCFontColor() {
        return desktopLRCFontColor.get();
    }

    public SimpleStringProperty desktopLRCFontColorProperty() {
        return desktopLRCFontColor;
    }

    public boolean isDesktopLRCBorderModel() {
        return desktopLRCBorderModel.get();
    }

    public SimpleBooleanProperty desktopLRCBorderModelProperty() {
        return desktopLRCBorderModel;
    }

    public String getDesktopLRCBorderColor() {
        return desktopLRCBorderColor.get();
    }

    public SimpleStringProperty desktopLRCBorderColorProperty() {
        return desktopLRCBorderColor;
    }

    public boolean isDesktopLRCStrokeModel() {
        return desktopLRCStrokeModel.get();
    }

    public SimpleBooleanProperty desktopLRCStrokeModelProperty() {
        return desktopLRCStrokeModel;
    }

    public String getDesktopLRCStrokeColor() {
        return desktopLRCStrokeColor.get();
    }

    public SimpleStringProperty desktopLRCStrokeColorProperty() {
        return desktopLRCStrokeColor;
    }

    public boolean isDesktopLRCBoldModel() {
        return desktopLRCBoldModel.get();
    }

    public SimpleBooleanProperty desktopLRCBoldModelProperty() {
        return desktopLRCBoldModel;
    }

    public void setDesktopLRCFontColor(String desktopLRCFontColor) {
        this.desktopLRCFontColor.set(desktopLRCFontColor);
    }

    public void setDesktopLRCBorderColor(String desktopLRCBorderColor) {
        this.desktopLRCBorderColor.set(desktopLRCBorderColor);
    }

    public void setDesktopLRCStrokeColor(String desktopLRCStrokeColor) {
        this.desktopLRCStrokeColor.set(desktopLRCStrokeColor);
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

    public String getWorkBlackList() {
        return workBlackList.get();
    }

    public SimpleStringProperty workBlackListProperty() {
        return workBlackList;
    }


    public ObservableList<String> getPlayers() {
        return players;
    }

    public int getSelectPlayerIndex() {
        return selectPlayerIndex.get();
    }

    public SimpleIntegerProperty selectPlayerIndexProperty() {
        return selectPlayerIndex;
    }

    public void setSelectPlayerIndex(int selectPlayerIndex) {
        this.selectPlayerIndex.set(selectPlayerIndex);
    }

    public boolean isStopPlayOnEnd() {
        return stopPlayOnEnd.get();
    }

    public SimpleBooleanProperty stopPlayOnEndProperty() {
        return stopPlayOnEnd;
    }

    public double getDarkerSize() {
        return darkerSize.get();
    }

    public SimpleDoubleProperty darkerSizeProperty() {
        return darkerSize;
    }

    public int getGaussianSize() {
        return gaussianSize.get();
    }

    public SimpleIntegerProperty gaussianSizeProperty() {
        return gaussianSize;
    }

    public boolean isDetailAlbumEffectModel() {
        return detailAlbumEffectModel.get();
    }

    public SimpleBooleanProperty detailAlbumEffectModelProperty() {
        return detailAlbumEffectModel;
    }

    public double getDetailAlbumEffectSize() {
        return detailAlbumEffectSize.get();
    }

    public SimpleDoubleProperty detailAlbumEffectSizeProperty() {
        return detailAlbumEffectSize;
    }

    public boolean isDetailAlbumRadiusModel() {
        return detailAlbumRadiusModel.get();
    }

    public SimpleBooleanProperty detailAlbumRadiusModelProperty() {
        return detailAlbumRadiusModel;
    }

    public double getDetailAlbumRadiusSize() {
        return detailAlbumRadiusSize.get();
    }

    public SimpleDoubleProperty detailAlbumRadiusSizeProperty() {
        return detailAlbumRadiusSize;
    }

    public boolean isDetailLrcAlignment() {
        return detailLrcAlignment.get();
    }

    public SimpleBooleanProperty detailLrcAlignmentProperty() {
        return detailLrcAlignment;
    }

    public String getDownloadDir() {
        return downloadDir.get();
    }

    public SimpleStringProperty downloadDirProperty() {
        return downloadDir;
    }

    public String getAria2Host() {
        return aria2Host.get();
    }

    public SimpleStringProperty aria2HostProperty() {
        return aria2Host;
    }

    public String getAriaRPCKey() {
        return ariaRPCKey.get();
    }

    public SimpleStringProperty ariaRPCKeyProperty() {
        return ariaRPCKey;
    }

    public String getSaveNameTemplate() {
        return saveNameTemplate.get();
    }

    public SimpleStringProperty saveNameTemplateProperty() {
        return saveNameTemplate;
    }

    public String getTagBlackList() {
        return tagBlackList.get();
    }

    public SimpleStringProperty tagBlackListProperty() {
        return tagBlackList;
    }

    public String getTextBlackList() {
        return textBlackList.get();
    }

    public SimpleStringProperty textBlackListProperty() {
        return textBlackList;
    }
}