package cn.tealc995.kkmaid.ui.component;


import cn.tealc995.kkmaid.Config;
import cn.tealc995.kkmaid.player.MediaPlayerUtil;
import cn.tealc995.kkmaid.util.CssLoader;
import cn.tealc995.teaFX.handler.DragWindowHandler;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @program: AsmrPlayer
 * @description: 桌面歌词类，注意该类依赖LcMediaPlayer，无法单独使用,该类仅在LcMediaPlayer中使用
 * @author: Leck
 * @create: 2023-02-28 23:56
 */
public class DesktopLrcDialog extends Stage {
    private static DesktopLrcDialog desktopLrcDialog;
    private SimpleBooleanProperty isVisible;
    private Text text;

    private Timer timer;

    private SimpleBooleanProperty hasEffect=new SimpleBooleanProperty();
    private SimpleBooleanProperty hasStroke=new SimpleBooleanProperty();
    private SimpleBooleanProperty hasBold=new SimpleBooleanProperty();
    public static DesktopLrcDialog getInstance(){
        if (desktopLrcDialog==null){
            desktopLrcDialog=new DesktopLrcDialog();
        }
        return desktopLrcDialog;
    }

    private DesktopLrcDialog() {
        isVisible=new SimpleBooleanProperty(false);
        //多创建一个stage，实现曲线救国
        //StageStyle.UTILITY创建的窗口在任务栏是没有图标的，但有默认的标题栏，过通过多创建一下stage，让歌词stage在依附于其，这样就不会在任务栏出现窗口了
        //前人的智慧，我tm怎么也想不出来啊
        Stage stage = new Stage();
        stage.setWidth(1.0);
        stage.setHeight(1.0);
        stage.initStyle(StageStyle.UTILITY);
        stage.setOpacity(0.0);
        stage.show();

        //设置拥有者
        initOwner(stage);
        initStyle(StageStyle.TRANSPARENT);

        VBox textPane=new VBox();
        textPane.setAlignment(Pos.CENTER);

        textPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        textPane.getStyleClass().add("dashboard-lrc-dialog");
        text=new Text();
        text.setMouseTransparent(true);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setWrappingWidth(1800);
        text.setFontSmoothingType(FontSmoothingType.GRAY);
        text.textProperty().bind(MediaPlayerUtil.mediaPlayer().lrcSelectedTextProperty());

        hasBold.bind(Config.desktopLRCBoldModel);
        hasBold.addListener((observableValue, aBoolean, t1) -> {
            if (t1){
                text.fontProperty().bind(Bindings.createObjectBinding(
                        ()->
                                (Font.font(Font.getDefault().getFamily(),FontWeight.BOLD,Config.desktopLRCFontSize.get())),
                        Config.desktopLRCFontSize)
                );
            }else{
                text.fontProperty().bind(Bindings.createObjectBinding(
                        ()->
                                (Font.font(Config.desktopLRCFontSize.get())),
                        Config.desktopLRCFontSize)
                );
            }
        });



        if (hasBold.get()){
            text.fontProperty().bind(Bindings.createObjectBinding(
                    ()->
                            (Font.font(Font.getDefault().getFamily(),FontWeight.BOLD,Config.desktopLRCFontSize.get())),
                    Config.desktopLRCFontSize)
            );
        }else{
            text.fontProperty().bind(Bindings.createObjectBinding(
                    ()->
                            (Font.font(Config.desktopLRCFontSize.get())),
                    Config.desktopLRCFontSize)
            );
        }

        text.fillProperty().bind(Bindings.createObjectBinding(
                () ->
                        (Color.web(Config.desktopLRCFontColor.get())),
                Config.desktopLRCFontColor)
        );
        //Font font=Font.font()
        //text.setFont(Font.font(Font.getDefault().getName(),FontWeight.BOLD,Config.desktopLRCFontSize.get()));
        //text.setStyle("-fx-font-weight: BOLD");


        hasEffect.bind(Config.desktopLRCBorderModel);
        hasEffect.addListener((observableValue, aBoolean, t1) -> {
            if (!t1){
                text.effectProperty().unbind();
                text.setEffect(null);
            }
            else
                text.effectProperty().bind(
                        Bindings.createObjectBinding(
                                ()->
                                        (new DropShadow(BlurType.THREE_PASS_BOX,Color.web(Config.desktopLRCBorderColor.get()),10.0,0,0,0)),
                                Config.desktopLRCBorderColor));
        });
        if (hasEffect.get()){
            text.effectProperty().bind(
                    Bindings.createObjectBinding(
                            ()->
                                    (new DropShadow(BlurType.THREE_PASS_BOX,Color.web(Config.desktopLRCBorderColor.get()),10.0,0,0,0)),
                            Config.desktopLRCBorderColor));
        }



        hasStroke.bind(Config.desktopLRCStrokeModel);
        hasStroke.addListener((observableValue, aBoolean, t1) -> {
            if (!t1){
                text.strokeProperty().unbind();
                text.setStroke(null);
            }
            else
                text.strokeProperty().bind(Bindings.createObjectBinding(
                        () ->
                                (Color.web(Config.desktopLRCStrokeColor.get())),
                        Config.desktopLRCStrokeColor));
        });
        if (hasStroke.get()){
            text.strokeProperty().bind(Bindings.createObjectBinding(
                    () ->
                            (Color.web(Config.desktopLRCStrokeColor.get())),
                    Config.desktopLRCStrokeColor));
        }







        //text.strokeProperty().set(Color.BLACK);

        ToggleButton lockBtn=new ToggleButton();
        lockBtn.setGraphic(new Region());
        lockBtn.getStyleClass().add("lrc-lock-btn");



        textPane.getChildren().add(text);
        DragWindowHandler dragWindowHandler=new DragWindowHandler(this);
        textPane.setOnMousePressed(dragWindowHandler);
        textPane.setOnMouseDragged(dragWindowHandler);

        
        VBox parentPane=new VBox(textPane,lockBtn);
        parentPane.setAlignment(Pos.CENTER);
        HBox root=new HBox(parentPane);
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        //root.setPrefSize(1800,200);
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        double width = screenRectangle.getWidth();

        if (width > 1800){
            root.setPrefWidth(1800);
        }else {
            root.setPrefWidth(width-100);
        }


        root.setPrefHeight(200);

        lockBtn.setVisible(false);
        lockBtn.setOnAction(actionEvent -> {
            if (lockBtn.isSelected()){
                textPane.setMouseTransparent(true);
            }else {
                textPane.setMouseTransparent(false);
            }
        });


        parentPane.setOnMouseEntered(mouseEvent -> {
            if (timer != null){
                timer.cancel();
                timer=null;
            }
            timer=new Timer(true);;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() ->lockBtn.setVisible(true));
                }
            }, 1000);

        });
        parentPane.setOnMouseExited(mouseEvent -> {
            if (timer != null){
                timer.cancel();
                timer=null;
            }
            timer=new Timer(true);;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() ->lockBtn.setVisible(false));
                }
            }, 1000);
        });





        Scene scene=new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        scene.getStylesheets().add(CssLoader.getCss(CssLoader.lrc_stage));
        setScene(scene);
        setAlwaysOnTop(true);



        isVisible.addListener((observableValue, aBoolean, t1) -> {
            if (t1){
                //Notification.show("鼠标悬停歌词弹窗1s可进行锁定与解锁",MessageType.WARNING,3000, Pos.TOP_CENTER,MainApplication.window);
                this.show();
            }else{
                this.close();
            }
        });
    }




    public boolean isVisible() {
        return isVisible.get();
    }

    public SimpleBooleanProperty isVisibleProperty() {
        return isVisible;
    }
    /**
     * @description: 设置歌词窗口显示与隐藏
     * @name: setVisible
     * @author: Leck
     * @param:	visible
     * @return  void
     * @date:   2023/3/1
     */

    public void setVisible(boolean isVisible) {
        this.isVisible.set(isVisible);
    }
}