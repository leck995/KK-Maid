package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.App;
import cn.tealc995.asmronline.api.model.Track;
import cn.tealc995.asmronline.util.AnchorPaneUtil;
import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-06 00:32
 */
public class PosterUI {
    private final ImageView imageView;
    private StackPane root;
    private PosterViewModel viewModel;

    private Point2D dragDistance;


    public PosterUI(List<Track> filePaths, int index) {
        viewModel=new PosterViewModel(filePaths,index);
        root=new StackPane();


        imageView = new ImageView();
        imageView.setManaged(false);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);


        imageView.imageProperty().bind(viewModel.imageProperty());


        StackPane centerPane=new StackPane(imageView);
        centerPane.prefWidthProperty().bind(imageView.fitWidthProperty());
        centerPane.prefHeightProperty().bind(imageView.fitHeightProperty());
        imageView.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()){
                dragDistance=new Point2D(mouseEvent.getSceneX(),mouseEvent.getScreenY());
                dragDistance=dragDistance.subtract(imageView.getParent().localToScene(new Point2D(imageView.getTranslateX(), imageView.getTranslateY())));

            }
        });
        imageView.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()){
                Point2D point2D = new Point2D(mouseEvent.getSceneX(), mouseEvent.getScreenY());
                point2D = imageView.getParent().sceneToLocal(point2D.subtract(dragDistance));
                imageView.setTranslateX(point2D.getX());
                imageView.setTranslateY(point2D.getY());
            }

        });
        imageView.imageProperty().addListener((observableValue, image, t1) -> {
            if (t1 != null){
                imageView.setScaleX(1.0);
                imageView.setScaleY(1.0);
                imageView.setTranslateX(0.0);
                imageView.setTranslateY(0.0);

                resize();

            }
        });

        // 图片位置绑定
        imageView.layoutXProperty().bind(Bindings
                .createDoubleBinding(() -> {
                            Bounds layoutBounds = imageView.getLayoutBounds();
                            return (centerPane.getWidth() - layoutBounds.getWidth()) / 2;
                        },
                        centerPane.widthProperty(),
                        imageView.layoutBoundsProperty()));
        imageView.layoutYProperty().bind(Bindings
                .createDoubleBinding(() -> {
                            Bounds layoutBounds = imageView.getLayoutBounds();
                            return (centerPane.getHeight() - layoutBounds.getHeight()) / 2;
                        },
                        centerPane.heightProperty(),
                        imageView.layoutBoundsProperty()));



        Button preBtn=new Button("上一个");
        preBtn.setOnAction(event -> viewModel.pre());
        StackPane leftPane=new StackPane(preBtn);

        Button nextBtn=new Button("下一个");
        nextBtn.setOnAction(event -> viewModel.next());
        StackPane rightPane=new StackPane(nextBtn);


        Button saveBtn=new Button("下载");
        saveBtn.setOnAction(event -> {
            FileChooser fileChooser=new FileChooser();
            fileChooser.setTitle("保存图片");
            fileChooser.setInitialFileName(viewModel.getImageName());
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("图片","*.jpg","*.jpeg","*.png"));

            File file = fileChooser.showSaveDialog(App.mainStage);
            if (file != null)
                viewModel.download(file);

        });


        AnchorPane anchorPane=new AnchorPane(centerPane,leftPane,rightPane,saveBtn);

        AnchorPaneUtil.setPosition(centerPane,0.0,0.0,0.0,0.0);
        AnchorPaneUtil.setPosition(leftPane,0.0,null,0.0,0.0);
        AnchorPaneUtil.setPosition(rightPane,0.0,0.0,0.0,null);
        AnchorPaneUtil.setPosition(saveBtn,10.0,10.0,null,null);


        root.setOnScroll(scrollEvent -> {
            double size;
            if (scrollEvent.getDeltaY() > 0){
                size= imageView.getScaleX()+0.1;
            }else {
                size= imageView.getScaleX()-0.1;
            }
            if (size < 0.2) return;
            imageView.setScaleX(size);
            imageView.setScaleY(size);
        });

        root.getChildren().addAll(anchorPane);

    }


    private void resize(){
        Image image=imageView.getImage();
        // 自适应窗口
        double height = root.getHeight();
        double width = root.getWidth();

        if (image.getWidth() < width && image.getHeight() < height) {
            imageView.setFitWidth(image.getWidth());
            imageView.setFitHeight(image.getHeight());
        } else {
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
        }
    }


    public void update(List<Track> filePaths, int index){
        viewModel.update(filePaths, index);

    }

    public StackPane getRoot() {
        return root;
    }
}