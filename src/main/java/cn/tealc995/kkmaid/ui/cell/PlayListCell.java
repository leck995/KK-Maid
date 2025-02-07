package cn.tealc995.kkmaid.ui.cell;

import atlantafx.base.theme.Styles;
import cn.tealc995.kikoreu.model.playList.PlayList;
import cn.tealc995.kkmaid.event.*;
import cn.tealc995.kkmaid.ui.MainPlayListUI;
import cn.tealc995.kkmaid.util.FXResourcesLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-12 21:53
 */
public class PlayListCell extends VBox {
    public PlayListCell(PlayList playList){
        ImageView itemAlbum = new ImageView();
        if (playList.getMainCoverUrl().startsWith("/")){
            itemAlbum.setImage(new Image(FXResourcesLoader.load("/cn/tealc995/kkmaid/image/cover-main.jpg"),240,180,true,true,true));
        }else {
            itemAlbum.setImage(new Image(playList.getMainCoverUrl(),240,180,true,true,true));
        }

        itemAlbum.setSmooth(true);
        itemAlbum.setPreserveRatio(true);
        itemAlbum.setId("grid-item-cover");
        itemAlbum.getStyleClass().add("list-item-image");
        Rectangle rectangle=new Rectangle(240,180);
        rectangle.setArcHeight(20);
        rectangle.setArcWidth(20);
        itemAlbum.setClip(rectangle);

        Label itemTitle = new Label();
        itemTitle.setId("grid-item-title");
        itemTitle.getStyleClass().add(Styles.TITLE_3);
        itemTitle.setWrapText(true);
        itemTitle.setText(playList.getName());

        Label userLabel=new Label();
        userLabel.getStyleClass().add(Styles.TEXT_MUTED);
        userLabel.setText(String.format("By %s",playList.getUsername()));

        Label countLabel=new Label();
        countLabel.getStyleClass().add(Styles.TEXT_MUTED);
        countLabel.setText(String.format("%d works",playList.getWorksCount()));


        setSpacing(6);
        getChildren().addAll(itemAlbum,itemTitle,userLabel,countLabel);
        setOnMouseClicked(mouseEvent -> {
            if ( mouseEvent.getButton()==MouseButton.PRIMARY){
                MainPlayListUI mainPlayListUI=new MainPlayListUI(playList);
                EventBusUtil.getDefault().post(new MainCenterEvent(mainPlayListUI.getRoot(),true,true));
            }
        });

        MenuItem deleteItem=new MenuItem("删除");
        deleteItem.setDisable(playList.canDelete());

        deleteItem.setOnAction(event -> {
            EventBusUtil.getDefault().post(new PlayListRemoveEvent(playList));
        });

        MenuItem alterItem=new MenuItem("修改");
        alterItem.setDisable(playList.canDelete());
        alterItem.setOnAction(event -> {
            EventBusUtil.getDefault().post(new PlayListAlterEvent(playList));
        });
        ContextMenu contextMenu=new ContextMenu(alterItem,deleteItem);

        setOnContextMenuRequested(contextMenuEvent -> {
            contextMenu.show(this,contextMenuEvent.getScreenX(),contextMenuEvent.getScreenY());
        });


    }




}