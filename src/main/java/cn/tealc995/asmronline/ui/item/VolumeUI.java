package cn.tealc995.asmronline.ui.item;

import cn.tealc995.asmronline.player.LcMediaPlayer;
import cn.tealc995.asmronline.util.CssLoader;
import com.jfoenix.controls.JFXSlider;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-20 07:01
 */
public class VolumeUI {
    private StackPane root;

    public VolumeUI() {
        root=new StackPane();
        JFXSlider volumeSlider=new JFXSlider();
        volumeSlider.setIndicatorPosition(JFXSlider.IndicatorPosition.RIGHT);
        volumeSlider.valueProperty().bindBidirectional(LcMediaPlayer.getInstance().volumeProperty());
        volumeSlider.setMin(0);
        volumeSlider.setMax(1);
        volumeSlider.getStyleClass().add("popup-volume-slider");
        volumeSlider.setOrientation(Orientation.VERTICAL);
        //设置播放条显示格式
        volumeSlider.setValueFactory(slider -> volumeSlider.valueProperty().multiply(100).asString("%.0f"));
        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setMaxWidth(40);

        ToggleButton muteBtnInChild=new ToggleButton();
        muteBtnInChild.selectedProperty().bindBidirectional(LcMediaPlayer.getInstance().muteProperty());
        muteBtnInChild.getStyleClass().addAll("lc-svg-toggle-btn","popup-volume-mute-btn");
        muteBtnInChild.setGraphic(new Region());
        VBox volumePane=new VBox(muteBtnInChild,volumeSlider);
        volumePane.setPrefWidth(50);
        volumePane.setPrefHeight(150);
        volumePane.setSpacing(5);
        volumePane.setPadding(new Insets(5.0,5.0,5.0,5.0));
        volumePane.setAlignment(Pos.CENTER);
        volumePane.getStylesheets().add(CssLoader.getCss(CssLoader.volume_popup));
        volumePane.getStyleClass().addAll("popup-volume-sound-popup");
        root.getChildren().add(volumePane);
    }

    public StackPane getRoot() {
        return root;
    }
}