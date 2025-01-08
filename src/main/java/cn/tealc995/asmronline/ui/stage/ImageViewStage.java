package cn.tealc995.asmronline.ui.stage;

import cn.tealc995.asmronline.App;
import cn.tealc995.asmronline.api.model.Track;
import cn.tealc995.asmronline.ui.PosterUI;
import cn.tealc995.asmronline.util.CssLoader;
import cn.tealc995.teaFX.controls.TitleBar;
import cn.tealc995.teaFX.enums.TitleBarStyle;
import cn.tealc995.teaFX.stage.RoundStage;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-06 01:05
 */
public class ImageViewStage extends RoundStage {
    private static ImageViewStage stage;
    private PosterUI posterUI;

    public static ImageViewStage getInstance(List<Track> filePaths, int index){
        if (stage == null){
            stage=new ImageViewStage(filePaths,index);
        }
        stage.posterUI.update(filePaths,index);
        return stage;
    }
    private ImageViewStage(List<Track> filepaths,int index) {
        setWidth(App.mainStage.getWidth()*0.8);
        setHeight(App.mainStage.getHeight()*0.8);

        posterUI = new PosterUI(filepaths,index);
        TitleBar titleBar=new TitleBar(this,TitleBarStyle.ALL);

        titleBar.setContent(posterUI.getRoot());
        titleBar.setTitle("图片查看");
        setContent(titleBar);
        setTitle("图片查看");
        setOnHidden(windowEvent -> {
            System.out.println("关闭");
            posterUI.dispose();
            System.gc();
        });

    }
}