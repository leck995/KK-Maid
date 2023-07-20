package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.api.UserApi;
import cn.tealc995.asmronline.api.model.SortType;
import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.MainCenterEvent;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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


    }





    public void save(){
        Config.saveProperties();
        EventBusUtil.getDefault().post(new MainCenterEvent(null,false));
    }

    public void cancel(){
        EventBusUtil.getDefault().post(new MainCenterEvent(null,false));
    }

    public String login(String username,String password){
        String login = UserApi.login(Config.HOST.get(), username, password);
        if (login != null){
            token.set(login);
        }
        return login;
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
}