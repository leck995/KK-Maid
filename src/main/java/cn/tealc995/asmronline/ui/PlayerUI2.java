package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.App;
import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.MainDialogEvent;
import cn.tealc995.asmronline.event.MainPaneEvent;
import cn.tealc995.asmronline.model.lrc.LrcBean;
import cn.tealc995.asmronline.player.LcMediaPlayer;
import cn.tealc995.asmronline.ui.component.LrcView;
import cn.tealc995.asmronline.util.AnchorPaneUtil;
import cn.tealc995.asmronline.util.CssLoader;
import cn.tealc995.asmronline.util.TimeFormatUtil;
import cn.tealc995.teaFX.controls.SceneBar;
import cn.tealc995.teaFX.enums.TitleBarStyle;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class PlayerUI2 {

    private StackPane root,rightPane,albumPane;

    private ImageView album;

    private Label currentTimeLabel,totalTimeLabel;

    private Pane bgPane;

    private AnchorPane infoPane,progressPane;

    private Label singer;

    private Label songName;

    private Button backBtn,nextBtn,preBtn,showSongListViewBtn;

    private ToggleButton disorderBtn,loopBtn,playBtn,lrcBtn,volumeBtn;

    private JFXSlider songSlider;

    private LrcView<LrcBean> lrcView;

    public Boolean isMouseMove = false;

    private ContextMenu soundPopup;

    private LcMediaPlayer player;


    public PlayerUI2(){
        player=LcMediaPlayer.getInstance();
        root=new StackPane();
        root.getStyleClass().add("background");
        root.getStylesheets().add(CssLoader.getCss(CssLoader.baseUI));
        root.getStylesheets().add(CssLoader.getCss(CssLoader.colors));
        root.getStylesheets().add(CssLoader.getCss(CssLoader.player));
        Pane bgPane1=new Pane();
        bgPane1.setBackground(new Background(new BackgroundFill(Color.web("#24282e"),null,null)));
        bgPane=new Pane();
        GaussianBlur gaussianBlur=new GaussianBlur(40);
        Lighting lighting=new Lighting();
        lighting.setDiffuseConstant(0.45);
        gaussianBlur.setInput(lighting);
        bgPane.setEffect(gaussianBlur);

        backBtn=new Button();
        backBtn.getStyleClass().addAll("lc-svg-btn","back-btn");
        backBtn.setOnAction(actionEvent -> {
            EventBusUtil.getDefault().post(new MainPaneEvent(null,false));
        });


        GridPane gridPane = initMain();
        AnchorPane mainPane=new AnchorPane(gridPane,backBtn);
        AnchorPaneUtil.setPosition(gridPane,0.0,0.0,0.0,0.0);
        AnchorPaneUtil.setPosition(backBtn,null,15.0,15.0,null);

        SceneBar sceneBar=new SceneBar(App.mainStage, TitleBarStyle.NO_LEFT,false);
        sceneBar.setContent(mainPane);
        root.getChildren().addAll(bgPane1,bgPane,sceneBar);


    }


    private GridPane initMain(){
        GridPane gridPane=new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        VBox leftPane=new VBox();
        rightPane=new StackPane();
        gridPane.add(leftPane, 0, 0); // column=1 row=0
        gridPane.add(rightPane, 1, 0);  // column=2 row=0


        /*封面*/
        album=new ImageView();
        album.setPreserveRatio(true);
        if (player.mainCover() != null){
            album.setImage(new Image(player.mainCover(), true));
        }
        albumPane=new StackPane(album);
        HBox albumPane2=new HBox(albumPane);
        albumPane2.setAlignment(Pos.CENTER);

        /*歌曲名称*/
        songName=new Label();
        songName.getStyleClass().add("title-label");
        songName.textProperty().bind(player.titleProperty());
        singer=new Label();
        singer.getStyleClass().add("artist-label");
        singer.textProperty().bind(player.artistProperty());
        lrcBtn=new ToggleButton();
        lrcBtn.getStyleClass().add("lrc-btn");
        lrcBtn.setGraphic(new Region());
        lrcBtn.setTooltip(new Tooltip("桌面歌词"));
        lrcBtn.selectedProperty().bindBidirectional(player.desktopLrcShowProperty());
        infoPane=new AnchorPane(songName,singer,lrcBtn);
        infoPane.prefWidthProperty().bind(albumPane.widthProperty());
        AnchorPaneUtil.setPosition(songName,0.0,40.0,null,0.0);
        AnchorPaneUtil.setPosition(singer,33.0,40.0,null,0.0);
        AnchorPaneUtil.setPosition(lrcBtn,15.0,40.0,null,null);
        HBox titlePane2=new HBox(infoPane);
        titlePane2.setAlignment(Pos.CENTER);

        /*进度条*/
        currentTimeLabel=new Label();
        currentTimeLabel.getStyleClass().add("time-label");

        totalTimeLabel=new Label();
        totalTimeLabel.getStyleClass().add("time-label");

        currentTimeLabel.textProperty().bind(Bindings
                .createStringBinding(() -> (
                        TimeFormatUtil.formatToClock(player.getCurrentTime())
                ) , player.currentTimeProperty()));
        totalTimeLabel.textProperty().bind(Bindings
                .createStringBinding(() -> (
                        TimeFormatUtil.formatToClock(player.totalTimeProperty().get())
                ) ,player.totalTimeProperty()));

        songSlider=new JFXSlider();
        songSlider.getStyleClass().add("detail-song-slider");
        songSlider.setMin(0);
        songSlider.setMaxWidth(900);
        songSlider.maxProperty().bind(player.totalTimeProperty());
        /*进度条随歌曲时间更改*/
        SimpleDoubleProperty currentTimeProperty= new SimpleDoubleProperty();
        currentTimeProperty.bind(player.currentTimeProperty());
        currentTimeProperty.addListener((obs,oldValue,newValue)->{
            if (!isMouseMove) {
                songSlider.setValue(newValue.doubleValue());
            }
        });
        //设置播放条显示格式
        songSlider.setValueFactory(slider -> Bindings
                .createStringBinding(() -> (
                        TimeFormatUtil.formatToClock(songSlider.getValue())
                ) , slider.valueProperty()));
        /*进度条按下监听*/
        songSlider.setOnMousePressed(mouseEvent -> isMouseMove = true);
        /*进度条释放监听，跳转到指定时间*/
        songSlider.setOnMouseReleased(mouseEvent -> {
            if (!player.ready())
                return;
            isMouseMove = false;
            player.seek(songSlider.getValue());
        });

        if (currentTimeProperty.getValue() != null)
            songSlider.setValue(currentTimeProperty.getValue());
        progressPane=new AnchorPane(currentTimeLabel,songSlider,totalTimeLabel);
        progressPane.prefWidthProperty().bind(albumPane.widthProperty());
        AnchorPaneUtil.setPosition(currentTimeLabel,5.0,null,null,0.0);
        AnchorPaneUtil.setPosition(songSlider,7.0,40.0,null,40.0);
        AnchorPaneUtil.setPosition(totalTimeLabel,5.0,0.0,null,null);
        HBox timePane2=new HBox(progressPane);
        timePane2.setAlignment(Pos.CENTER);



        /*按钮*/
        volumeBtn=new ToggleButton();
        volumeBtn.setGraphic(new Region());
        volumeBtn.getStyleClass().addAll("lc-svg-toggle-btn","volume-btn");
        volumeBtn.selectedProperty().bind(player.muteProperty());

        loopBtn=new ToggleButton();
        loopBtn.setGraphic(new Region());
        loopBtn.getStyleClass().add("loop-btn");
        loopBtn.setTooltip(new Tooltip("循环"));
        loopBtn.selectedProperty().bindBidirectional(player.loopProperty());



        preBtn=new Button();
        preBtn.setGraphic(new Region());
        preBtn.getStyleClass().addAll("lc-svg-btn","pre-btn");
        preBtn.setOnMouseClicked(mouseEvent -> player.pre());

        playBtn=new ToggleButton();
        playBtn.setGraphic(new Region());
        playBtn.getStyleClass().addAll("lc-svg-toggle-btn","play-btn");
        playBtn.selectedProperty().bindBidirectional(player.playingProperty());
        playBtn.setOnAction(actionEvent -> {
            if (!player.ready()){
                playBtn.setSelected(false);
                actionEvent.consume();
            }
        });

        nextBtn=new Button();
        nextBtn.setGraphic(new Region());
        nextBtn.getStyleClass().addAll("lc-svg-btn","next-btn");
        nextBtn.setOnMouseClicked(mouseEvent -> player.next());


        disorderBtn=new ToggleButton();
        disorderBtn.setGraphic(new Region());
        disorderBtn.getStyleClass().add("disorder-btn");
        disorderBtn.setTooltip(new Tooltip("乱序"));
        disorderBtn.selectedProperty().bindBidirectional(player.disorderProperty());


        showSongListViewBtn=new Button();
        showSongListViewBtn.setGraphic(new Region());
        showSongListViewBtn.getStyleClass().addAll("lc-svg-btn","list-btn");
        showSongListViewBtn.setTooltip(new Tooltip("播放列表"));
        showSongListViewBtn.setOnMouseClicked(mouseEvent -> {
            PlayingListUI playingListUI=new PlayingListUI();
            EventBusUtil.getDefault().post(new MainDialogEvent(playingListUI.getRoot()));
        });

        HBox functionBtn=new HBox(volumeBtn,loopBtn,preBtn,playBtn,nextBtn,disorderBtn,showSongListViewBtn);
        functionBtn.setAlignment(Pos.CENTER);

        leftPane.getChildren().addAll(albumPane2,titlePane2,timePane2,functionBtn);




        ObservableList<LrcBean> lrcBeanList=FXCollections.observableArrayList();
        lrcView=new LrcView(player.currentTimeProperty());
        lrcView.getStyleClass().add("lrc-listview");
        rightPane.getChildren().add(lrcView);
        lrcView.setItems(lrcBeanList);
        lrcView.itemsProperty().bind(player.lrcBeansProperty());

        return gridPane;


    }






    /**
     * @description: 创建封面的圆角和阴影
     * @name: creatClip
     * @author: Leck
     * @param:
     * @return  javafx.scene.shape.Rectangle
     * @date:   2023/3/2
     */
    private Rectangle creatClip() {
        Rectangle rectangle=new Rectangle();
        rectangle.widthProperty().bind(albumPane.widthProperty().add(-10));
        rectangle.heightProperty().bind(albumPane.heightProperty().add(-10));
        rectangle.arcWidthProperty().bind(Config.detailAlbumRadiusSize);
        rectangle.arcHeightProperty().bind(Config.detailAlbumRadiusSize);

        SimpleBooleanProperty detailAlbumEffectModel=new SimpleBooleanProperty();
        detailAlbumEffectModel.bind( Config.detailAlbumEffectModel);


        if (detailAlbumEffectModel.get()){
            DropShadow dropShadow = new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(25, 25, 26,0.9), 2, 1, 2, 2);
            dropShadow.radiusProperty().bind(Config.detailAlbumEffectSize);
            rectangle.setEffect(dropShadow);
        }


        detailAlbumEffectModel.addListener((o, a, b) -> {
            if (!b){
                DropShadow effect = (DropShadow) rectangle.getEffect();
                effect.radiusProperty().unbind();
                rectangle.setEffect(null);
            }
        });

        return rectangle;
    }






    /**
     * @name: initLrcView
     * @description: 初始化LrcView
     * @author: Leck
     * @param:
     * @return  void
     * @date:   2022/12/17
     */
    private void initLrcView(){
        ObservableList<LrcBean> lrcBeanList=FXCollections.observableArrayList();
        lrcView=new LrcView(player.currentTimeProperty());
        lrcView.getStyleClass().add("lrc-listview");
        //lrcPane.getChildren().add(lrcView);
        lrcView.setItems(lrcBeanList);
        //lrcView.maxWidthProperty().bind(lrcPane.widthProperty());

        //lrcView.currentTimeProperty().bind(player.currentTimeProperty());


    }


    public StackPane getRoot() {
        return root;
    }
}


