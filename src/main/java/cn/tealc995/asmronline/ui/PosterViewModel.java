package cn.tealc995.asmronline.ui;

import cn.tealc995.asmronline.api.model.Track;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
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
        image=new SimpleObjectProperty<>(new Image(filePaths.get(index).getMediaStreamUrl(),true));
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





    public void update(List<Track> filePaths, int index){
        this.filePaths=filePaths;
        this.index=new SimpleIntegerProperty(index);
        title.set(filePaths.get(index).getTitle());
        image.set(new Image(filePaths.get(index).getMediaStreamUrl(),true));
    }

    public void download(File file){
        String urlPath=filePaths.get(index.get()).getMediaStreamUrl();
        try {
            URL url=new URL(urlPath);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();

            if (!file.exists()){
                file.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] temp=new byte[1024];
            int index;

            while ((index = inputStream.read(temp))!= -1.){
                fileOutputStream.write(temp,0,index);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();


        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public String getImageName(){
        return filePaths.get(index.get()).getTitle();
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