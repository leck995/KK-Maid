package cn.tealc995.kkmaid.ui;

import atlantafx.base.theme.Styles;
import cn.tealc995.kkmaid.App;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainDialogEvent;
import cn.tealc995.kkmaid.event.MainPaneEvent;
import cn.tealc995.kkmaid.player.MediaPlayerUtil;
import cn.tealc995.kkmaid.player.TeaMediaPlayer;
import cn.tealc995.kkmaid.ui.item.VolumeUI;
import cn.tealc995.kkmaid.util.CssLoader;
import cn.tealc995.kkmaid.util.FXResourcesLoader;
import cn.tealc995.kkmaid.util.TimeFormatUtil;
import com.jfoenix.controls.JFXSlider;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-14 00:48
 */
public class SimplePlayerUI {
    private StackPane root;
    private ContextMenu volumePopup;
    public Boolean isMouseMove = false;
    public SimplePlayerUI() {
        root=new StackPane();
        TeaMediaPlayer player=MediaPlayerUtil.mediaPlayer();

        ImageView album=new ImageView();
        album.imageProperty().bind(player.albumProperty());
        album.setFitHeight(40);
        album.setFitWidth(40);
        album.setSmooth(true);
        Circle circle=new Circle(20,20,20);
        album.setClip(circle);

        StackPane albumPane=new StackPane(album);

        albumPane.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fl=new FXMLLoader(FXResourcesLoader.loadURL("/cn/tealc995/kkmaid/fxml/player.fxml"));
            try {
                Pane parent = fl.load();
                parent.setTranslateY(App.mainStage.getHeight());
                EventBusUtil.getDefault().post(new MainPaneEvent(parent,true));

                Timeline timeline=new Timeline(new KeyFrame(Duration.millis(240),new KeyValue(parent.translateYProperty(),0)));
                timeline.play();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
/*
            PlayerUI2 playerUI2=new PlayerUI2();
            EventBusUtil.getDefault().post(new MainPaneEvent(playerUI2.getRoot(),true));*/
        });



        RotateTransition transition=new RotateTransition(Duration.seconds(5),album);
        transition.setCycleCount(RotateTransition.INDEFINITE);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setByAngle(360);
        transition.setAutoReverse(false);




        JFXSlider timeSlider=new JFXSlider();
        timeSlider.getStyleClass().add("simple-song-slider");
        timeSlider.setMin(0);
        timeSlider.setValue(0);

        timeSlider.maxProperty().bind(player.totalTimeProperty());



        /*进度条随歌曲时间更改*/
        player.currentTimeProperty().addListener((obs,oldValue,newValue)->{
            if (!isMouseMove) {
                timeSlider.setValue(newValue.doubleValue());
            }
        });

        //设置播放条显示格式
        timeSlider.setValueFactory(slider -> Bindings
                .createStringBinding(() -> (
                        TimeFormatUtil.formatToClock(slider.getValue())
                ) , slider.valueProperty()));
        /*进度条按下监听*/
        timeSlider.setOnMousePressed(mouseEvent -> isMouseMove = true);
        /*进度条释放监听，跳转到指定时间*/
        timeSlider.setOnMouseReleased(mouseEvent -> {
            if (!player.ready())
                return;
            isMouseMove = false;
            double currentTime=timeSlider.getValue();
            player.seek(currentTime );
        });


/*        ProgressBar bufferedTimeSlider=new ProgressBar();
*//*        bufferedTimeSlider.getStyleClass().add("simple-song-buffer-slider");
        bufferedTimeSlider.setMin(0);
        bufferedTimeSlider.setValue(0);
        bufferedTimeSlider.setMax(100);
        bufferedTimeSlider.valueProperty().bind(player.bufferedTimeProperty());*//*

        bufferedTimeSlider.progressProperty().bind(player.bufferedTimeProperty());
        bufferedTimeSlider.getStyleClass().add("small");
        bufferedTimeSlider.prefWidthProperty().bind(timeSlider.widthProperty());*/
        StackPane timeSliderPane=new StackPane(timeSlider);



        Label currentTime=new Label();
        currentTime.getStyleClass().add("simple-time-label");
        Label totalTime=new Label();
        totalTime.getStyleClass().add("simple-time-label");
        currentTime.textProperty().bind(Bindings
                .createStringBinding(() -> (
                        TimeFormatUtil.formatToClock(player.getCurrentTime())
                ) , player.currentTimeProperty()));

        totalTime.textProperty().bind(Bindings
                .createStringBinding(() -> (
                        TimeFormatUtil.formatToClock(player.getTotalTime())
                ) ,player.totalTimeProperty()));

        BorderPane timeLabelPane=new BorderPane();
        timeLabelPane.setLeft(currentTime);
        timeLabelPane.setRight(totalTime);


        ToggleButton playBtn=new ToggleButton();
        playBtn.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1){
                transition.play();
            }else {
                transition.pause();
            }
        });
        playBtn.setGraphic(new Region());
        playBtn.getStyleClass().addAll("simple-btn","simple-play-btn");
        Button nextBtn=new Button();
        nextBtn.setGraphic(new Region());
        nextBtn.getStyleClass().addAll("simple-btn","simple-next-btn");
        Button preBtn=new Button();
        preBtn.setGraphic(new Region());
        preBtn.getStyleClass().addAll("simple-btn","simple-pre-btn");

        playBtn.selectedProperty().bindBidirectional(player.playingProperty());
        playBtn.setOnAction(actionEvent -> {
            if (!player.ready()){
                playBtn.setSelected(false);
                actionEvent.consume();
            }
        });
        nextBtn.setOnMouseClicked(mouseEvent -> player.next());
        preBtn.setOnMouseClicked(mouseEvent -> player.pre());


        HBox hBox=new HBox(preBtn,playBtn,nextBtn);
        hBox.setSpacing(5.0);
        hBox.setAlignment(Pos.CENTER);
/*
        VBox vBox=new VBox(timeSlider,timeLabelPane,hBox);
        vBox.setAlignment(Pos.CENTER);*/


        ToggleButton lrcBtn=new ToggleButton();
        lrcBtn.getStyleClass().addAll("simple-btn","simple-lrc-btn");
        lrcBtn.setGraphic(new Region());
        lrcBtn.selectedProperty().bindBidirectional(player.desktopLrcShowProperty());

        Button listBtn=new Button();
        listBtn.getStyleClass().addAll("simple-btn","simple-list-btn");
        listBtn.setGraphic(new Region());
        listBtn.setOnAction(event -> {
            PlayingListUI playingListUI=new PlayingListUI();
            EventBusUtil.getDefault().post(new MainDialogEvent(playingListUI.getRoot()));
        });


        ToggleButton volumeBtn=new ToggleButton();
        volumeBtn.getStyleClass().addAll("simple-btn", Styles.BUTTON_CIRCLE,"simple-volume-btn");
        volumeBtn.setGraphic(new Region());

        volumeBtn.selectedProperty().bind(player.muteProperty());

        volumeBtn.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (volumePopup!=null){
                    Bounds bounds = volumeBtn.localToScreen(volumeBtn.getBoundsInLocal());
                    volumePopup.show(root.getScene().getWindow(), bounds.getMinX()-8, bounds.getMinY() - 160);
                    event.consume();
                    return;
                }
                Bounds bounds = volumeBtn.localToScreen(volumeBtn.getBoundsInLocal());
                volumePopup = new ContextMenu();
                volumePopup.setPrefWidth(100);
                volumePopup.setMaxWidth(100);
                volumePopup.getScene().setRoot(new VolumeUI().getRoot());
                volumePopup.show(root.getScene().getWindow(), bounds.getMinX()-8, bounds.getMinY() - 160);
                event.consume();
            }
        });



        HBox headItem1=new HBox(volumeBtn,listBtn);
        headItem1.setSpacing(5);
        BorderPane headPane=new BorderPane();
        headPane.setLeft(lrcBtn);
        headPane.setRight(headItem1);


        BorderPane timePane=new BorderPane();
        timePane.setLeft(currentTime);
        timePane.setRight(timeLabelPane);
        VBox vBox=new VBox(headPane,timeSliderPane,timePane);
        vBox.setPadding(new Insets(0,0,20,0));

        BorderPane borderPane=new BorderPane();
        borderPane.setTop(vBox);
        borderPane.setLeft(albumPane);
        borderPane.setCenter(hBox);

        root.getChildren().add(borderPane);
        root.getStylesheets().add(CssLoader.getCss(CssLoader.simple_player));
        root.setPadding(new Insets(0,5,10,5));
    }

    public StackPane getRoot() {
        return root;
    }

}