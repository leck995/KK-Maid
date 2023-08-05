package cn.tealc995.asmronline;

import atlantafx.base.theme.PrimerDark;
import cn.tealc995.asmronline.ui.MainUI;
import cn.tealc995.asmronline.util.CssLoader;
import cn.tealc995.teaFX.controls.SceneBar;
import cn.tealc995.teaFX.controls.TitleBar;
import cn.tealc995.teaFX.enums.TitleBarStyle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.greenrobot.eventbus.Logger;

import java.io.IOException;

public class App extends Application {
    public static Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException {

        mainStage=stage;

        if (Config.proxyModel.get()){
            System.setProperty("https.proxyHost", Config.proxyHost.get());
            System.setProperty("https.proxyPort", Config.proxyPort.get());
        }


        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        MainUI mainUI=new MainUI();

        //TitleBar titleBar=new TitleBar(stage,TitleBarStyle.ALL,true);
        Scene scene = new Scene(mainUI.getRoot());
        scene.getStylesheets().addAll(CssLoader.getCss(CssLoader.baseUI),CssLoader.getCss(CssLoader.main));
        scene.setFill(Color.TRANSPARENT);

        stage.setWidth(Config.stageWidth.get());
        stage.setHeight(Config.stageHeight.get());
        stage.setFullScreenExitHint("");
        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setTitle("KK Maid");
        stage.getIcons().add(new Image(getClass().getResource("/cn/tealc995/asmronline/image/icon.png").toExternalForm()));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}