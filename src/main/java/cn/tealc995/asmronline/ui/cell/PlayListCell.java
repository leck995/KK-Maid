package cn.tealc995.asmronline.ui.cell;

import atlantafx.base.theme.Styles;
import cn.tealc995.asmronline.api.model.playList.PlayList;
import cn.tealc995.asmronline.event.*;
import cn.tealc995.asmronline.ui.MainPlayListUI;
import cn.tealc995.asmronline.util.FXResourcesLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 21:53
 */
public class PlayListCell extends VBox {
    public PlayListCell(PlayList playList){
        ImageView itemAlbum = new ImageView();
        if (playList.getMainCoverUrl().startsWith("/")){
            itemAlbum.setImage(new Image(FXResourcesLoader.load("/cn/tealc995/asmronline/image/cover-main.jpg"),240,180,true,true,true));
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

            MainPlayListUI mainPlayListUI=new MainPlayListUI(playList);
            EventBusUtil.getDefault().post(new MainCenterEvent(mainPlayListUI.getRoot(),true));
        });

    }
}