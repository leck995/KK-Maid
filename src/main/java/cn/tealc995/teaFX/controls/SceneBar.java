package cn.tealc995.teaFX.controls;

import cn.tealc995.teaFX.config.Config;
import cn.tealc995.teaFX.enums.TitleBarStyle;
import cn.tealc995.teaFX.handler.DragWindowHandler;
import cn.tealc995.teaFX.handler.ResizeWindowHandler;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @program: AzurLaneWikiGetTool
 * @description:
 * @author: Leck
 * @create: 2023-06-03 09:07
 */
public class SceneBar extends StackPane {
    private Stage stage;
    private StackPane icon;
    private HBox leftHBox,rightHBox;
    private Label title;
    private Button closeBtn,minBtn;
    private ToggleButton maxBtn,fullScreenBtn;
    private StackPane resizePane;
    private Region resizeConner;
    private ResizeWindowHandler resizeWindowHandler;
    private AnchorPane titleBarPane;
    private AnchorPane contentPane;
    private BoundingBox maximizedBox;

    private SimpleBooleanProperty hasInsert = new SimpleBooleanProperty(false);

    private double insertSize=5.0;
    private Color bgColor;

    private boolean main;
    public SceneBar(Stage stage, TitleBarStyle style,boolean main) {
        this.stage=stage;
        this.main=main;

        AnchorPane mainPane=new AnchorPane();

        resizeConner = new Region();
        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M542.72 884.053333l341.333333-341.333333a32 32 0 0 1 47.445334 42.816l-2.197334 2.432-341.333333 341.333333a32 32 0 0 1-47.466667-42.837333l2.197334-2.432 341.333333-341.333333-341.333333 341.333333z m-437.333333-10.666666l778.666666-778.666667a32 32 0 0 1 47.445334 42.816l-2.197334 2.432-778.666666 778.666667a32 32 0 0 1-47.466667-42.837334l2.197333-2.432 778.666667-778.666666-778.666667 778.666666z");
        resizeConner.setShape(svgPath);
        resizeConner.setBackground(new Background(new BackgroundFill(Color.GREY, null,null)));
        resizeConner.setMaxSize(16.0,16.00);
        resizeConner.setPrefSize(16.0,16.0);
        resizePane=new StackPane(resizeConner);
        ResizeWindowHandler resizeWindowHandler = new ResizeWindowHandler(stage);
        resizePane.setOnMousePressed(resizeWindowHandler);
        resizePane.setOnMouseDragged(resizeWindowHandler);
        resizePane.setOnMouseEntered(resizeWindowHandler);
        resizePane.setOnMouseExited(resizeWindowHandler);


        titleBarPane=new AnchorPane();
        titleBarPane.setPrefHeight(60.0);
        titleBarPane.getStyleClass().add("scene-bar-title");
        DragWindowHandler dragWindowHandler=new DragWindowHandler(stage);
        titleBarPane.setOnMousePressed(dragWindowHandler);/* 鼠标按下 */
        titleBarPane.setOnMouseDragged(dragWindowHandler);/* 鼠标拖动 */
        titleBarPane.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount()==2){
                if (maxBtn != null){
                    maxBtn.setSelected(!maxBtn.isSelected());
                }

            }
        });
        if (TitleBarStyle.ALL==style || TitleBarStyle.NO_RIGHT==style){
            /*标题栏左边*/
            icon=new StackPane();
            title=new Label();
            leftHBox=new HBox(icon,title);
            leftHBox.setAlignment(Pos.CENTER_LEFT);
            leftHBox.setSpacing(10);
            leftHBox.setPadding(new Insets(0,0.0,0,15.0));
            leftHBox.getStyleClass().add("scene-bar-title-left");
            title.getStyleClass().add("title");
            //icon.getStyleClass().add("icon");
            titleBarPane.getChildren().add(leftHBox);

            setPosition(leftHBox,0.0,null,0.0,0.0);
        }

        if (TitleBarStyle.ALL==style || TitleBarStyle.NO_LEFT==style) {
            /*标题栏右边边*/
            closeBtn = new Button();
            closeBtn.setGraphic(new Region());
            maxBtn = new ToggleButton();
            maxBtn.setGraphic(new Region());
            fullScreenBtn=new ToggleButton();
            fullScreenBtn.setGraphic(new Region());
            minBtn = new Button();
            minBtn.setGraphic(new Region());
            maxBtn.selectedProperty().bindBidirectional(Config.SCREEN_MAXED);
            maxBtn.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                setMaxed(t1);
            });


            minBtn.setOnMouseClicked(mouseEvent -> {
                Node node=(Node) mouseEvent.getSource();
                Stage stage1 =(Stage)node.getScene().getWindow();
                stage1.setIconified(true);
            });
            closeBtn.setOnAction(event -> Platform.exit());
            rightHBox=new HBox(fullScreenBtn,minBtn,maxBtn,closeBtn);
            fullScreenBtn.selectedProperty().bindBidirectional(Config.SCREEN_FULL);
            fullScreenBtn.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                setFullScreen(t1);
            });
            rightHBox.setSpacing(5.0);
            rightHBox.setPadding(new Insets(0,10,0,0));
            rightHBox.setAlignment(Pos.CENTER_RIGHT);
            rightHBox.getStyleClass().add("scene-bar-title-right");
            maxBtn.getStyleClass().add("max-btn");
            minBtn.getStyleClass().add("min-btn");
            closeBtn.getStyleClass().add("close-btn");
            fullScreenBtn.getStyleClass().add("full-btn");
            titleBarPane.getChildren().add(rightHBox);
            setPosition(rightHBox,0.0,0.0,0.0,null);
        }

        contentPane=new AnchorPane();
        contentPane.getStyleClass().add("scene-bar-content");
        mainPane.getChildren().addAll(titleBarPane,contentPane,resizePane);
        setPosition(titleBarPane,0.0,0.0,null,0.0);
        setPosition(resizePane,null,5.0,5.0,null);
        setPosition(contentPane,titleBarPane.getPrefHeight(),0.0,0.0,0.0);

        getStyleClass().add("scene-bar");
        //getStylesheets().add(this.getClass().getResource("/cn/tealc995/teaFX/css/scene-bar.css").toExternalForm());
        getChildren().add(mainPane);


        if (Config.SCREEN_FULL.get() || Config.SCREEN_MAXED.get()){
          /*  setPosition(titleBarPane,0.0,0.0,null,0.0);
            setPosition(resizePane,null,0.0,0.0,null);
            setPosition(contentPane,titleBarPane.getPrefHeight(),0.0,0.0,0.0);*/
            setPadding(new Insets(0));
            resizePane.setVisible(false);
        }else {
            setPadding(new Insets(10));
        }

    }

    public void setMaxed(boolean maxed){

        if (maxed){
            if (main) {
                //Config.SCREEN_MAXED.set(true);
                Config.ORIGINAL_SIZE= new BoundingBox(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
                Screen screen = Screen.getScreensForRectangle(stage.getX(),stage.getY(),stage.getWidth(), stage.getHeight()).get(0);
                Rectangle2D bounds = screen.getVisualBounds();
                maximizedBox = new BoundingBox(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
                stage.setX(maximizedBox.getMinX());
                stage.setY(maximizedBox.getMinY());
                stage.setWidth(maximizedBox.getWidth());
                stage.setHeight(maximizedBox.getHeight());
            }


            bgColor= (Color) getBackground().getFills().get(0).getFill();
            setBackground(new Background(new BackgroundFill(bgColor,new CornerRadii(0),new Insets(0))));
    /*        setPosition(titleBarPane,0.0,0.0,null,0.0);
            setPosition(resizePane,null,0.0,0.0,null);
            setPosition(contentPane,titleBarPane.getPrefHeight(),0.0,0.0,0.0);*/
            setPadding(new Insets(0));
            resizePane.setVisible(false);
        }else {
            if (main){
                stage.setX(Config.ORIGINAL_SIZE.getMinX());
                stage.setY(Config.ORIGINAL_SIZE.getMinY());
                stage.setWidth(Config.ORIGINAL_SIZE.getWidth());
                stage.setHeight(Config.ORIGINAL_SIZE.getHeight());
            }

            //Config.ORIGINAL_SIZE=null;
            //Config.SCREEN_MAXED.set(false);
            setBackground(new Background(new BackgroundFill(bgColor,new CornerRadii(10),new Insets(5))));
        /*    setPosition(titleBarPane,insertSize,insertSize,null,insertSize);
            setPosition(resizePane,null,10.0,10.0,null);
            setPosition(contentPane,titleBarPane.getPrefHeight()+insertSize,8.0,8.0,5.0);*/
            setPadding(new Insets(10));
            resizePane.setVisible(true);
        }
    }

    public void setFullScreen(boolean full){
        if (!main) return;
        if (full){
            if (main){
                stage.setFullScreen(true);
            }

            bgColor= (Color) getBackground().getFills().get(0).getFill();
            setBackground(new Background(new BackgroundFill(bgColor,new CornerRadii(0),new Insets(0))));
       /*     setPosition(titleBarPane,0.0,0.0,null,0.0);
            setPosition(resizePane,null,0.0,0.0,null);
            setPosition(contentPane,titleBarPane.getPrefHeight(),0.0,0.0,0.0);*/
            setPadding(new Insets(0));
            resizePane.setVisible(false);
        }else {
            if (main){
                stage.setFullScreen(false);
            }
            setBackground(new Background(new BackgroundFill(bgColor,new CornerRadii(10),new Insets(5))));
   /*         setPosition(titleBarPane,insertSize,insertSize,null,insertSize);
            setPosition(resizePane,null,10.0,10.0,null);
            setPosition(contentPane,titleBarPane.getPrefHeight()+insertSize,8.0,8.0,5.0);*/
            setPadding(new Insets(10));
            resizePane.setVisible(true);
        }
    }


    /**
     * @name: setPosition
     * @description: 简化AnchorPane的位置设置
     * @author: Leck
     * @param:	pane
     * @param:	top
     * @param:	right
     * @param:	bottom
     * @param:	left
     * @return  void
     * @date:   2023/1/2
     */
    private void setPosition(Node pane, Double top, Double right, Double bottom, Double left){
        AnchorPane.setTopAnchor(pane,top);
        AnchorPane.setRightAnchor(pane,right);
        AnchorPane.setBottomAnchor(pane,bottom);
        AnchorPane.setLeftAnchor(pane,left);
    }

    public void setContent(Node node){
        contentPane.getChildren().add(node);
        setPosition(node,0.0,0.0,0.0,0.0);
    }


    public HBox getTitleBarLeftPane(){
        return leftHBox;
    }

    public HBox getTitleBarRightPane(){
        return rightHBox;
    }

    public void setIcon(Node node){
        icon.getChildren().add(node);
    }

    public void setTitle(String title){
        this.title.setText(title);
    }

    public void hideResizeConner(Boolean b){
        resizeConner.setVisible(!b);
    }


    public void setRoot(Node node){
        getChildren().add(node);
        setPosition(node,10.0,10.0,10.0,10.0);
    }


    public Button close(){
        return closeBtn;
    }

    public void setCloseEvent(EventHandler<ActionEvent> eventHandler){
        closeBtn.setOnAction(eventHandler);
    }


    public Label title(){
        return title;
    }

}