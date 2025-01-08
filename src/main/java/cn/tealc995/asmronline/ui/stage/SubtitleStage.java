package cn.tealc995.asmronline.ui.stage;

import cn.tealc995.asmronline.App;
import cn.tealc995.asmronline.util.CssLoader;
import cn.tealc995.teaFX.controls.TitleBar;
import cn.tealc995.teaFX.enums.TitleBarStyle;
import cn.tealc995.teaFX.stage.RoundStage;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-09-05 00:49
 */
public class SubtitleStage extends RoundStage {
    public SubtitleStage(String row) {
        row=row.replaceAll("\\\\n","\n");
        StackPane root=new StackPane();
        TextArea textArea=new TextArea(row);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        root.getChildren().add(textArea);


        setTitle("文本查看");
        initOwner(App.mainStage);
        setWidth(App.mainStage.getWidth()*0.8);
        setHeight(App.mainStage.getHeight()*0.8);


        TitleBar titleBar=new TitleBar(this, TitleBarStyle.ALL);

        titleBar.setContent(root);
        titleBar.setTitle("文本查看");
        setContent(titleBar);
    }
}