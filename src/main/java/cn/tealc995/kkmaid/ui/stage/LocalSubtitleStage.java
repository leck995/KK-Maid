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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @description:
 * @author: Leck
 * @create: 2023-09-05 00:49
 */
public class LocalSubtitleStage extends RoundStage {
    private static final Logger LOG = LoggerFactory.getLogger(LocalSubtitleStage.class);
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
        root.setPadding(new Insets(15));
        root.getChildren().add(center);


        setTitle("字幕查看");
        initOwner(App.mainStage);
        setWidth(App.mainStage.getWidth()*0.8);
        setHeight(App.mainStage.getHeight()*0.8);


        TitleBar titleBar=new TitleBar(this, TitleBarStyle.ALL);

        titleBar.setContent(root);
        titleBar.setTitle("字幕查看");
        setContent(titleBar);
    }


    /**
     * @description: 加载指定歌词文件内容
     * @param:	lrcFile
     * @return  void
     * @date:   2025/2/6
     */
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
                LOG.debug(value.getMsg());
                if (value.isSuccess()) {
                    List<LrcBean> list = value.getData();
                    if (list != null) {
                        //移除黑名单数据
                        list.removeIf(lrcBean -> {
                            String row = lrcBean.getRowText();
                            for (String s : Config.blackList.getTextBlackList()) {
                                if (!s.trim().isEmpty() && row.contains(s)) {
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