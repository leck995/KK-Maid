package cn.tealc995.teaFX.handler;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @program: JavaFXTitleBarDemo
 * @description:
 * @author: leck
 * @create: 2021-08-23 18:20
 */
public class DragWindowHandler implements EventHandler<MouseEvent> {
    private Stage primaryStage;
    private double oldStageX;
    private double oldStageY;
    private double oldScreenX;
    private double oldScreenY;

    public DragWindowHandler(Stage primaryStage) { //构造器
        this.primaryStage = primaryStage;
    }

    @Override
    public void handle(MouseEvent e) {
        Screen screen =Screen.getScreensForRectangle(primaryStage.getX(),primaryStage.getY(),primaryStage.getWidth(), primaryStage.getHeight()).get(0);
        Rectangle2D bounds = screen.getVisualBounds();
        if (primaryStage.isFullScreen()) return;
        if (primaryStage.getWidth()==bounds.getWidth() && primaryStage.getHeight()==bounds.getHeight()) return;
        if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {    //鼠标按下的事件
            this.oldStageX = this.primaryStage.getX();
            this.oldStageY = this.primaryStage.getY();
            this.oldScreenX = e.getScreenX();
            this.oldScreenY = e.getScreenY();

        } else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {  //鼠标拖动的事件
            this.primaryStage.setX(e.getScreenX() - this.oldScreenX + this.oldStageX);
            this.primaryStage.setY(e.getScreenY() - this.oldScreenY + this.oldStageY);
        }
    }
}
