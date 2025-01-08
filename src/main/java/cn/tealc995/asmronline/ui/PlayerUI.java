package cn.tealc995.asmronline.ui;

import atlantafx.base.util.Animations;
import cn.tealc995.asmronline.App;
import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.MainDialogEvent;
import cn.tealc995.asmronline.event.MainPaneEvent;
import cn.tealc995.asmronline.model.lrc.LrcBean;
import cn.tealc995.asmronline.player.LcMediaPlayer;
import cn.tealc995.asmronline.player.MediaPlayerUtil;
import cn.tealc995.asmronline.player.TeaMediaPlayer;
import cn.tealc995.asmronline.ui.component.LrcView;
import cn.tealc995.asmronline.ui.item.*;
import cn.tealc995.asmronline.util.TimeFormatUtil;
import cn.tealc995.teaFX.controls.TitleBar;
import cn.tealc995.teaFX.enums.TitleBarStyle;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import com.jhlabs.image.ContrastFilter;
import com.jhlabs.image.GaussianFilter;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;


public class PlayerUI {
    @FXML
    private StackPane root,lrcPane,albumPane;
    @FXML
    private ImageView album;
    @FXML
    private Label currentTimeLabel,totalTimeLabel;
    @FXML
    private Pane bgPane;
    @FXML
    private AnchorPane infoPane,progressPane;
    @FXML
    private Label singer;
    @FXML
    private Label songName;
    @FXML
    private Button backBtn,nextBtn,preBtn,showSongListViewBtn;
    @FXML
    private ToggleButton disorderBtn,loopBtn,playBtn,lrcBtn,volumeBtn;
    @FXML
    private JFXSlider songSlider;
    @FXML
    private JFXNodesList nodesList;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private Button importLrcZipBtn,importLrcFolderBtn,manageLrcBtn;
    private LrcView<LrcBean> lrcView;

    public Boolean isMouseMove = false;

    private ContextMenu soundPopup;

    private TeaMediaPlayer player;
    private GaussianFilter gaussianFilter;
    private ContrastFilter contrastFilter;


    public PlayerUI(){
        player= MediaPlayerUtil.mediaPlayer();
    }


    @FXML
    private void  initialize(){
        GaussianFilter gaussianFilter=new GaussianFilter(Config.detailGaussianSize.get());
        ContrastFilter contrastFilter=new ContrastFilter();
        contrastFilter.setBrightness(Config.detailDarkerSize.floatValue());

        Rectangle rectangle=new Rectangle();
        rectangle.setFill(Color.RED);
        rectangle.widthProperty().bind(bgPane.widthProperty());
        rectangle.heightProperty().bind(bgPane.heightProperty());
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        root.setClip(rectangle);


        TitleBar sceneBar=new TitleBar(App.mainStage, TitleBarStyle.NO_LEFT);
        sceneBar.setContent(root.getChildren().get(2));
        root.getChildren().add(sceneBar);


        if (player.mainCover() != null){
            Image image = new Image(player.mainCover(), true);
            image.progressProperty().addListener((observableValue, number, t1) -> {
                if (t1.intValue() == 1){

                    try {
                        BufferedImage bufferedImage = Thumbnails.of(SwingFXUtils.fromFXImage(image, null)).width(100).asBufferedImage();

                        BufferedImage filter = gaussianFilter.filter(bufferedImage, null);

                        filter=contrastFilter.filter(filter,null);
                        BackgroundImage backgroundImage=new BackgroundImage(
                                SwingFXUtils.toFXImage(filter,null),
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,true,true,true,true));
                        Background background=new Background(backgroundImage);
                        bgPane.setBackground(background);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            });

            album.setImage(image);
        }





        SimpleBooleanProperty detailAlbumRadiusModel=new SimpleBooleanProperty();
        detailAlbumRadiusModel.bind(Config.detailAlbumRadiusModel);
        if (detailAlbumRadiusModel.get()){
            albumPane.setClip(creatClip());
        }
        detailAlbumRadiusModel.addListener((observableValue, aBoolean, t1) -> {
            if (t1){
                albumPane.setClip(creatClip());
            }else{
                if (albumPane.getClip()!=null){
                    Rectangle clip = (Rectangle) albumPane.getClip();
                    clip.widthProperty().unbind();
                    clip.heightProperty().unbind();
                    clip.arcHeightProperty().unbind();
                    clip.arcWidthProperty().unbind();
                    albumPane.setClip(null);

                }
            }
        });







        album.fitWidthProperty().bind(root.widthProperty().multiply(0.45));
        album.fitHeightProperty().bind(root.heightProperty().multiply(0.45));

        infoPane.prefWidthProperty().bind(albumPane.widthProperty());
        progressPane.prefWidthProperty().bind(albumPane.widthProperty());



/*        GaussianBlur gaussianBlur=new GaussianBlur();
        gaussianBlur.setRadius(60);
        Lighting lighting=new Lighting();
        lighting.setDiffuseConstant(0.45);
        gaussianBlur.setInput(lighting);
            bgPane.setEffect(gaussianBlur);*/


        

        disorderBtn.setTooltip(new Tooltip("乱序"));
        disorderBtn.selectedProperty().bindBidirectional(player.disorderProperty());
        loopBtn.setTooltip(new Tooltip("循环"));
        loopBtn.selectedProperty().bindBidirectional(player.loopProperty());
        showSongListViewBtn.setTooltip(new Tooltip("播放列表"));
        showSongListViewBtn.setOnMouseClicked(mouseEvent -> {
            PlayingListUI playingListUI=new PlayingListUI();
            EventBusUtil.getDefault().post(new MainDialogEvent(playingListUI.getRoot()));
        });
        playBtn.selectedProperty().bindBidirectional(player.playingProperty());
        playBtn.setOnAction(actionEvent -> {
            if (!player.ready()){
                playBtn.setSelected(false);
                actionEvent.consume();
            }
        });
        lrcBtn.setTooltip(new Tooltip("桌面歌词"));
        //lrcBtn.selectedProperty().bindBidirectional(DesktopLrcDialog.getInstance().isVisibleProperty());
        lrcBtn.selectedProperty().bindBidirectional(player.desktopLrcShowProperty());

        nextBtn.setOnMouseClicked(mouseEvent -> player.next());
        preBtn.setOnMouseClicked(mouseEvent -> player.pre());

        volumeBtn.selectedProperty().bind(player.muteProperty());
        volumeBtn.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (soundPopup!=null){
                    Bounds bounds = volumeBtn.localToScreen(volumeBtn.getBoundsInLocal());
                    soundPopup.show(root.getScene().getWindow(), bounds.getMinX() -10, bounds.getMinY() - 160);
                    event.consume();
                    return;
                }
                Bounds bounds = volumeBtn.localToScreen(volumeBtn.getBoundsInLocal());
                soundPopup=new ContextMenu();
                soundPopup.setPrefWidth(100);
                soundPopup.setMaxWidth(100);
                soundPopup.getScene().setRoot(new VolumeUI().getRoot());
                soundPopup.show(root.getScene().getWindow(), bounds.getMinX() -10, bounds.getMinY() - 160);
                event.consume();
            }
        });

        songSlider.setMin(0);
        //songSlider.maxWidthProperty().bind(album.fitHeightProperty());
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

        songName.textProperty().bind(player.titleProperty());




        singer.textProperty().bind(player.artistProperty());
        currentTimeLabel.textProperty().bind(Bindings
                .createStringBinding(() -> (
                        TimeFormatUtil.formatToClock(player.getCurrentTime())
                ) , player.currentTimeProperty()));
        totalTimeLabel.textProperty().bind(Bindings
                .createStringBinding(() -> (
                        TimeFormatUtil.formatToClock(player.totalTimeProperty().get())
                ) ,player.totalTimeProperty()));
        initLrcView();
    
        lrcView.itemsProperty().bind(player.lrcBeansProperty());





     
        backBtn.setOnAction(actionEvent -> {
            currentTimeProperty.unbind();

            Timeline timeline=new Timeline(new KeyFrame(Duration.millis(240),new KeyValue(root.translateYProperty(),App.mainStage.getHeight())));
            timeline.setOnFinished(event -> {
                EventBusUtil.getDefault().post(new MainPaneEvent(null,false));
            });
            timeline.play();

        });



        nodesList.setRotate(180);
        nodesList.setSpacing(10);
        HamburgerBasicCloseTransition burgerTask1 = new HamburgerBasicCloseTransition(hamburger);
        burgerTask1.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            burgerTask1.setRate(burgerTask1.getRate() * -1);
            burgerTask1.play();
        });

        importLrcZipBtn.setOnAction(event -> {
            FileChooser fileChooser=new FileChooser();
            fileChooser.setTitle("选择字幕包");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("压缩包","*.zip","*.ZIP"));
            File file = fileChooser.showOpenDialog(App.mainStage);
            if (file != null){
                LrcZipDialogUI lrcZipDialogUI=new LrcZipDialogUI(file);
                EventBusUtil.getDefault().post(new MainDialogEvent(lrcZipDialogUI.getRoot()));
            }
        });
        importLrcFolderBtn.setOnAction(event -> {
            DirectoryChooser directoryChooser=new DirectoryChooser();
            directoryChooser.setTitle("选择字幕文件夹");
            File file = directoryChooser.showDialog(App.mainStage);
            if (file!=null){
                LrcFolderDialogUI lrcFolderDialogUI=new LrcFolderDialogUI(file);
                EventBusUtil.getDefault().post(new MainDialogEvent(lrcFolderDialogUI.getRoot()));
            }

        });
        manageLrcBtn.setOnAction(event -> {
            LrcFileDialogUi lrcFileDialogUi=new LrcFileDialogUi();
            EventBusUtil.getDefault().post(new MainDialogEvent(lrcFileDialogUi.getRoot()));
        });

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
        lrcPane.getChildren().add(lrcView);

        //lrcView.maxWidthProperty().bind(lrcPane.widthProperty());
        lrcView.setItems(lrcBeanList);
        //lrcView.currentTimeProperty().bind(player.currentTimeProperty());


    }





}


