package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.api.model.Track;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-06 00:43
 */
public class PosterViewModel {
    private SimpleObjectProperty<Image> image;
    private List<Track> filePaths;
    private SimpleIntegerProperty index;
    private SimpleStringProperty title;

    public PosterViewModel(List<Track> filePaths, int index) {
        this.filePaths=filePaths;
        this.index=new SimpleIntegerProperty(index);
        title=new SimpleStringProperty(filePaths.get(index).getTitle());
        image=new SimpleObjectProperty<>(new Image(filePaths.get(index).getMediaStreamUrl(),1920,1080,true,true,true));
    }



    public void next(){
        if (index.get() < filePaths.size()-1){
            index.set(index.get()+1);
        }else {
            index.set(0);
        }
        title.set(filePaths.get(index.get()).getTitle());
        image.set(new Image(filePaths.get(index.get()).getMediaStreamUrl(),1920,1080,true,true,true));
    }

    public void pre(){
        if (index.get() == 0){
            index.set(filePaths.size()-1);
        }else {
            index.set(index.get()-1);
        }
        title.set(filePaths.get(index.get()).getTitle());
        image.set(new Image(filePaths.get(index.get()).getMediaStreamUrl(),1920,1080,true,true,true));
    }

    public Image getImage() {
        return image.get();
    }

    public SimpleObjectProperty<Image> imageProperty() {
        return image;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }
}