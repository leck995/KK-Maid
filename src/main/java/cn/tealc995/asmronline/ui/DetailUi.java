package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.App;
import cn.tealc995.asmronline.api.model.Role;
import cn.tealc995.asmronline.api.model.Work;
import cn.tealc995.asmronline.ui.component.FolderTableView;
import cn.tealc995.asmronline.util.CssLoader;
import cn.tealc995.teaFX.controls.notification.MessageType;
import cn.tealc995.teaFX.controls.notification.Notification;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 21:20
 */
public class DetailUi {
    private StackPane root;
    private DetailViewModel viewModel;

    public DetailUi(Work work) {
        viewModel=new DetailViewModel(work);
        root=new StackPane();
        BorderPane borderPane=new BorderPane();

        ImageView imageView = new ImageView();
        imageView.imageProperty().bind(viewModel.posterProperty());
        imageView.setFitHeight(210);
        imageView.setFitWidth(280);
        Rectangle rectangle=new Rectangle(280,210);
        rectangle.setArcHeight(15);
        rectangle.setArcWidth(15);
        imageView.setClip(rectangle);


        FlowPane tagsPane = new FlowPane();
        tagsPane.setHgap(8.0);
        tagsPane.setVgap(5.0);

        FlowPane actorsPane = new FlowPane();
        actorsPane.setHgap(8.0);
        actorsPane.setVgap(5.0);

        if (work.getTags() != null) {
            for (Role tag : work.getTags()) {
                Label label = new Label(tag.getName());
                label.getStyleClass().add("tag-label");
                label.setOnMouseClicked(mouseEvent -> {
                    Notification.show("世界线可能发生了变动，没有任何响应。", MessageType.SUCCESS,Pos.TOP_CENTER, App.mainStage);
                });
                tagsPane.getChildren().add(label);
            }
        }

        if (work.getVas() != null) {
            for (Role vas : work.getVas()) {
                Label label = new Label(vas.getName());
                label.getStyleClass().add("actor-label");
                label.setOnMouseClicked(mouseEvent -> {
                    Notification.show("世界线可能发生了变动，没有任何响应。", MessageType.SUCCESS,Pos.TOP_CENTER, App.mainStage);
                });
                actorsPane.getChildren().add(label);
            }
        }


        Label circleLabel = new Label();
        if (work.getCircle() != null) {
            circleLabel.setText(work.getCircle().getName());
        }else {
            circleLabel.setVisible(false);
        }

        circleLabel.getStyleClass().add("list-item-big-circle");
        circleLabel.setGraphic(new Region());
        circleLabel.setOnMouseClicked(mouseEvent -> {
            Notification.show("世界线可能发生了变动，没有任何响应。", MessageType.SUCCESS,Pos.TOP_CENTER, App.mainStage);
        });
        circleLabel.setPadding(new Insets(0,0,0,10));


        Label titleLabel=new Label();
        titleLabel.getStyleClass().add("detail-dialog-title");
        titleLabel.setText(work.getTitle());


        VBox left = new VBox(imageView,circleLabel, tagsPane, actorsPane);
        left.getStyleClass().add("detail-dialog-left");
        left.setAlignment(Pos.TOP_LEFT);
        left.setSpacing(15);
        left.setPrefWidth(290);
        ScrollPane leftScrollPane = new ScrollPane(left);
        leftScrollPane.setPrefWidth(300);

        FolderTableView folderTableView = new FolderTableView(work,viewModel.getTracks());


        borderPane.setTop(titleLabel);
        borderPane.setLeft(leftScrollPane);
        borderPane.setCenter(folderTableView);
        borderPane.getStyleClass().add("detail-dialog");
        root.getChildren().add(borderPane);
        root.getStylesheets().add(CssLoader.getCss(CssLoader.detail));

    }


    public StackPane getRoot() {
        return root;
    }
}