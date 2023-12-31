package cn.tealc995.asmronline;

import atlantafx.base.theme.PrimerDark;
import cn.tealc995.asmronline.player.MediaPlayerUtil;
import cn.tealc995.asmronline.ui.MainUI;
import cn.tealc995.asmronline.util.CssLoader;

import cn.tealc995.teaFX.controls.TitleBar;
import cn.tealc995.teaFX.enums.TitleBarStyle;
import cn.tealc995.teaFX.stage.RoundStage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.greenrobot.eventbus.Logger;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class App extends Application {
    public static RoundStage mainStage;
    @Override
    public void start(Stage stage) throws IOException {
        Timer timer=new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.gc();
            }
        },60000,60000);
        stage.close();
        mainStage=new RoundStage();

        if (Config.proxyModel.get()){
            System.setProperty("https.proxyHost", Config.proxyHost.get());
            System.setProperty("https.proxyPort", Config.proxyPort.get());
        }


        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        MainUI mainUI=new MainUI();




        mainStage.setOnHidden(windowEvent -> {
            System.out.println("退出程序");
            Platform.exit();
        });

        mainStage.setContent(mainUI.getRoot());
        mainStage.getScene().getStylesheets().addAll(CssLoader.getCss(CssLoader.baseUI),CssLoader.getCss(CssLoader.main));
        mainStage.getScene().setFill(Color.TRANSPARENT);
        mainStage.setWidth(Config.stageWidth.get());
        mainStage.setHeight(Config.stageHeight.get());
        mainStage.setFullScreenExitHint("");
        mainStage.initStyle(StageStyle.TRANSPARENT);

        mainStage.setTitle("KK Maid");
        mainStage.getIcons().add(new Image(getClass().getResource("/cn/tealc995/asmronline/image/icon.png").toExternalForm()));
        mainStage.show();
    }


    @Override
    public void stop() throws Exception {
        super.stop();
        MediaPlayerUtil.mediaPlayer().dispose();
        Config.saveProperties();
    }

    public static void main(String[] args) {
        launch();
    }
}