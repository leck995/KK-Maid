package cn.tealc995.kkmaid.ui.cell;


import cn.tealc995.kkmaid.model.lrc.LrcBean;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

/**
 * @description: 播放界面歌词显示的Cell
 * @author: Leck
 * @create: 2023-03-01 01:43
 */
public class LrcListCell extends ListCell<LrcBean> {
    @Override
    protected void updateItem(LrcBean lrcBean, boolean b) {
        super.updateItem(lrcBean, b);
        if (!b){
            VBox box=new VBox();
            box.setPadding(new Insets(10,0,10,0));
            box.setSpacing(5);
            //box.setAlignment(SettingProperties.detailLrcAlignment.get() ? Pos.CENTER : Pos.CENTER_LEFT);
            Label row=new Label(lrcBean.getRowText());
            row.setWrapText(true);
            box.getChildren().add(row);

            setGraphic(box);

        }else{
           setGraphic(null);
        }
    }

}