package cn.tealc995.kkmaid.ui.cell;

import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.Role;
import cn.tealc995.kikoreu.model.Work;
import cn.tealc995.kkmaid.App;
import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kkmaid.event.*;
import cn.tealc995.kkmaid.service.api.star.StarWorkRemoveTask;
import cn.tealc995.kkmaid.service.api.star.StarWorkAddTask;
import cn.tealc995.kkmaid.ui.CategoryType;
import cn.tealc995.kkmaid.ui.DetailUI;
import cn.tealc995.kkmaid.util.AnchorPaneUtil;
import cn.tealc995.kkmaid.util.FXResourcesLoader;
import cn.tealc995.teaFX.controls.notification.MessageType;
import cn.tealc995.teaFX.controls.notification.Notification;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.Rating;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-12 21:53
 */
public class WorkCell extends VBox {
    public WorkCell(Work work){
        setId(work.getId());
        ImageView itemAlbum = new ImageView();
        if (work.getMainCoverUrl().startsWith("/")){
            itemAlbum.setImage(new Image(FXResourcesLoader.load("/cn/tealc995/kkmaid/image/cover-main.jpg"),400,300,true,true,true));
        }else {
            itemAlbum.setImage(new Image(work.getMainCoverUrl(),400,300,true,true,true));
        }



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
            content.putString(work.getFullId());
            clipboard.setContent(content);
            Notification.show(String.format("%s:已经复制到剪切板", work.getFullId()), MessageType.SUCCESS,2000, Pos.TOP_CENTER, App.mainStage);
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
        Label subtitle = new Label();
        subtitle.setId("grid-item-subtext");
        subtitle.getStyleClass().add("list-item-tag");
        tagPane.getChildren().add(subtitle);
        if (work.isHasLocalSubtitle() && work.isHas_subtitle()){
            subtitle.setText("双字幕");
        }else if (work.isHasLocalSubtitle()){
            subtitle.setText("本地字幕");
        }else if (work.isHas_subtitle()){
            subtitle.setText("字幕");
        }else {
            subtitle.setVisible(false);
        }



        Rating rating = new Rating();
        rating.setMax(5);
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
                StarWorkRemoveTask task=new StarWorkRemoveTask(work.getId());
                task.setOnSucceeded(workerStateEvent -> {
                    ResponseBody<Boolean> value = task.getValue();
                    if(value.isSuccess()){
                        EventBusUtil.getDefault().post(new GridItemRemoveEvent(work));
                        rating.getStyleClass().remove("user-rating");
                        Notification.show(String.format("成功取消收藏作品(RJ%s)",work.getId()), MessageType.SUCCESS,Pos.TOP_CENTER, App.mainStage);
                    }else {
                        starBtn.setVisible(true);
                        Notification.show(String.format("错误：无法取消收藏作品(RJ%s)",work.getId()), MessageType.FAILED,Pos.TOP_CENTER, App.mainStage);
                    }
                });
                Thread.startVirtualThread(task);

            });
            headPane.getChildren().add(starBtn);
            AnchorPaneUtil.setPosition(starBtn, 10.0, 10.0, null, null);
        }else {
            rating.setRating(work.getRate_average_2dp());
        }


        rating.addEventFilter(MouseEvent.MOUSE_CLICKED,event -> {
            if (Config.setting.getTOKEN() == null || Config.setting.getTOKEN().isEmpty()){
                Notification.show("使用收藏功能需要在设置中填写Token", MessageType.WARNING,2000,Pos.TOP_CENTER,App.mainStage);
                event.consume();
            }
        });


        rating.ratingProperty().addListener((observableValue, old, t1) -> {
            int i = (int) Math.ceil(t1.doubleValue());
            rating.getStyleClass().add("user-rating");
            rating.setRating(i);
            StarWorkAddTask task = new StarWorkAddTask(work.getId(), i);
            task.setOnSucceeded(event -> {
                ResponseBody<Boolean> value = task.getValue();
                if (value.isSuccess()){
                    Notification.show(String.format("成功收藏作品(RJ%s)",work.getId()), MessageType.SUCCESS,Pos.TOP_CENTER, App.mainStage);
                }else {
                    Notification.show(String.format("错误：无法收藏作品(RJ%s)",work.getId()), MessageType.FAILED,Pos.TOP_CENTER, App.mainStage);
                }
            });
            Thread.startVirtualThread(task);
        });
        BorderPane borderPane1 = new BorderPane();
        borderPane1.setLeft(rating);
        borderPane1.setRight(tagPane);


        getChildren().addAll(headPane, itemTitle, borderPane1, circleLabel);
        setSpacing(10);
        EventHandler<MouseEvent> labelHandler = mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY){
                Label source = (Label) mouseEvent.getSource();
                if (source.getAccessibleText().equals("category")) {
                    EventBusUtil.getDefault().post(new SearchEvent(CategoryType.TAG,source.getText()));
                }
                if (source.getAccessibleText().equals("va")) {
                    EventBusUtil.getDefault().post(new SearchEvent(CategoryType.VA,source.getText()));
                }
                mouseEvent.consume();
            }

        };
        FlowPane categoriesPane = new FlowPane();
        categoriesPane.setId("grid-item-categories");

        boolean isAI = false;
        if (work.getTags() != null) {
            categoriesPane.setVgap(5);
            categoriesPane.setHgap(5);

            Label label;
            for (Role category : work.getTags()) {
                label = new Label(category.getName());
                label.setAccessibleText("category");
                label.setOnMouseClicked(labelHandler);
                if (category.getName().equals("AI")){
                    isAI = true;
                    label.getStyleClass().add("tag-ai-label");
                }else {
                    label.getStyleClass().add("tag-label");
                }



                MenuItem blackBtn = new MenuItem("加入黑名单");
                blackBtn.setOnAction(event -> {
                    Config.blackList.getTagBlackList().add(category.getName());
                    EventBusUtil.getDefault().post(new BlackWorkEvent(work));
                });
                ContextMenu contextMenu = new ContextMenu(blackBtn);
                label.setContextMenu(contextMenu);
                categoriesPane.getChildren().add(label);
            }
        }
        getChildren().add(categoriesPane);

        if (isAI){
            Label label= new Label("AI");
            label.getStyleClass().add("list-item-tag-ai");
            label.setId("grid-item-ai");
            tagPane.getChildren().add(0, label);
        }



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
            if (mouseEvent.getButton() == MouseButton.PRIMARY){
                DetailUI detailUi=new DetailUI(work);
                EventBusUtil.getDefault().post(new MainDialogEvent(detailUi.getRoot()));
            }

        });


        setOnContextMenuRequested(contextMenuEvent -> {
            MenuItem copyTitleItem=new MenuItem("标题");
            copyTitleItem.setOnAction(event -> {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(work.getTitle());
                clipboard.setContent(content);
                Notification.show(String.format("%s:已经复制标题到剪切板", work.getFullId()), MessageType.SUCCESS,2000, Pos.TOP_CENTER, App.mainStage);
                event.consume();
            });
            MenuItem copyRJItem=new MenuItem("RJ");
            copyRJItem.setOnAction(event -> {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(work.getFullId());
                clipboard.setContent(content);
                Notification.show(String.format("%s:已经复制RJ到剪切板", work.getFullId()), MessageType.SUCCESS,2000, Pos.TOP_CENTER, App.mainStage);
                event.consume();
            });
            MenuItem copyCircleItem=new MenuItem("社团");
            copyCircleItem.setOnAction(event -> {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(work.getCircle().getName());
                clipboard.setContent(content);
                Notification.show(String.format("%s:已经复制团到剪切板", work.getFullId()), MessageType.SUCCESS,2000, Pos.TOP_CENTER, App.mainStage);
                event.consume();
            });

            Menu copyParentItem=new Menu("复制");
            copyParentItem.getItems().addAll(copyTitleItem,copyRJItem,copyCircleItem);





            MenuItem blacklistItem=new MenuItem("加入黑名单");
            blacklistItem.setOnAction(event -> {
                Config.blackList.getWorkBlackList().add(work.getFullId());
                EventBusUtil.getDefault().post(new BlackWorkEvent(work));
            });
            ContextMenu contextMenu=new ContextMenu(copyParentItem,blacklistItem);
            contextMenu.show(this,contextMenuEvent.getScreenX(),contextMenuEvent.getScreenY());
        });



    }
}