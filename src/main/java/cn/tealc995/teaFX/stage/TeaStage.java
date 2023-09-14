package cn.tealc995.teaFX.stage;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @program: teafx
 * @description: 无修饰圆角窗体
 * @author: Leck
 * @create: 2023-08-11 17:28
 */
public class TeaStage extends Stage {

    private SimpleBooleanProperty maxed;
    private SimpleBooleanProperty fullScreened;//stage的fullScreen()方法是只读的，无法进行双向绑定，故包装一下
    private BoundingBox maximizedBox;
    BoundingBox originBox;
    SimpleBooleanProperty fitScreen;//一旦分屏，全屏等操作，都会为true


    public TeaStage() {
        super();
        maxed=new SimpleBooleanProperty(false);
        fitScreen=new SimpleBooleanProperty(false);
        fullScreened=new SimpleBooleanProperty(isFullScreen());
        maxed.addListener((observableValue, aBoolean, t1) -> {
            if (t1){
                originBox = new BoundingBox(getX(), getY(), getWidth(), getHeight());
                Screen screen = Screen.getScreensForRectangle(getX(),getY(),getWidth(), getHeight()).get(0);
                Rectangle2D bounds = screen.getVisualBounds();
                maximizedBox = new BoundingBox(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
                setX(maximizedBox.getMinX());
                setY(maximizedBox.getMinY());
                setWidth(maximizedBox.getWidth());
                setHeight(maximizedBox.getHeight());
                fitScreen.set(true);
            }else {
                setX(originBox.getMinX());
                setY(originBox.getMinY());
                setWidth(originBox.getWidth());
                setHeight(originBox.getHeight());
                fitScreen.set(false);
            }
        });

        fullScreened.addListener((observableValue, aBoolean, t1) -> {
            super.setFullScreen(t1);
        });

    }





    /**
     * @description: 分屏最大化返回原尺寸
     * @name: setToOriginScreen
     * @author: Leck
     * @param:	positionX
     * @param:	positionY
     * @return  void
     * @date:   2023/8/11
     */
    public void setToOriginScreen(Double positionX,Double positionY){
        if (isMaxed()){
            maxed.set(false);
        }
        setWidth(originBox.getWidth());
        setHeight(originBox.getHeight());
        fitScreen.set(false);
        setX(positionX - getWidth() / 2 );
        setY(positionY);




        //new Robot().mouseMove(positionX,positionY);
    }


    /**
     * @description: 最大化
     * @name: setMaxed
     * @author: Leck
     * @param:
     * @return  void
     * @date:   2023/8/11
     */
    public void setMaxed(boolean maxed){
        this.maxed.set(maxed);
/*        if (maxed){
            originBox = new BoundingBox(getX(), getY(), getWidth(), getHeight());
            Screen screen = Screen.getScreensForRectangle(getX(),getY(),getWidth(), getHeight()).get(0);
            Rectangle2D bounds = screen.getVisualBounds();
            maximizedBox = new BoundingBox(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
            setX(maximizedBox.getMinX());
            setY(maximizedBox.getMinY());
            setWidth(maximizedBox.getWidth());
            setHeight(maximizedBox.getHeight());
            this.maxed.set(true);
            fitScreen.set(true);
        }else {
            System.out.println("222aaa");
            setX(originBox.getMinX());
            setY(originBox.getMinY());
            setWidth(originBox.getWidth());
            setHeight(originBox.getHeight());
            this.maxed.set(false);
            fitScreen.set(false);
        }*/
    }




    /**
     * @description: 左分屏
     * @name: setToLeftScreen
     * @author: Leck
     * @param:
     * @return  void
     * @date:   2023/8/11
     */
    public void setToLeftScreen(){
        originBox = new BoundingBox(getX(), getY(), getWidth(), getHeight());
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        setWidth(visualBounds.getWidth() / 2);
        setHeight(visualBounds.getHeight());
        setX(0);
        setY(0);
        fitScreen.set(true);
    }

    /**
     * @description:右分屏
     * @name: setToRightScreen
     * @author: Leck
     * @param:
     * @return  void
     * @date:   2023/8/11
     */
    public void setToRightScreen(){
        if (isMaxed()) return;
        originBox = new BoundingBox(getX(), getY(), getWidth(), getHeight());
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        setWidth(visualBounds.getWidth() / 2);
        setHeight(visualBounds.getHeight());
        setX(visualBounds.getWidth() / 2 + 1);
        setY(0);
        fitScreen.set(true);
    }

    /**
     * @description: 左上分屏
     * @name: setToTopLeftScreen
     * @author: Leck
     * @param:
     * @return  void
     * @date:   2023/8/11
     */
    public void setToTopLeftScreen(){
        if (isMaxed()) return;
        originBox = new BoundingBox(getX(), getY(), getWidth(), getHeight());
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        setWidth(visualBounds.getWidth() / 2);
        setHeight(visualBounds.getHeight() / 2);
        setX(0);
        setY(0);
        fitScreen.set(true);
    }
    /**
     * @description: 右上分屏
     * @name: setToTopRightScreen
     * @author: Leck
     * @param:
     * @return  void
     * @date:   2023/8/11
     */
    public void setToTopRightScreen(){
        originBox = new BoundingBox(getX(), getY(), getWidth(), getHeight());
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        setWidth(visualBounds.getWidth() / 2);
        setHeight(visualBounds.getHeight() / 2);
        setX(visualBounds.getWidth() / 2 +1);
        setY(0);
        fitScreen.set(true);
    }

    /**
     * @description: 底部左分屏
     * @name: setToBottomLeftScreen
     * @author: Leck
     * @param:
     * @return  void
     * @date:   2023/8/11
     */
    public void setToBottomLeftScreen(){
        originBox = new BoundingBox(getX(), getY(), getWidth(), getHeight());
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        setWidth(visualBounds.getWidth() / 2);
        setHeight(visualBounds.getHeight() / 2);
        setX(0);
        setY(visualBounds.getHeight() / 2 + 1);
        fitScreen.set(true);
    }
    /**
     * @description: 底部右分屏
     * @name: setToBottomRightScreen
     * @author: Leck
     * @param:
     * @return  void
     * @date:   2023/8/11
     */
    public void setToBottomRightScreen(){
        originBox = new BoundingBox(getX(), getY(), getWidth(), getHeight());
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        setWidth(visualBounds.getWidth() / 2);
        setHeight(visualBounds.getHeight() / 2);
        setX(visualBounds.getWidth() - visualBounds.getWidth() / 2 + 1);
        setY(visualBounds.getHeight() -visualBounds.getHeight() / 2 + 1);
        fitScreen.set(true);
    }



    public boolean isMaxed() {
        return maxed.get();
    }

    public SimpleBooleanProperty maxedProperty() {
        return maxed;
    }

    public boolean isFitScreen() {
        return fitScreen.get();
    }

    public SimpleBooleanProperty fitScreenProperty() {
        return fitScreen;
    }

    public void setFitScreen(boolean fitScreen) {
        this.fitScreen.set(fitScreen);
    }


    public boolean isFullScreened() {
        return fullScreened.get();
    }

    public SimpleBooleanProperty fullScreenedProperty() {
        return fullScreened;
    }

    public void setFullScreened(boolean fullScreened) {
        this.fullScreened.set(fullScreened);
    }
}