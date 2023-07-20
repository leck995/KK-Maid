package cn.tealc995.asmronline.ui;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.controls.ProgressSliderSkin;
import atlantafx.base.controls.ToggleSwitch;
import cn.tealc995.asmronline.App;
import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.api.model.SortType;
import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.MainNotificationEvent;
import cn.tealc995.asmronline.player.LcMediaPlayer;
import cn.tealc995.asmronline.util.AnchorPaneUtil;
import cn.tealc995.asmronline.util.CssLoader;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.util.StringConverter;

import java.io.File;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-15 03:46
 */
public class SettingUI {
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

        Tab hostTab=new Tab("服务器");
        hostTab.setContent(initHostTab());
        hostTab.setClosable(false);



        Tab desktopTab=new Tab("桌面歌词");
        desktopTab.setContent(initDesktopTab());
        desktopTab.setClosable(false);

        Tab lrcTab=new Tab("字幕管理");
        lrcTab.setContent(initLrcTab());
        lrcTab.setClosable(false);

        Tab aboutTab=new Tab("关于");
        aboutTab.setContent(initAboutTab());
        aboutTab.setClosable(false);
        tabPane.getTabs().addAll(baseTab,mainTab,hostTab,desktopTab,lrcTab,aboutTab);


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

        Label versionLabel=new Label("检查更新");
        ToggleSwitch versionSwitch=new ToggleSwitch();
        versionSwitch.setDisable(true);
        versionSwitch.selectedProperty().bindBidirectional(viewModel.autoCheckVersionProperty());
        Pane pane3 = initRowPane(versionLabel, versionSwitch);
        VBox vBox=new VBox(pane1,pane2,new Separator(Orientation.HORIZONTAL),pane3);
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




    private Pane initHostTab(){
        Label hostLabel=new Label("服务器API地址");
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
                String login = viewModel.login(nameField.getText(), passwordField.getText());
                if (login != null){
                    EventBusUtil.getDefault().post(new MainNotificationEvent("获取Token成功"));
                    dialog.close();
                }else {
                    EventBusUtil.getDefault().post(new MainNotificationEvent("登陆失败,检查网络，用户名和密码"));
                }

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
        showLrcSwitch.selectedProperty().bindBidirectional(LcMediaPlayer.getInstance().desktopLrcShowProperty());



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
    private Pane initAboutTab(){

        Label label=new Label("未来道具11号(KK Maid) 测试版");
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


    public StackPane getRoot() {
        return root;
    }
}