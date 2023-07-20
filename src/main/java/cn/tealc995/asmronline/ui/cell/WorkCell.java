package cn.tealc995.asmronline.ui.cell;

import cn.tealc995.asmronline.App;
import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.api.StarApi;
import cn.tealc995.asmronline.api.model.Role;
import cn.tealc995.asmronline.api.model.Work;
import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.GridItemRemoveEvent;
import cn.tealc995.asmronline.event.MainDialogEvent;
import cn.tealc995.asmronline.event.SearchEvent;
import cn.tealc995.asmronline.ui.CategoryType;
import cn.tealc995.asmronline.ui.DetailUi;
import cn.tealc995.asmronline.util.AnchorPaneUtil;
import cn.tealc995.teaFX.controls.notification.MessageType;
import cn.tealc995.teaFX.controls.notification.Notification;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.Rating;

import java.io.File;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 21:53
 */
public class WorkCell extends VBox {
    public WorkCell(Work work){
        setId(work.getId());
        ImageView itemAlbum = new ImageView();
        itemAlbum.setImage(new Image(work.getMainCoverUrl(),400,300,true,true,true));

        /* itemAlbum.setViewport(new Rectangle2D(0, 0, 400, 300));*/
        itemAlbum.setSmooth(true);
        itemAlbum.setPreserveRatio(true);
        itemAlbum.setId("grid-item-cover");
        itemAlbum.getStyleClass().add("list-item-image");
        Rectangle rectangle=new Rectangle(400,300);
        rectangle.setArcHeight(15);
        rectangle.setArcWidth(15);
        itemAlbum.setClip(rectangle);

        Label itemTitle = new Label();
        itemTitle.setId("grid-item-title");
        itemTitle.getStyleClass().add("list-item-title");
        itemTitle.setWrapText(true);
        itemTitle.setText(work.getTitle());


        Label circleLabel = new Label();
        circleLabel.setGraphic(new Region());
        if (work.getCircle() != null) {
            circleLabel.setText(work.getCircle().getName());
        }else {
            circleLabel.setVisible(false);
        }
        circleLabel.setId("grid-item-circle");
        circleLabel.getStyleClass().add("list-item-circle");
        circleLabel.setOnMouseClicked(mouseEvent -> {
            EventBusUtil.getDefault().post(new SearchEvent(CategoryType.CIRCLE,work.getCircle().getName()));
            mouseEvent.consume();
        });

        Label rjLabel = new Label();
        rjLabel.setId("grid-item-rj");
        if (work.getId()!= null){
            rjLabel.setText(work.getId());
        }else {
            rjLabel.setVisible(false);
        }
        rjLabel.getStyleClass().add("list-item-rj");
        rjLabel.setOnMouseClicked(mouseEvent -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(work.getId());
            clipboard.setContent(content);
            Notification.show(String.format("%s:已经复制到剪切板", work.getId()), MessageType.SUCCESS,2000, Pos.TOP_CENTER, App.mainStage);
            mouseEvent.consume();
        });

        Label dateLabel = new Label(work.getRelease());
        dateLabel.setId("grid-item-date");
        dateLabel.getStyleClass().add("list-item-date");





        AnchorPane headPane = new AnchorPane(itemAlbum, rjLabel, dateLabel);
        headPane.getStyleClass().add("list-item-head-pane");
        AnchorPaneUtil.setPosition(rjLabel, 10.0, null, null, 10.0);
        AnchorPaneUtil.setPosition(dateLabel, null, 5.0, 5.0, null);






        HBox tagPane = new HBox();
        tagPane.setAlignment(Pos.CENTER_RIGHT);
        tagPane.setId("list-item-tag-pane");
        tagPane.setSpacing(10);

        if (!work.isNsfw()) {
            Label nsfwLabel= new Label("全年龄");
            nsfwLabel.getStyleClass().add("list-item-tag-nsfw");
            nsfwLabel.setId("grid-item-nsfw");
            tagPane.getChildren().add(nsfwLabel);
        }

        if (work.isHas_subtitle()) {
            Label label = new Label("字幕");
            label.setId("grid-item-subtext");
            label.getStyleClass().add("list-item-tag");
            tagPane.getChildren().add(label);
        }



        Rating rating = new Rating();
        rating.setPartialRating(true);
        if (work.getUserRating() != null){
            rating.setRating(work.getUserRating());
            rating.getStyleClass().add("user-rating");

            ToggleButton starBtn=new ToggleButton();
            starBtn.setSelected(true);
            starBtn.setGraphic(new Region());
            starBtn.setId("grid-item-star");
            starBtn.getStyleClass().addAll("lc-svg-btn", "list-item-star-btn");
            starBtn.setOnAction(actionEvent -> {
                starBtn.setVisible(false);
                EventBusUtil.getDefault().post(new GridItemRemoveEvent(work));
                rating.getStyleClass().remove("user-rating");
                //rating.setRating(work.getRate_average_2dp());
                boolean b = StarApi.deleteStar(Config.HOST.get(), work.getId());

                if (b){
                    Notification.show(String.format("成功取消收藏作品(RJ%s)",work.getId()), MessageType.SUCCESS,Pos.TOP_CENTER, App.mainStage);
                }else {
                    Notification.show(String.format("错误：无法取消收藏作品(RJ%s)",work.getId()), MessageType.FAILED,Pos.TOP_CENTER, App.mainStage);
                }


            });
            headPane.getChildren().add(starBtn);
            AnchorPaneUtil.setPosition(starBtn, 10.0, 10.0, null, null);


        }else {
            rating.setRating(work.getRate_average_2dp());
        }

        rating.setMax(5);


        rating.addEventFilter(MouseEvent.MOUSE_CLICKED,event -> {
            if (Config.TOKEN.get() == null || Config.TOKEN.get().length() == 0){
                Notification.show("使用收藏功能需要在设置中填写Token", MessageType.WARNING,2000,Pos.TOP_CENTER,App.mainStage);
                event.consume();
            }
        });


        rating.ratingProperty().addListener((observableValue, old, t1) -> {
            int i = (int) Math.ceil(t1.doubleValue());
            if ((int) Math.ceil(old.doubleValue()) == i){
                return;
            }

            rating.getStyleClass().add("user-rating");
            rating.setRating(i);
            boolean b = StarApi.updateStar(Config.HOST.get(), work.getId(), i);
            if (b){
                Notification.show(String.format("成功收藏作品(RJ%s)",work.getId()), MessageType.SUCCESS,Pos.TOP_CENTER, App.mainStage);
            }else {
                Notification.show(String.format("错误：无法收藏作品(RJ%s)",work.getId()), MessageType.FAILED,Pos.TOP_CENTER, App.mainStage);
            }



        });
        BorderPane borderPane1 = new BorderPane();
        borderPane1.setLeft(rating);
        borderPane1.setRight(tagPane);


        getChildren().addAll(headPane, itemTitle, borderPane1, circleLabel);
        setSpacing(10);
        EventHandler<MouseEvent> labelHandler = mouseEvent -> {
            Label source = (Label) mouseEvent.getSource();
            if (source.getAccessibleText().equals("category")) {
                EventBusUtil.getDefault().post(new SearchEvent(CategoryType.TAG,source.getText()));
                mouseEvent.consume();
            }
            if (source.getAccessibleText().equals("va")) {
                EventBusUtil.getDefault().post(new SearchEvent(CategoryType.VA,source.getText()));
                mouseEvent.consume();
            }
            mouseEvent.consume();
        };
        FlowPane categoriesPane = new FlowPane();
        categoriesPane.setId("grid-item-categories");
        if (work.getTags() != null) {
            categoriesPane.setVgap(5);
            categoriesPane.setHgap(5);

            Label label;
            for (Role category : work.getTags()) {
                label = new Label(category.getName());
                label.setAccessibleText("category");
                label.setOnMouseClicked(labelHandler);
                label.getStyleClass().add("tag-label");
                categoriesPane.getChildren().add(label);
            }
        }
        getChildren().add(categoriesPane);
        FlowPane vaPane = new FlowPane();
        vaPane.setId("grid-item-vas");
        vaPane.setMaxHeight(90);
        if (work.getVas() != null) {
            vaPane.setVgap(5);
            vaPane.setHgap(5);
            Label label;
            for (Role va : work.getVas()) {
                label = new Label(va.getName());
                label.setOnMouseClicked(labelHandler);
                label.setAccessibleText("va");
                label.getStyleClass().add("actor-label");
                vaPane.getChildren().add(label);
            }
        }
        getChildren().add(vaPane);

        getStyleClass().add("list-item-pane");

        setOnMouseClicked(mouseEvent -> {
            DetailUi detailUi=new DetailUi(work);
            EventBusUtil.getDefault().post(new MainDialogEvent(detailUi.getRoot()));
        });

    }
}