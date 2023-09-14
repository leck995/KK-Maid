package cn.tealc995.teaFX.stage.handler;


import cn.tealc995.teaFX.stage.TeaStage;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @program: JavaFXTitleBarDemo
 * @description:
 * @author: leck
 * @create: 2021-08-23 18:20
 */
public class DragWindowHandler implements EventHandler<MouseEvent> {
    private TeaStage primaryStage;
    private boolean autoScreen;

    private SimpleObjectProperty<Pos> position;

    private Stage hideStage;
    private Stage popup;


    private double oldStageX;
    private double oldStageY;
    private double oldScreenX;
    private double oldScreenY;

    public DragWindowHandler(TeaStage primaryStage, boolean autoScreen) { //构造器
        this.primaryStage = primaryStage;
        this.autoScreen = autoScreen;
        position=new SimpleObjectProperty<>();
        position.addListener((observableValue, aBoolean, t1) -> {
            if (t1 != null){
                if (hideStage == null){ init();}
                Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
                switch (t1){
                    case TOP_LEFT ->{
                        popup.setWidth(visualBounds.getWidth() / 2);
                        popup.setHeight(visualBounds.getHeight() / 2);
                        popup.setX(0);
                        popup.setY(0);
                    }
                    case TOP_CENTER -> {
                        popup.setWidth(visualBounds.getWidth());
                        popup.setHeight(visualBounds.getHeight());
                        popup.setX(0);
                        popup.setY(0);
                    }
                    case TOP_RIGHT -> {
                        popup.setWidth(visualBounds.getWidth() / 2);
                        popup.setHeight(visualBounds.getHeight() / 2);
                        popup.setX(visualBounds.getWidth() / 2 +1);
                        popup.setY(0);
                    }
                    case CENTER_LEFT -> {
                        popup.setWidth(visualBounds.getWidth() / 2);
                        popup.setHeight(visualBounds.getHeight());
                        popup.setX(0);
                        popup.setY(0);
                    }
                    case CENTER_RIGHT -> {
                        popup.setWidth(visualBounds.getWidth() / 2);
                        popup.setHeight(visualBounds.getHeight());
                        popup.setX(visualBounds.getWidth() / 2 + 1);
                        popup.setY(0);
                    }
                    case BOTTOM_LEFT -> {
                        popup.setWidth(visualBounds.getWidth() / 2);
                        popup.setHeight(visualBounds.getHeight() / 2);
                        popup.setX(0);
                        popup.setY(visualBounds.getHeight() / 2 + 1);
                    }
                    case BOTTOM_RIGHT -> {
                        popup.setWidth(visualBounds.getWidth() / 2);
                        popup.setHeight(visualBounds.getHeight() / 2);
                        popup.setX(visualBounds.getWidth() - visualBounds.getWidth() / 2 + 1);
                        popup.setY(visualBounds.getHeight() -visualBounds.getHeight() / 2 + 1);
                    }
                }
                if (popup.isShowing()){
                    popup.setOpacity(1.0);
                    popup.toFront();
                }else {
                    popup.show();
                }

                primaryStage.toFront();
            }else {
                if (popup != null){
                    popup.setOpacity(0.0);

                }
            }
        });
    }

    @Override
    public void handle(MouseEvent e) {
        Screen screen =Screen.getScreensForRectangle(primaryStage.getX(),primaryStage.getY(),primaryStage.getWidth(), primaryStage.getHeight()).get(0);
        Rectangle2D bounds = screen.getVisualBounds();


        if (primaryStage.isFullScreen()) return;
        //if (primaryStage.getWidth()==bounds.getWidth() && primaryStage.getHeight()==bounds.getHeight()) return;

        if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {    //鼠标按下的事件
            if (e.getClickCount() == 2){

                primaryStage.setMaxed(!primaryStage.isMaxed());
            }

        } else if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {    //鼠标按下的事件
            this.oldStageX = this.primaryStage.getX();
            this.oldStageY = this.primaryStage.getY();
            this.oldScreenX = e.getScreenX();
            this.oldScreenY = e.getScreenY();

        } else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {  //鼠标拖动的事件

            double screenX = e.getScreenX();
            double screenY = e.getScreenY();
            if (primaryStage.isFitScreen()){
                primaryStage.setToOriginScreen(screenX,screenY);
                this.oldStageX = this.primaryStage.getX();
                this.oldStageY = this.primaryStage.getY();
                this.oldScreenX = e.getScreenX();
                this.oldScreenY = e.getScreenY();

                return;
            }

            this.primaryStage.setX(e.getScreenX() - this.oldScreenX + this.oldStageX);
            this.primaryStage.setY(e.getScreenY() - this.oldScreenY + this.oldStageY);

            if (autoScreen){
                if(screenY == 0){
                    if(screenX < 200){
                        position.set(Pos.TOP_LEFT);
                    }else if (screenX > bounds.getWidth()-200){
                        position.set(Pos.TOP_RIGHT);
                    }else{
                        position.set(Pos.TOP_CENTER);
                    }
                }else if (screenX == 0){
                    if(screenY < 200){
                        position.set(Pos.TOP_LEFT);
                    }else if (screenY > bounds.getHeight()-200){
                        position.set(Pos.BOTTOM_LEFT);
                    }else{
                        position.set(Pos.CENTER_LEFT);
                    }
                }else if (screenX == bounds.getWidth() - 1){
                    if(screenY < 200){
                        position.set(Pos.TOP_RIGHT);
                    }else if (screenY > bounds.getHeight()-200){
                        position.set(Pos.BOTTOM_RIGHT);
                    }else{
                        position.set(Pos.CENTER_RIGHT);
                    }
                }else {
                    position.set(null);
                }
            }
        } else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) { //点击释放事件
            if (position.get() != null && popup != null){

                switch (position.get()){
                    case TOP_LEFT ->{
                        primaryStage.setToTopLeftScreen();
                    }
                    case TOP_CENTER -> {
                        primaryStage.setMaxed(true);
                    }
                    case TOP_RIGHT -> {
                        primaryStage.setToTopRightScreen();
                    }
                    case CENTER_LEFT -> {
                        primaryStage.setToLeftScreen();
                    }
                    case CENTER_RIGHT -> {
                        primaryStage.setToRightScreen();
                    }
                    case BOTTOM_LEFT -> {
                        primaryStage.setToBottomLeftScreen();
                    }
                    case BOTTOM_RIGHT -> {
                        primaryStage.setToBottomRightScreen();
                    }
                }

                position.set(null);
                popup.close();
                hideStage.close();
                popup=null;
                hideStage=null;
            }

        }
    }


    private void init(){
        hideStage=new Stage();
        hideStage.initStyle(StageStyle.UTILITY);
        hideStage.setWidth(100);
        hideStage.setHeight(100);
        hideStage.setOpacity(0.0);
        hideStage.show();

        popup=new Stage();
        popup.initOwner(hideStage);
        popup.initStyle(StageStyle.TRANSPARENT);

        Region region=new Region();
        region.setBackground(new Background(new BackgroundFill(Color.web("#3c3f41"),new CornerRadii(10),null)));
        region.setEffect(new DropShadow(5,Color.BLACK));
        StackPane stackPane=new StackPane(region);
        stackPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        stackPane.setPadding(new Insets(8.0));
        Scene scene = new Scene(stackPane);
        scene.setFill(null);
        popup.setScene(scene);

        //popup.getScene().setFill(Color.TRANSPARENT);
/*        Region region=new Region();
        region.setBackground(new Background(new BackgroundFill(Color.web("#3c3f41"),new CornerRadii(10),null)));
        region.setEffect(new DropShadow(5,Color.BLACK));


        StackPane stackPane=new StackPane(region);
        stackPane.setPadding(new Insets(8.0));
        Scene scene = new Scene(stackPane);
        scene.setFill(Color.TRANSPARENT);
        hideStage.setScene(scene);*/

    }
}
