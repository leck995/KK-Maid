package cn.tealc995.kkmaid;

import atlantafx.base.theme.PrimerDark;
import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kkmaid.player.MediaPlayerUtil;
import cn.tealc995.kkmaid.ui.MainUI;
import cn.tealc995.kkmaid.util.CssLoader;

import cn.tealc995.teaFX.stage.RoundStage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

        if (Config.setting.isProxyModel()){
            System.setProperty("https.proxyHost", Config.setting.getProxyHost());
            System.setProperty("https.proxyPort", Config.setting.getProxyPort());
        }

        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        MainUI mainUI=new MainUI();

        mainStage.setOnHidden(windowEvent -> {
            Platform.exit();
        });

        mainStage.setContent(mainUI.getRoot());
        mainStage.getScene().getStylesheets().addAll(CssLoader.getCss(CssLoader.baseUI),CssLoader.getCss(CssLoader.main));
        mainStage.getScene().setFill(Color.TRANSPARENT);
        mainStage.setWidth(Config.setting.getStageWidth());
        mainStage.setHeight(Config.setting.getStageHeight());
        mainStage.setFullScreenExitHint("");
        mainStage.initStyle(StageStyle.TRANSPARENT);

        mainStage.setTitle("KK Maid");
        mainStage.getIcons().add(new Image(getClass().getResource("/cn/tealc995/kkmaid/image/icon.png").toExternalForm()));
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