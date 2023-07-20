package cn.tealc995.teaFX.controls.notification;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @program: AzurLanePocket
 * @description:
 * @author: Leck
 * @create: 2023-06-03 05:43
 */
public class Notification {
    public static void show(String text,MessageType type,Pos pos,Stage stage){
        Popup popup=new Popup();
        int width = text.length() * 20 + 60;
        Label tip=new Label(text);
        tip.setMaxWidth(420);
        tip.setFont(Font.font(18));
        HBox root=new HBox(tip);
        root.setBackground(new Background(new BackgroundFill(Color.web("#000000"),new CornerRadii(35), Insets.EMPTY)));
        root.setSpacing(15);
        root.setPadding(new Insets(10,15,10,15));
        root.setPrefSize(width,35);
        root.setMaxSize(450,35);
        root.setAlignment(Pos.CENTER);

        root.getStyleClass().add("notification");

        switch (type){
            case WARNING -> root.getChildren().add(0,getWaringIcon());
            case SUCCESS -> root.getChildren().add(0,getSuccessIcon());
            case FAILED -> root.getChildren().add(0,getFailedIcon());
        }
        popup.getScene().setRoot(root);
//        popup.getContent().clear();
//        popup.getContent().setAll(root);
        //popup.getScene().setFill(Color.RED);
        Point2D position = getPosition(pos, Math.min(width, 450), stage);
        popup.show(stage,position.getX(), position.getY());
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    popup.hide();
                });
            }
        },1200);
    }
    public static void show(String text,MessageType type,Integer delay,Pos pos,Stage stage){
        if (text == null) return;
        Popup popup=new Popup();
        int width = text.length() * 20 + 60;
        Label tip=new Label(text);
        tip.setMaxWidth(420);
        tip.setFont(Font.font(18));
        HBox root=new HBox(tip);
        root.setBackground(new Background(new BackgroundFill(Color.web("#000000"),new CornerRadii(35), Insets.EMPTY)));
        root.setSpacing(15);
        root.setPadding(new Insets(10,15,10,15));
        root.setPrefSize(width,35);
        root.setMaxSize(450,35);
        root.setAlignment(Pos.CENTER);

        root.getStyleClass().add("notification");
        switch (type){
            case WARNING -> root.getChildren().add(0,getWaringIcon());
            case SUCCESS -> root.getChildren().add(0,getSuccessIcon());
            case FAILED -> root.getChildren().add(0,getFailedIcon());
        }
        popup.getScene().setRoot(root);
        //popup.getContent().setAll(root);
        //popup.getScene().setFill(Color.TRANSPARENT);
        Point2D position = getPosition(pos, Math.min(width, 450), stage);
        popup.show(stage,position.getX(), position.getY());
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    popup.hide();
                });
            }
        },delay == null ? 1200 : delay);

    }

    private static Point2D getPosition(Pos pos, double width, Stage stage){
        Point2D point;
        switch (pos){
            case TOP_CENTER -> point=new Point2D( (stage.getWidth()-width) / 2 + stage.getX(),30 +stage.getY());
            case BOTTOM_CENTER -> point=new Point2D((stage.getWidth()-width) / 2 + stage.getX(),stage.getHeight()-100 +stage.getY());
            default -> point=new Point2D((stage.getWidth()-width) / 2 + stage.getX(),stage.getHeight()-100 +stage.getY()); //其他类型都没做
        }
        return point;
    }




    private static Region getWaringIcon(){
        Region region = new Region();
        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M512 64c126.677333 3.328 232.192 47.146667 316.501333 131.498667C912.853333 279.808 956.672 385.28 960 512c-3.328 126.677333-47.146667 232.192-131.498667 316.501333C744.192 912.853333 638.72 956.672 512 960c-126.677333-3.328-232.192-47.146667-316.501333-131.498667C111.146667 744.192 67.328 638.72 64 512c3.328-126.677333 47.146667-232.192 131.498667-316.501333C279.808 111.146667 385.28 67.328 512 64zM512 256c-17.322667 0-31.658667 6.357333-43.008 19.029333A58.197333 58.197333 0 0 0 453.973333 320l23.04 256a35.925333 35.925333 0 0 0 11.477334 22.485333 34.048 34.048 0 0 0 23.466666 8.533334 33.621333 33.621333 0 0 0 23.466667-8.533334 36.138667 36.138667 0 0 0 11.52-22.485333l23.04-256a57.984 57.984 0 0 0-15.018667-44.970667A55.381333 55.381333 0 0 0 511.914667 256H512z m0 512c14.677333-0.64 26.88-5.674667 36.522667-15.018667 9.642667-9.344 14.506667-21.333333 14.506666-35.968A49.578667 49.578667 0 0 0 512 665.984a49.493333 49.493333 0 0 0-50.986667 51.029333c0 14.677333 4.821333 26.666667 14.506667 35.968 9.642667 9.301333 21.802667 14.293333 36.48 15.018667z");
        region.setShape(svgPath);
        LinearGradient paint = new LinearGradient(
                0.0, 0.0, 1.0, 0.0, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, new Color(0.9737, 0.781, 0.3651, 1.0)),
                new Stop(1.0, new Color(1.0, 0.7, 0.0, 1.0)));;
        region.setBackground(new Background(new BackgroundFill(paint, null,null)));
        region.setMinSize(24.0,24.00);
        region.setMaxSize(24.0,24.00);
        region.setPrefSize(24.0,24.0);
        return region;
    }
    private static Region getFailedIcon(){
        Region region = new Region();
        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M563.2 512L716.8 358.4 665.6 307.2 512 460.8 358.4 307.2 307.2 358.4 460.8 512 307.2 665.6l51.2 51.2L512 563.2 665.6 716.8l51.2-51.2L563.2 512zM512 4.388571C791.405714 4.388571 1019.611429 231.131429 1019.611429 512S791.405714 1019.611429 512 1019.611429 5.851429 791.405714 5.851429 512 232.594286 4.388571 512 4.388571z");
        region.setShape(svgPath);

        region.setBackground(new Background(new BackgroundFill(Color.web("#eb3d3d"), null,null)));
        region.setMinSize(24.0,24.00);
        region.setMaxSize(24.0,24.00);
        region.setPrefSize(24.0,24.0);
        return region;
    }
    private static Region getSuccessIcon(){
        Region region = new Region();
        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M512 53.248c129.707008 3.412992 237.739008 48.299008 324.096 134.656S967.339008 382.292992 970.752 512c-3.412992 129.707008-48.299008 237.739008-134.656 324.096S641.707008 967.339008 512 970.752c-129.707008-3.412992-237.739008-48.299008-324.096-134.656S56.660992 641.707008 53.248 512c3.412992-129.707008 48.299008-237.739008 134.656-324.096S382.292992 56.660992 512 53.248z m-57.344 548.864l-101.376-101.376c-8.192-7.508992-17.579008-11.264-28.16-11.264-10.580992 0-19.796992 3.924992-27.648 11.776-7.851008 7.851008-11.776 16.896-11.776 27.136s3.755008 19.456 11.264 27.648l130.048 130.048c7.508992 7.508992 16.724992 11.264 27.648 11.264 10.923008 0 20.139008-3.755008 27.648-11.264l269.312-269.312c10.24-10.24 13.483008-22.699008 9.728-37.376-3.755008-14.676992-12.971008-23.892992-27.648-27.648-14.676992-3.755008-27.136-0.512-37.376 9.728L454.656 602.112z");
        region.setShape(svgPath);

        LinearGradient paint = new LinearGradient(
                0.0, 0.0, 1.0, 0.0, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, new Color(0.08,0.9,0.8,1.0)),
                new Stop(1.0, new Color(0.07,0.67,0.91,1.0)));;
        region.setBackground(new Background(new BackgroundFill(paint, null,null)));
        region.setMinSize(24.0,24.00);
        region.setMaxSize(24.0,24.00);
        region.setPrefSize(24.0,24.0);
        return region;
    }

}

