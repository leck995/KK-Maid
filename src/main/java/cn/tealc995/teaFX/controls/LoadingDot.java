package cn.tealc995.teaFX.controls;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * @program: AsmrPlayer
 * @description:
 * @author: Leck
 * @create: 2023-04-17 15:29
 */
public class LoadingDot extends HBox {
    private SimpleDoubleProperty radius=new SimpleDoubleProperty(12);
    private SimpleStringProperty startColor=new SimpleStringProperty("#f6d5f7");
    private SimpleStringProperty endColor=new SimpleStringProperty("#fbe9d7");


    public LoadingDot() {
        init();
    }

    private void init(){
        Circle circle1=new Circle(radius.get(), Color.web(startColor.get()));
        circle1.setCenterX(radius.get());
        circle1.setCenterY(radius.get());
        StackPane circlePane1=new StackPane(circle1);
        circlePane1.setPrefSize(30,20);

        Circle circle2=new Circle(radius.get() * 0.75, Color.web("#f6a3ce"));
        circle2.setCenterX(radius.get());
        circle2.setCenterY(radius.get());
        StackPane circlePane2=new StackPane(circle2);
        circlePane2.setPrefSize(30,20);

        Circle circle3=new Circle(radius.get() * 0.5, Color.web("#f7b4ad"));
        circle3.setCenterX(radius.get());
        circle3.setCenterY(radius.get());
        StackPane circlePane3=new StackPane(circle3);
        circlePane3.setPrefSize(30,20);

        Circle circle4=new Circle(radius.get() * 0.25, Color.web(endColor.get()));
        circle4.setCenterX(radius.get());
        circle4.setCenterY(radius.get());
        StackPane circlePane4=new StackPane(circle4);

        circlePane4.setPrefSize(30,20);

        Timeline timeline=new Timeline(
                new KeyFrame(Duration.millis(200),
                        new KeyValue(circle1.radiusProperty(),radius.get() * 0.75),
                        new KeyValue(circle2.radiusProperty(),radius.get()),
                        new KeyValue(circle3.radiusProperty(),radius.get() * 0.75),
                        new KeyValue(circle4.radiusProperty(),radius.get() * 0.5),

                        new KeyValue(circle2.fillProperty(),Color.web(endColor.get()))),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(circle1.radiusProperty(),radius.get() * 0.5),
                        new KeyValue(circle2.radiusProperty(),radius.get() * 0.75),
                        new KeyValue(circle3.radiusProperty(),radius.get()),
                        new KeyValue(circle4.radiusProperty(),radius.get() * 0.75),

                        new KeyValue(circle3.fillProperty(),Color.web(endColor.get()))),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(circle1.radiusProperty(),radius.get() * 0.25),
                        new KeyValue(circle2.radiusProperty(),radius.get() * 0.5),
                        new KeyValue(circle3.radiusProperty(),radius.get() * 0.75),
                        new KeyValue(circle4.radiusProperty(),radius.get()),

                        new KeyValue(circle1.fillProperty(),Color.web(startColor.get())),
                        new KeyValue(circle4.fillProperty(),Color.web(endColor.get()))),
                new KeyFrame(Duration.millis(800),
                        new KeyValue(circle1.radiusProperty(),radius.get() * 0.5),
                        new KeyValue(circle2.radiusProperty(),radius.get() * 0.25),
                        new KeyValue(circle3.radiusProperty(),radius.get() * 0.5),
                        new KeyValue(circle4.radiusProperty(),radius.get() * 0.75),

                        new KeyValue(circle2.fillProperty(),Color.web(startColor.get()))),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(circle1.radiusProperty(),radius.get() * 0.75),
                        new KeyValue(circle2.radiusProperty(),radius.get() * 0.5),
                        new KeyValue(circle3.radiusProperty(),radius.get() * 0.25),
                        new KeyValue(circle4.radiusProperty(),radius.get() * 0.5),

                        new KeyValue(circle3.fillProperty(),Color.web(startColor.get()))),
                new KeyFrame(Duration.millis(1200),
                        new KeyValue(circle1.radiusProperty(),radius.get()),
                        new KeyValue(circle2.radiusProperty(),radius.get() * 0.75),
                        new KeyValue(circle3.radiusProperty(),radius.get() * 0.5),
                        new KeyValue(circle4.radiusProperty(),radius.get() * 0.25),

                        new KeyValue(circle4.fillProperty(),Color.web(startColor.get())),
                        new KeyValue(circle1.fillProperty(),Color.web(endColor.get())))
               /* new KeyFrame(Duration.millis(600),new KeyValue(circle2.radiusProperty(),15),new KeyValue(circle2.radiusProperty(),radius.get() * 0.5)),
                new KeyFrame(Duration.millis(900),new KeyValue(circle3.radiusProperty(),15),new KeyValue(circle2.radiusProperty(),radius.get() * 0.5)),
                new KeyFrame(Duration.millis(radius.get() * 0.7500),new KeyValue(circle4.radiusProperty(),15),new KeyValue(circle3.radiusProperty(),radius.get() * 0.5))*/

        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        this.visibleProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1){
                timeline.play();
            }else {
                timeline.stop();
            }
        });
        this.getChildren().addAll(circlePane1,circlePane2,circlePane3,circlePane4);
        this.setSpacing(5);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("loading-dot");
    }

}