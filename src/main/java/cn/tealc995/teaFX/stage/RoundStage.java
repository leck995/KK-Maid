package cn.tealc995.teaFX.stage;

import cn.tealc995.teaFX.stage.handler.ResizeWindowHandler;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.StageStyle;

/**
 * @program: teafx
 * @description: 无修饰圆角窗体
 * @author: Leck
 * @create: 2023-08-11 17:28
 */
public class RoundStage extends TeaStage {
    private StackPane root,content,shadowPane;
    private SimpleDoubleProperty paddingSize;//根布局padding
    private SimpleDoubleProperty radius;//圆角size
    private SimpleDoubleProperty radiusBack;//圆角备份，用于最大化及分屏还原圆角
    private SimpleObjectProperty<Color> dropShadowColor;

    public RoundStage() {
        super();
        paddingSize=new SimpleDoubleProperty(3);
        radius=new SimpleDoubleProperty(15);
        radiusBack=new SimpleDoubleProperty(radius.get());
        dropShadowColor=new SimpleObjectProperty<>(Color.web("#1a1a21"));
        content=new StackPane();
        Rectangle rect=new Rectangle();
        rect.widthProperty().bind(content.widthProperty());
        rect.heightProperty().bind(content.heightProperty());
        rect.arcHeightProperty().bind(radiusBack);
        rect.arcWidthProperty().bind(radiusBack);
        content.setClip(rect);

        Region resizeConner=new Region();
        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M542.72 884.053333l341.333333-341.333333a32 32 0 0 1 47.445334 42.816l-2.197334 2.432-341.333333 341.333333a32 32 0 0 1-47.466667-42.837333l2.197334-2.432 341.333333-341.333333-341.333333 341.333333z m-437.333333-10.666666l778.666666-778.666667a32 32 0 0 1 47.445334 42.816l-2.197334 2.432-778.666666 778.666667a32 32 0 0 1-47.466667-42.837334l2.197333-2.432 778.666667-778.666666-778.666667 778.666666z");
        resizeConner.setShape(svgPath);
        resizeConner.setBackground(new Background(new BackgroundFill(Color.GREY, null,null)));
        resizeConner.setMaxSize(16.0,16.00);
        resizeConner.setPrefSize(16.0,16.0);
        StackPane resizePane=new StackPane(resizeConner);
        ResizeWindowHandler resizeWindowHandler = new ResizeWindowHandler(this);
        resizePane.setOnMousePressed(resizeWindowHandler);
        resizePane.setOnMouseDragged(resizeWindowHandler);
        resizePane.setOnMouseEntered(resizeWindowHandler);
        resizePane.setOnMouseExited(resizeWindowHandler);
        resizePane.setMaxSize(16,16);
        resizePane.setTranslateX(-3);
        resizePane.setTranslateY(-3);
        resizePane.visibleProperty().bind(maxedProperty().not());


        shadowPane=new StackPane(content,resizePane);
        StackPane.setAlignment(resizePane, Pos.BOTTOM_RIGHT);
        DropShadow dropShadow = new DropShadow(BlurType.THREE_PASS_BOX,dropShadowColor.get() , 10, 0, 1.5, 1.5);
        dropShadow.colorProperty().bind(dropShadowColor);
        shadowPane.setEffect(dropShadow);
        root=new StackPane(shadowPane);
        root.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        root.setPadding(new Insets(getPaddingSize()));
        Scene scene=new Scene(root);
        scene.setFill(null);
        initStyle(StageStyle.TRANSPARENT);
        setScene(scene);

        maxedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1){
                root.setPadding(new Insets(0));
                radiusBack.set(0);
            }else {
                root.setPadding(new Insets(getPaddingSize()));
                radiusBack.set(radius.get());
            }
        });

        fitScreenProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1){
                root.setPadding(new Insets(0));
                radiusBack.set(0);
            }else {
                root.setPadding(new Insets(getPaddingSize()));
                radiusBack.set(radius.get());
            }
        });

        fullScreenProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1){
                root.setPadding(new Insets(0));
                radiusBack.set(0);
            }else {
                root.setPadding(new Insets(getPaddingSize()));
                radiusBack.set(radius.get());
            }
        });


    }





    public void setContent(Region region){
        content.getChildren().add(region);
    }

    @Override
    public void setToOriginScreen(Double positionX,Double positionY) {
        super.setToOriginScreen(positionX,positionY);
    }

    /**
     * @description: 最大化
     * @name: setMaxed
     * @author: Leck
     * @param:
     * @return  void
     * @date:   2023/8/11
     */
    @Override
    public void setMaxed(boolean maxed){
        super.setMaxed(maxed);
    }

    @Override
    public void setToLeftScreen(){
        super.setToLeftScreen();
    }
    @Override
    public void setToRightScreen(){
        super.setToRightScreen();
    }
    @Override
    public void setToTopLeftScreen(){
        super.setToTopLeftScreen();
    }
    @Override
    public void setToTopRightScreen(){
     super.setToTopRightScreen();
    }
    @Override
    public void setToBottomLeftScreen(){
        super.setToBottomLeftScreen();
    }
    @Override
    public void setToBottomRightScreen(){
        super.setToBottomRightScreen();
    }





    public double getPaddingSize() {
        return paddingSize.get();
    }

    public SimpleDoubleProperty paddingSizeProperty() {
        return paddingSize;
    }

    public void setPaddingSize(double paddingSize) {
        this.paddingSize.set(paddingSize);
    }

    public double getRadius() {
        return radius.get();
    }

    public SimpleDoubleProperty radiusProperty() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius.set(radius);
    }

    public void setDropShadowColor(Color dropShadowColor) {
        this.dropShadowColor.set(dropShadowColor);
    }


}