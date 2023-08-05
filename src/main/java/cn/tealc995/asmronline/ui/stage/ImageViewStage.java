package cn.tealc995.asmronline.ui.stage;

import cn.tealc995.asmronline.api.model.Track;
import cn.tealc995.asmronline.ui.PosterUI;
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
public class ImageViewStage extends Stage {
    private static ImageViewStage stage;
    private final PosterUI posterUI;

    public static ImageViewStage getInstance(List<Track> filePaths, int index){
        if (stage == null){
            stage=new ImageViewStage(filePaths,index);
        }
        return stage;
    }
    private ImageViewStage(List<Track> filepaths,int index) {
        //initStyle(StageStyle.TRANSPARENT);

        setWidth(1920);
        setHeight(1080);
        posterUI = new PosterUI(filepaths,index);
        Scene scene=new Scene(posterUI.getRoot());
        setScene(scene);

    }
}