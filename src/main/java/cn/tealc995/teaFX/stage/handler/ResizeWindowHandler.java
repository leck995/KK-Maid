package cn.tealc995.teaFX.stage.handler;

import cn.tealc995.teaFX.stage.TeaStage;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Screen;

/**
 * @program: JavaFXTitleBarDemo
 * @description:
 * @author: leck
 * @create: 2021-08-23 18:20
 */
public class ResizeWindowHandler implements EventHandler<MouseEvent> {
    private TeaStage primaryStage;
    private double oldScreenX;
    private double oldScreenY;

    private double minWidth = 1100;
    private double minHeight = 750;

    public ResizeWindowHandler(TeaStage primaryStage) { //构造器
        this.primaryStage = primaryStage;
    }

    public ResizeWindowHandler(TeaStage primaryStage, double minWidth, double minHeight) {
        this.primaryStage = primaryStage;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    @Override
    public void handle(MouseEvent e) {

        Screen screen = (Screen)Screen.getScreensForRectangle(primaryStage.getX(),primaryStage.getY(),primaryStage.getWidth(), primaryStage.getHeight()).get(0);
        Rectangle2D bounds = screen.getVisualBounds();
        Region region = (Region) e.getSource();
        //更换鼠标样式
        if (e.getEventType() == MouseEvent.MOUSE_ENTERED){
            region.setCursor(Cursor.NW_RESIZE);
        }

        if (e.getEventType() == MouseEvent.MOUSE_EXITED){
            region.setCursor(Cursor.DEFAULT);
        }

        if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {    //鼠标按下的事件
            this.oldScreenX = e.getScreenX();
            this.oldScreenY = e.getScreenY();
        } else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {  //鼠标拖动的事件
            //小于最小尺寸不让其再缩小
            if (this.primaryStage.getWidth() < minWidth) {
                this.primaryStage.setWidth(minWidth);
                return;
            }
            if ( this.primaryStage.getHeight() < minHeight) {
                this.primaryStage.setHeight(minHeight);
                return;
            }


            this.primaryStage.setWidth(primaryStage.getWidth()+e.getScreenX()-this.oldScreenX);
            this.primaryStage.setHeight(primaryStage.getHeight()+e.getScreenY()-this.oldScreenY);
            this.oldScreenX = e.getScreenX();
            this.oldScreenY = e.getScreenY();
            primaryStage.setFitScreen(false);



        }
    }
}
