package cn.tealc995.kkmaid.ui.component;

import atlantafx.base.controls.Card;
import atlantafx.base.theme.Styles;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;

/**
 * @description:
 * @author: Leck
 * @create: 2023-08-08 06:48
 */
public class Dialog {

    public static Card Warning(String title, String info, EventHandler<ActionEvent> OKEvent, EventHandler<ActionEvent> cancelEvent){
        Card card=new Card();
        card.setPrefWidth(300);
        Label titleLabel=new Label(title,new FontIcon(Material2MZ.WARNING));
        titleLabel.getStyleClass().add(Styles.TITLE_4);
        card.setHeader(titleLabel);

        Label infoLabel=new Label(info);
        infoLabel.getStyleClass().add(Styles.TEXT_MUTED);
        card.setBody(infoLabel);

        Button okBtn=new Button("确定");
        okBtn.setOnAction(OKEvent);
        okBtn.getStyleClass().addAll(Styles.SMALL,Styles.DANGER);
        Button cancelBtn=new Button("取消");
        cancelBtn.setOnAction(cancelEvent);
        cancelBtn.getStyleClass().add(Styles.SMALL);
        HBox footPane=new HBox(okBtn,cancelBtn);
        footPane.setAlignment(Pos.CENTER_RIGHT);
        footPane.setSpacing(10.0);

        card.setFooter(footPane);
        return card;
    }
}