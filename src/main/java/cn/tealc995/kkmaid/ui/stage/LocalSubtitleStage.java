package cn.tealc995.kkmaid.ui.stage;

import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kkmaid.App;
import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kkmaid.model.lrc.LrcBean;
import cn.tealc995.kkmaid.model.lrc.LrcFile;
import cn.tealc995.kkmaid.model.lrc.LrcType;
import cn.tealc995.kkmaid.service.subtitle.beans.SubtitleBeansBaseTask;
import cn.tealc995.kkmaid.service.subtitle.beans.SubtitleBeansByFolderTask;
import cn.tealc995.kkmaid.service.subtitle.beans.SubtitleBeansByNetTask;
import cn.tealc995.kkmaid.service.subtitle.beans.SubtitleBeansByZipTask;
import cn.tealc995.kkmaid.ui.cell.LrcListCell;
import cn.tealc995.kkmaid.util.LrcImportUtil;
import cn.tealc995.teaFX.controls.TitleBar;
import cn.tealc995.teaFX.enums.TitleBarStyle;
import cn.tealc995.teaFX.stage.RoundStage;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-09-05 00:49
 */
public class LocalSubtitleStage extends RoundStage {

    private ListView<LrcBean> lrcListView;
    private ObservableList<LrcBean> lrcBeans;

    private ListView<LrcFile> lrcFileView;
    private ObservableList<LrcFile> lrcFiles;

    public LocalSubtitleStage(List<LrcFile> list,int index) {
        lrcFiles = FXCollections.observableArrayList(list);
        lrcFileView = new ListView<>(lrcFiles);
        lrcFileView.setPrefWidth(300.0);
        lrcFileView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue != null) {
               load(newValue);

           }
        });
        lrcFileView.getSelectionModel().select(index);


        lrcBeans = FXCollections.observableArrayList();
        lrcListView = new ListView<>();
        lrcListView.setItems(lrcBeans);
        lrcListView.setCellFactory(listView -> new LrcListCell());


        HBox center = new HBox(10.0,lrcListView,lrcFileView);
        HBox.setHgrow(lrcListView, Priority.ALWAYS);

        StackPane root=new StackPane();
        root.getChildren().add(center);


        setTitle("字幕查看");
        initOwner(App.mainStage);
        setWidth(App.mainStage.getWidth()*0.8);
        setHeight(App.mainStage.getHeight()*0.8);


        TitleBar titleBar=new TitleBar(this, TitleBarStyle.ALL);

        titleBar.setContent(root);
        titleBar.setTitle("字幕查看");
        setContent(titleBar);


        load(list.get(index));
    }

    private void load(LrcFile lrcFile) {
        SubtitleBeansBaseTask task = null;
        if (lrcFile.getType() == LrcType.NET) {
            task = new SubtitleBeansByNetTask(lrcFile.getPath());
        } else if (lrcFile.getType() == LrcType.FOLDER) {
            task = new SubtitleBeansByFolderTask(lrcFile.getPath());
        } else if (lrcFile.getType() == LrcType.ZIP) {
            task = new SubtitleBeansByZipTask(lrcFile);
        }
        if (task != null) {
            task.setOnSucceeded(workerStateEvent -> {
                SubtitleBeansBaseTask source = (SubtitleBeansBaseTask) workerStateEvent.getSource();
                ResponseBody<List<LrcBean>> value = source.getValue();
                if (value.isSuccess()) {
                    List<LrcBean> list = value.getData();
                    if (list != null) {
                        //移除黑名单数据
                        list.removeIf(lrcBean -> {
                            String row = lrcBean.getRowText();
                            for (String s : Config.blackList.getTextBlackList()) {
                                if (row.contains(s)) {
                                    return true;
                                }
                            }
                            return false;
                        });
                        //加载到lrcBeans中
                        lrcBeans.setAll(list);
                        lrcListView.scrollTo(0);
                    }else {
                        lrcBeans.clear();
                    }
                }
            });
            Thread.startVirtualThread(task);
        }
    }
}