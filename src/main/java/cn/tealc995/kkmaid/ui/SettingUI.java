package cn.tealc995.kkmaid.ui;

import atlantafx.base.controls.*;
import atlantafx.base.theme.Styles;
import cn.tealc995.kkmaid.App;
import cn.tealc995.kkmaid.Config;
import cn.tealc995.kikoreu.model.SortType;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainNotificationEvent;
import cn.tealc995.kkmaid.player.MediaPlayerUtil;
import cn.tealc995.kkmaid.util.AnchorPaneUtil;
import cn.tealc995.kkmaid.util.CssLoader;
import cn.tealc995.kkmaid.util.OSUtil;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jianggujin.registry.JExecResult;
import com.jianggujin.registry.JQueryOptions;
import com.jianggujin.registry.JRegistry;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-15 03:46
 */
public class SettingUI {
    private static final Logger logger= LoggerFactory.getLogger(SettingUI.class);
    private StackPane root;
    private SettingUIViewModel viewModel;

    public SettingUI() {
        viewModel=new SettingUIViewModel();
        root=new StackPane();
        root.getStylesheets().add(CssLoader.getCss(CssLoader.setting));
        root.getStyleClass().add("background");
        BorderPane parent=new BorderPane();

        Label title=new Label("设置");
        title.getStyleClass().add("setting-title");

        TabPane tabPane=new TabPane();
        Tab baseTab=new Tab("基本");
        baseTab.setContent(initBaseTab());
        baseTab.setClosable(false);

        Tab mainTab=new Tab("主界面");
        mainTab.setContent(initMainTab());
        mainTab.setClosable(false);
        Tab playerUITab=new Tab("播放界面");
        playerUITab.setContent(initplayerUITab());
        playerUITab.setClosable(false);

        Tab playerTab=new Tab("播放");
        playerTab.setContent(initPlayerTab());
        playerTab.setClosable(false);

        Tab hostTab=new Tab("服务器");
        hostTab.setContent(initHostTab());
        hostTab.setClosable(false);



        Tab desktopTab=new Tab("桌面歌词");
        desktopTab.setContent(initDesktopTab());
        desktopTab.setClosable(false);

        Tab lrcTab=new Tab("字幕管理");
        lrcTab.setContent(initLrcTab());
        lrcTab.setClosable(false);


        Tab blacklistTab=new Tab("黑名单");
        blacklistTab.setContent(initBlackListTab());
        blacklistTab.setClosable(false);


        Tab downloadTab=new Tab("下载");
        downloadTab.setContent(initDownloadTab());
        downloadTab.setClosable(false);

        Tab aboutTab=new Tab("关于");
        aboutTab.setContent(initAboutTab());
        aboutTab.setClosable(false);
        tabPane.getTabs().addAll(baseTab,mainTab,playerUITab,playerTab,hostTab,desktopTab,lrcTab,blacklistTab,downloadTab,aboutTab);


        Button saveBtn=new Button("保存");
        saveBtn.setOnAction(event -> viewModel.save());
        saveBtn.getStyleClass().add("accent");
        Button cancelBtn=new Button("取消");
        cancelBtn.setOnAction(event -> viewModel.cancel());
        HBox bottomPane=new HBox(saveBtn,cancelBtn);
        bottomPane.setSpacing(20);
        bottomPane.setAlignment(Pos.CENTER_RIGHT);
        bottomPane.setPadding(new Insets(0,40,0,0));




        parent.setTop(title);
        parent.setCenter(tabPane);
        parent.setBottom(bottomPane);




        parent.setPadding(new Insets(10));
        root.getChildren().add(parent);


    }

    private Pane initBaseTab(){
        Label widthLabel=new Label("默认窗口宽度");
        TextField  widthField=new TextField();
        widthField.setText(String.valueOf(viewModel.getStageWidth()));
        widthField.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.length() == 0) return;
            if (!t1.matches("\\d*")) {
                widthField.setText(t1.replaceAll("[^\\d]", ""));
            }else {
                viewModel.setStageWidth(Double.parseDouble(t1));
            }
        });
        Pane pane1 = initRowPane(widthLabel, widthField);

        Label heightLabel=new Label("默认窗口高度");
        TextField heightField=new TextField();
        heightField.setText(String.valueOf(viewModel.getStageHeight()));
        heightField.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.length() == 0) return;
            if (!t1.matches("\\d*")) {
                heightField.setText(t1.replaceAll("[^\\d]", ""));
            }else {
                viewModel.setStageHeight(Double.parseDouble(t1));
            }
        });
        Pane pane2 = initRowPane(heightLabel, heightField);

        Button setSizeButton=new Button("设置当前尺寸为默认尺寸");
        setSizeButton.setOnAction(event -> {
            widthField.setText(String.valueOf((int)App.mainStage.getWidth()));
            heightField.setText(String.valueOf((int)App.mainStage.getHeight()));
        });
        StackPane pane3=new StackPane(setSizeButton);
        pane3.setPadding(new Insets(10,20,10,20));
        pane3.setAlignment(Pos.CENTER_RIGHT);


        Label versionLabel=new Label("检查更新");
        ToggleSwitch versionSwitch=new ToggleSwitch();
        versionSwitch.setDisable(true);
        versionSwitch.selectedProperty().bindBidirectional(viewModel.autoCheckVersionProperty());
        Pane pane4 = initRowPane(versionLabel, versionSwitch);




        VBox vBox=new VBox(pane1,pane2,pane3,new Separator(Orientation.HORIZONTAL),pane4);
        vBox.setPadding(new Insets(10,20,10,20));
        return vBox;
    }

    private Pane initMainTab(){
        Label orderLabel=new Label("排列顺序");
        ChoiceBox<SortType> sortChoiceBox=new ChoiceBox<>(viewModel.getSortItems());
        sortChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(SortType sortType) {
                if (sortType != null)
                    return sortType.getValue();
                else
                    return "";
            }
            @Override
            public SortType fromString(String s) {
                return null;
            }
        });
        sortChoiceBox.getSelectionModel().select(viewModel.getGridOrder());
        sortChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, sortType, t1) -> viewModel.setGridOrder(t1));
        Pane pane1 = initRowPane(orderLabel, sortChoiceBox);

        Label subtitleLabel=new Label("字幕");
        ToggleSwitch subtitleSwitch=new ToggleSwitch();
        subtitleSwitch.selectedProperty().bindBidirectional(viewModel.gridSubtitleProperty());
        Pane pane2 = initRowPane(subtitleLabel, subtitleSwitch);

        Label descLabel=new Label("倒序");
        ToggleSwitch descSwitch=new ToggleSwitch();
        descSwitch.selectedProperty().bindBidirectional(viewModel.gridDescProperty());
        Pane pane3 = initRowPane(descLabel, descSwitch);

        VBox vBox=new VBox(pane1,pane2,pane3);
        vBox.setPadding(new Insets(10,20,10,20));
        return vBox;

    }

    private Pane initplayerUITab(){
        Label gaussianLabel=new Label("高斯模糊(越大越模糊)");
        Slider gaussianSlider=new Slider();
        gaussianSlider.setMin(5);
        gaussianSlider.setMax(80);
        gaussianSlider.setSkin(new ProgressSliderSkin(gaussianSlider));
        gaussianSlider.valueProperty().bindBidirectional(viewModel.gaussianSizeProperty());
        Pane pane1 = initRowPane(gaussianLabel, gaussianSlider);

        Label darkerLabel=new Label("暗角(越大越亮)");
        Slider darkerSlider=new Slider();
        darkerSlider.setMin(0.4);
        darkerSlider.setMax(0.9);
        darkerSlider.setSkin(new ProgressSliderSkin(darkerSlider));
        darkerSlider.valueProperty().bindBidirectional(viewModel.darkerSizeProperty());
        Pane pane2 = initRowPane(darkerLabel, darkerSlider);

        Label radiusModelLabel=new Label("封面圆角");
        ToggleSwitch radiusModelSwitch=new ToggleSwitch();
        radiusModelSwitch.selectedProperty().bindBidirectional(viewModel.detailAlbumRadiusModelProperty());
        Pane pane3 = initRowPane(radiusModelLabel, radiusModelSwitch);


        Label radiusSizeLabel=new Label("圆角尺寸");
        Slider radiusSizeSlider=new Slider();

        radiusSizeSlider.setMin(5);
        radiusSizeSlider.setMax(80);
        radiusSizeSlider.setSkin(new ProgressSliderSkin(radiusSizeSlider));
        radiusSizeSlider.valueProperty().bindBidirectional(viewModel.detailAlbumRadiusSizeProperty());
        Pane pane4 = initRowPane(radiusSizeLabel, radiusSizeSlider);
        pane4.disableProperty().bind(radiusModelSwitch.selectedProperty().not());
/*        Label effectModelLabel=new Label("封面阴影");
        ToggleSwitch effectModelSwitch=new ToggleSwitch();
        effectModelSwitch.selectedProperty().bindBidirectional(viewModel.detailAlbumEffectModelProperty());
        Pane pane5 = initRowPane(effectModelLabel, effectModelSwitch);


        Label effectSizeLabel=new Label("阴影尺寸");
        Slider effectSizeSlider=new Slider();
        effectSizeSlider.setMin(1);
        effectSizeSlider.setMax(20);
        effectSizeSlider.setSkin(new ProgressSliderSkin(effectSizeSlider));
        effectSizeSlider.valueProperty().bindBidirectional(viewModel.detailAlbumEffectSizeProperty());
        Pane pane6 = initRowPane(effectSizeLabel, effectSizeSlider);*/

        VBox vBox=new VBox(pane1,pane2,new Separator(Orientation.HORIZONTAL),pane3,pane4);
        vBox.setPadding(new Insets(10,20,10,20));
        return vBox;

    }
    private Pane initPlayerTab(){

        Label selectPlayerLabel=new Label("播放器内核(推荐使用VLC内核，更改需重启)");
        ChoiceBox<String> selectPlayerChoiceBox=new ChoiceBox<>(viewModel.getPlayers());

        selectPlayerChoiceBox.getSelectionModel().select(viewModel.getSelectPlayerIndex());
        selectPlayerChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) ->{
            int i= t1.intValue();
            viewModel.setSelectPlayerIndex(t1.intValue());
            if (i==1){
                Card card=new Card();

                JFXDialog dialog=new JFXDialog(root,card, JFXDialog.DialogTransition.CENTER);
                dialog.setOverlayClose(false);

                String tip=new String("KK Maid支持使用VLC内核进行播放，使用前请确保电脑中已经安装VLC播放器，否则将无法播放。如未安装请点击下方链接前往官网下载安装(64位电脑安装64位VLC，32位电脑安装32位VLC)");
                Label introductionLabel=new Label(tip);
                introductionLabel.setPrefWidth(550.0);
                introductionLabel.setWrapText(true);
                card.setHeader(introductionLabel);

                introductionLabel.setWrapText(true);
                Hyperlink vlcLink=new Hyperlink("未安装，前往VLC官网下载");
                vlcLink.setOnAction(event -> {
                    try {
                        Desktop.getDesktop().browse(new URI("https://www.videolan.org/vlc/"));
                    } catch (IOException | URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });

                Button cancelBtn=new Button("已安装");
                cancelBtn.setOnAction(event -> dialog.close());


                HBox footPane=new HBox(vlcLink,cancelBtn);
                footPane.setSpacing(15);
                footPane.setAlignment(Pos.CENTER_RIGHT);
                card.setFooter(footPane);

                dialog.show();
            }
        });
        Pane pane1 = initRowPane(selectPlayerLabel, selectPlayerChoiceBox);



        Label checkLabel=new Label("Windows电脑可点击右侧按钮检测是否已安装VLC");
        checkLabel.getStyleClass().add(Styles.TEXT_MUTED);

        Button checkVlcBtn=new Button("检测VLC");
        checkVlcBtn.setOnAction(event ->
            EventBusUtil.getDefault().post(new MainNotificationEvent(installVLC() ? "VLC播放器已安装" : "VLC播放器未安装"))
        );
        checkVlcBtn.setDisable(!OSUtil.isWindows());
        Pane pane2 = initRowPane(checkLabel, checkVlcBtn);



        Label stopPlayOnEndLabel=new Label("作品播放完毕后停止播放");
        ToggleSwitch stopPlayOnEndSwitch=new ToggleSwitch();
        stopPlayOnEndSwitch.selectedProperty().bindBidirectional(viewModel.stopPlayOnEndProperty());
        Pane pane3 = initRowPane(stopPlayOnEndLabel, stopPlayOnEndSwitch);


        VBox vBox=new VBox(pane1,pane2,new Separator(Orientation.HORIZONTAL),pane3);
        vBox.setPadding(new Insets(10,20,10,20));





        return vBox;

    }





    private Pane initHostTab(){
        Popover hostPopover=new Popover(new Label("例如域名是https://baidu.com，那么应填写https://api.baidu.com"));
        Hyperlink hostLink=new Hyperlink("?");
        hostLink.setOnAction(event -> hostPopover.show(hostLink));
        Label hostLabel=new Label("服务器API地址",hostLink);
        hostLabel.setContentDisplay(ContentDisplay.RIGHT);



        TextField hostField=new TextField();
        hostField.setPromptText("https://api.xxxx.xxx");
        hostField.textProperty().bindBidirectional(viewModel.hostProperty());
        Pane pane1 = initRowPane(hostLabel, hostField);

        Label tokenLabel=new Label("账号Token");
        TextField tokenField=new TextField();
        tokenField.textProperty().bindBidirectional(viewModel.tokenProperty());

        Button loginBtn=new Button("获取Token");
        loginBtn.setOnAction(event -> {
            JFXDialog dialog=new JFXDialog();
            JFXDialogLayout dialogLayout=new JFXDialogLayout();
            dialogLayout.setHeading(new Label("登录"));

            TextField nameField=new TextField();
            nameField.setPromptText("用户名");
            TextField passwordField=new PasswordField();
            passwordField.setPromptText("密码");
            VBox vBox=new VBox(nameField,passwordField);
            vBox.setSpacing(15);
            dialogLayout.setBody(vBox);

            dialogLayout.setStyle("-fx-background-color: -color-base-9");

            Button loginBtn1=new Button("登录");
            loginBtn1.setOnAction(event1 -> {
                viewModel.login(nameField.getText(), passwordField.getText(),loginStatus ->{
                   if (loginStatus)
                       dialog.close();
                });
            });
            Button cancel1=new Button("取消");
            cancel1.setOnAction(event1 -> dialog.close());
            dialogLayout.setActions(loginBtn1,cancel1);
            dialog.setContent(dialogLayout);
            dialog.setOverlayClose(false);

            dialog.show(root);
        });
        HBox hBox=new HBox(tokenField,loginBtn);
        hBox.setSpacing(10);
        Pane pane2 = initRowPane(tokenLabel, hBox);


        Label proxyLabel=new Label("使用代理(需要重启)");
        ToggleSwitch proxySwitch=new ToggleSwitch();
        proxySwitch.selectedProperty().bindBidirectional(viewModel.proxyModelProperty());
        Pane pane3 = initRowPane(proxyLabel, proxySwitch);

        Label proxyHostLabel=new Label("代理地址");
        TextField proxyHostField=new TextField();
        proxyHostField.textProperty().bindBidirectional(viewModel.proxyHostProperty());
        Pane pane4 = initRowPane(proxyHostLabel, proxyHostField);
        Label proxyPortLabel=new Label("代理端口");
        TextField proxyPortField=new TextField();
        proxyPortField.textProperty().bindBidirectional(viewModel.proxyPortProperty());
        Pane pane5 = initRowPane(proxyPortLabel, proxyPortField);

        VBox vBox=new VBox(pane1,pane2,new Separator(Orientation.HORIZONTAL),pane3,pane4,pane5);
        vBox.setPadding(new Insets(10,20,10,20));
        return vBox;
    }
    private Pane initDesktopTab(){
        Label showLrcLabel=new Label("显示歌词");
        ToggleSwitch showLrcSwitch=new ToggleSwitch();
        Pane pane1 = initRowPane(showLrcLabel, showLrcSwitch);
        showLrcSwitch.selectedProperty().bindBidirectional(MediaPlayerUtil.mediaPlayer().desktopLrcShowProperty());



        Label fontSizeLabel=new Label("字体大小");
        Slider fontSizeSlider=new Slider();
        fontSizeSlider.setSkin(new ProgressSliderSkin(fontSizeSlider));
        fontSizeSlider.valueProperty().bindBidirectional(viewModel.desktopLRCFontSizeProperty());
        Pane pane2 = initRowPane(fontSizeLabel, fontSizeSlider);

        Label fontColorLabel=new Label("字体颜色");
        ColorPicker fontColorPicker=new ColorPicker();

        fontColorPicker.setValue(Color.web(viewModel.getDesktopLRCFontColor()));
        fontColorPicker.setOnAction(actionEvent -> {
            Color value = fontColorPicker.getValue();
            int var1 = (int)Math.round(value.getRed() * 255.0);
            int var2 = (int)Math.round(value.getGreen() * 255.0);
            int var3 = (int)Math.round(value.getBlue() * 255.0);
            int var4 = (int)Math.round(value.getOpacity() * 255.0);
            viewModel.setDesktopLRCFontColor(String.format("%02x%02x%02x%02x", var1, var2, var3, var4));
        });
        Pane pane3 = initRowPane(fontColorLabel, fontColorPicker);

        Label boldLabel=new Label("粗体");
        ToggleSwitch boldSwitch=new ToggleSwitch();
        boldSwitch.selectedProperty().bindBidirectional(viewModel.desktopLRCBoldModelProperty());
        Pane pane4 = initRowPane(boldLabel, boldSwitch);

        Label shadowLabel=new Label("字体阴影");
        ToggleSwitch shadowSwitch=new ToggleSwitch();
        shadowSwitch.selectedProperty().bindBidirectional(viewModel.desktopLRCBorderModelProperty());
        Pane pane5 = initRowPane(shadowLabel, shadowSwitch);

        Label shadowColorLabel=new Label("阴影颜色");
        ColorPicker shadowColorPicker=new ColorPicker();
        shadowColorPicker.setValue(Color.web(viewModel.getDesktopLRCBorderColor()));
        shadowColorPicker.setOnAction(actionEvent -> {
            Color value = shadowColorPicker.getValue();
            int var1 = (int)Math.round(value.getRed() * 255.0);
            int var2 = (int)Math.round(value.getGreen() * 255.0);
            int var3 = (int)Math.round(value.getBlue() * 255.0);
            int var4 = (int)Math.round(value.getOpacity() * 255.0);
            viewModel.setDesktopLRCBorderColor(String.format("%02x%02x%02x%02x", var1, var2, var3, var4));
        });
        Pane pane6 = initRowPane(shadowColorLabel, shadowColorPicker);




        Label strokeLabel=new Label("描边");
        ToggleSwitch strokeSwitch=new ToggleSwitch();
        strokeSwitch.selectedProperty().bindBidirectional(viewModel.desktopLRCStrokeModelProperty());
        Pane pane7 = initRowPane(strokeLabel, strokeSwitch);

        Label strokeColorLabel=new Label("描边颜色");
        ColorPicker strokeColorPicker=new ColorPicker();
        strokeColorPicker.setValue(Color.web(viewModel.getDesktopLRCStrokeColor()));
        strokeColorPicker.setOnAction(actionEvent -> {
            Color value = strokeColorPicker.getValue();
            int var1 = (int)Math.round(value.getRed() * 255.0);
            int var2 = (int)Math.round(value.getGreen() * 255.0);
            int var3 = (int)Math.round(value.getBlue() * 255.0);
            int var4 = (int)Math.round(value.getOpacity() * 255.0);
            viewModel.setDesktopLRCStrokeColor(String.format("%02x%02x%02x%02x", var1, var2, var3, var4));
        });
        Pane pane8 = initRowPane(strokeColorLabel, strokeColorPicker);


        VBox vBox=new VBox(pane1,new Separator(Orientation.HORIZONTAL),pane2,pane3,pane4,new Separator(Orientation.HORIZONTAL),pane5,pane6,new Separator(Orientation.HORIZONTAL),pane7,pane8);
        vBox.setPadding(new Insets(10,20,10,20));
        return vBox;
    }

    private Pane initLrcTab(){
        Label folderLabel=new Label("字幕根文件夹");
        TextField folderField=new TextField();
        folderField.textProperty().bindBidirectional(viewModel.lrcFileFolderProperty());
        Button folderImportBtn=new Button("导入");
        folderImportBtn.setOnAction(event -> {
            DirectoryChooser directoryChooser=new DirectoryChooser();
            directoryChooser.setTitle("选择字幕根文件夹");
            File file = directoryChooser.showDialog(App.mainStage);
            if (file != null){
                viewModel.setLrcFileFolder(file.getPath());
                System.out.println("aa"+Config.lrcFileFolder.get());
            }
        });
        HBox hBox1=new HBox(folderField,folderImportBtn);
        hBox1.setSpacing(10);
        Pane pane1 = initRowPane(folderLabel, hBox1);

        Label zipLabel=new Label("字幕压缩包文件夹");
        TextField zipField=new TextField();
        zipField.textProperty().bindBidirectional(viewModel.lrcZipFolderProperty());
        Button zipImportBtn=new Button("导入");
        zipImportBtn.setOnAction(event -> {
            DirectoryChooser directoryChooser=new DirectoryChooser();
            directoryChooser.setTitle("选择字幕压缩包文件夹");
            File file = directoryChooser.showDialog(App.mainStage);
            if (file != null){
                viewModel.setLrcZipFolder(file.getPath());
            }
        });
        HBox hBox2=new HBox(zipField,zipImportBtn);
        hBox2.setSpacing(10);
        Pane pane2 = initRowPane(zipLabel, hBox2);


        VBox vBox=new VBox(pane1,pane2);
        vBox.setPadding(new Insets(10,20,10,20));
        return vBox;

    }

    private Pane initBlackListTab(){
        Label workLabel=new Label("作品黑名单(空格分隔)");
        workLabel.getStyleClass().add("title-2");

     /*   ListView<String> listView=new ListView<>();
        listView.*/

        TextArea workArea=new TextArea();
        workArea.setPromptText("这里输入作品，以空格分隔");
        workArea.setWrapText(true);
        workArea.textProperty().bindBidirectional(viewModel.workBlackListProperty());

        Label tagLabel=new Label("标签黑名单(空格分隔)");
        tagLabel.getStyleClass().add("title-2");
        TextArea tagArea=new TextArea();
        tagArea.setWrapText(true);
        tagArea.setPromptText("这里输入标签，以空格分隔");
        tagArea.textProperty().bindBidirectional(viewModel.tagBlackListProperty());

        Label textLabel=new Label("文本黑名单(换行分隔)");
        textLabel.getStyleClass().add("title-2");
        TextArea textArea=new TextArea();
        textArea.setWrapText(true);
        textArea.setPromptText("这里输入文本，以换行分隔");
        textArea.textProperty().bindBidirectional(viewModel.textBlackListProperty());

        VBox vBox=new VBox(workLabel,workArea,tagLabel,tagArea,textLabel,textArea);
        vBox.setPadding(new Insets(10,20,10,20));
        return vBox;
    }



    private Pane initDownloadTab(){
        Label downloadLabel=new Label("下载目录");
        TextField  downloadField=new TextField();
        downloadField.setDisable(true);
        downloadField.textProperty().bindBidirectional(viewModel.downloadDirProperty());
        Button downloadBtn=new Button("选择");
        downloadBtn.setOnAction(event -> {
            DirectoryChooser directoryChooser=new DirectoryChooser();
            directoryChooser.setTitle("选择下载目录");
            File file = directoryChooser.showDialog(App.mainStage);
            if (file != null)
                downloadField.setText(file.getAbsolutePath());
        });
        HBox hBox=new HBox(downloadField,downloadBtn);
        hBox.setSpacing(10);
        Pane pane1 = initRowPane(downloadLabel, hBox);


        Popover hostPopover=new Popover(new Label("本地Aira2一般填写http://localhost:6800/jsonrpc，本地Motrix一般填写http://localhost:16800/jsonrpc"));
        Hyperlink hostLink=new Hyperlink("?");
        hostLink.setOnAction(event -> hostPopover.show(hostLink));
        Label hostLabel=new Label("Aria2地址",hostLink);
        hostLabel.setContentDisplay(ContentDisplay.RIGHT);
        TextField heightField=new TextField();
        heightField.textProperty().bindBidirectional(viewModel.aria2HostProperty());
        Pane pane2 = initRowPane(hostLabel, heightField);



        Label tokenLabel=new Label("Aria2 Token(RPC密钥)");
        TextField tokenField=new TextField();
        tokenField.textProperty().bindBidirectional(viewModel.ariaRPCKeyProperty());
        Pane pane3 = initRowPane(tokenLabel, tokenField);







        VBox vBox=new VBox(pane1,new Separator(Orientation.HORIZONTAL),pane2,pane3);
        vBox.setPadding(new Insets(10,20,10,20));
        return vBox;
    }
    private Pane initAboutTab(){

        Label label=new Label("未来道具11号(KK Maid) " + Config.version.get());
        label.getStyleClass().add("title-2");


        VBox vBox=new VBox(label);
        vBox.setPadding(new Insets(10,20,10,20));
        return vBox;
    }





    private Pane initRowPane(Node node1, Node node2){
        AnchorPane anchorPane=new AnchorPane(node1,node2);
        anchorPane.setPadding(new Insets(10,20,10,20));
        AnchorPaneUtil.setPosition(node1,10.0,null,null,0.0);
        AnchorPaneUtil.setPosition(node2,0.0,0.0,null,null);


        return anchorPane;
    }




    private boolean installVLC(){
        if (OSUtil.isWindows()){
            JExecResult result = null;
            try {
                result = JRegistry.query("HKEY_CLASSES_ROOT\\Applications\\",
                        new JQueryOptions().useF("\"vlc.exe\""));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            JRegistry.dump(result);
            logger.info("Windows系统，VLC播放器是否安装:{}", result.isSuccess());
            return result.isSuccess();
        }else {
            return false;
        }





    }

    public StackPane getRoot() {
        return root;
    }
}