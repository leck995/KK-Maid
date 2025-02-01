package cn.tealc995.kkmaid.ui;

import atlantafx.base.controls.Card;
import atlantafx.base.theme.Styles;
import cn.tealc995.kkmaid.App;
import cn.tealc995.kkmaid.Config;
import cn.tealc995.kikoreu.model.Response;
import cn.tealc995.kikoreu.model.Track;
import cn.tealc995.kikoreu.model.Work;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainDialogEvent;
import cn.tealc995.kkmaid.ui.cell.DownloadTreeItem;
import cn.tealc995.kkmaid.util.CssLoader;
import cn.tealc995.teaFX.controls.notification.MessageType;
import cn.tealc995.teaFX.controls.notification.Notification;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-09-13 04:01
 */
public class DownloadUI {
    private static final String RESULT_TITLE_TIP="结果";
    private static final String RESULT_CONTENT_1="成功添加%d个下载任务至Aria2";
    private static final String RESULT_CONTENT_2="成功添加%d个下载任务至Aria2,失败添加%d个下载任务";
    private StackPane root;
    private DownloadViewModel viewModel;
    private Work work;

    public DownloadUI(Work work,Track rootTrack) {
        this.work=work;
        viewModel=new DownloadViewModel(work,rootTrack);
        root=new StackPane();
        root.setPrefSize(900,600);

        Label title=new Label("下载: RJ"+rootTrack.getTitle());
        title.getStyleClass().add(Styles.TITLE_3);

        Label dir=new Label("保存至:"+Config.downloadDir.get());
        dir.getStyleClass().add(Styles.TEXT_MUTED);

        StackPane top=new StackPane(title,dir);
        StackPane.setAlignment(title,Pos.CENTER_LEFT);
        StackPane.setAlignment(dir,Pos.CENTER_RIGHT);


        TreeView<Track> treeView=new TreeView<>();
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        DownloadTreeItem rootTreeItem=new DownloadTreeItem(rootTrack);
        treeView.setRoot(rootTreeItem);






        treeView.setCellFactory(new Callback<TreeView<Track>, TreeCell<Track>>() {
            @Override
            public TreeCell<Track> call(TreeView<Track> trackTreeView) {
                CheckBoxTreeCell<Track> treeCell=new CheckBoxTreeCell<>(){
                    @Override
                    public void updateItem(Track track, boolean b) {
                        super.updateItem(track, b);
                        if (!b && track != null){
                            setDisable(false);
                            setText(track.getTitle());


                        }else {
                            setText("");
                            setDisable(true);
                        }
                    }
                };


                return treeCell;
            }
        });



        Set<TreeItem<Track>> selected = new HashSet<>();

        // listen for selection change
        rootTreeItem.addEventHandler(CheckBoxTreeItem.checkBoxSelectionChangedEvent(), (CheckBoxTreeItem.TreeModificationEvent<Track> evt) -> {
            CheckBoxTreeItem<Track> item = evt.getTreeItem();

            if (evt.wasIndeterminateChanged()) {
                if (item.isIndeterminate()) {
                    selected.remove(item);
                } else if (item.isSelected()) {
                    selected.add(item);
                }
            } else if (evt.wasSelectionChanged()) {
                if (item.isSelected()) {
                    selected.add(item);
                } else {
                    selected.remove(item);
                }
            }
        });

        // listen for subtree add/remove
        rootTreeItem.addEventHandler(TreeItem.childrenModificationEvent(), (TreeItem.TreeModificationEvent<Track> evt) -> {
            if (evt.wasAdded()) {
                for (TreeItem<Track> added : evt.getAddedChildren()) {
                    addSubtree(selected, (CheckBoxTreeItem<Track>) added);
                }
            }
        });

        Button downloadBtn=new Button("下载");
        downloadBtn.setOnAction(event -> {
           /* selected.stream().map(TreeItem::getValue).forEach(s -> System.out.println(s.getTitle()));*/
            if (Config.downloadDir.get() == null || Config.aria2Host ==null || Config.ariaRPCKey ==null){
                Notification.show("先在设置中配置下载目录和Aria2", MessageType.WARNING,2000, Pos.TOP_CENTER, App.mainStage);
            }else {
                Set<Track> set=new HashSet<>();
                for (TreeItem<Track> trackTreeItem : selected) {
                    DownloadTreeItem downloadTreeItem= (DownloadTreeItem) trackTreeItem;
                    Track value = downloadTreeItem.getValue();
                    if (!value.isFolder()){
                        value.setPath(downloadTreeItem.getPath());
                        set.add(value);
                    }

                }
                viewModel.download(work,set);
                System.out.println(String.format("成功创建下载任务：%d，失败：%d",viewModel.getSuccessList().size(),viewModel.getFailList().size()));


                EventBusUtil.getDefault().post(new MainDialogEvent(createResultPane()));
            }


           // print(rootTreeItem);
        });
        HBox bottomPane=new HBox(downloadBtn);
        bottomPane.setAlignment(Pos.CENTER_RIGHT);

/*        HBox bottomPane=new HBox(downloadBtn);

        BorderPane parent=new BorderPane();
        parent.setTop(title);
        parent.setCenter(treeView);
        parent.setBottom(bottomPane);*/

        Card card=new Card();
        card.setHeader(top);
        card.setBody(treeView);
        card.setFooter(bottomPane);
        root.getStylesheets().add(CssLoader.getCss(CssLoader.download));
        root.getChildren().add(card);



    }
    private static <T> void addSubtree(Collection<TreeItem<T>> collection, CheckBoxTreeItem<T> item) {
        if (item.isSelected()) {
            collection.add(item);
        } else if (!item.isIndeterminate() && !item.isIndependent()) {
            return;
        }
        for (TreeItem<T> child : item.getChildren()) {
            addSubtree(collection, (CheckBoxTreeItem<T>) child);
        }
    }

    private static <T> void print(CheckBoxTreeItem<T> item) {
        if (item.isSelected()) {
            System.out.println(item.getValue());
        } else if (!item.isIndeterminate() && !item.isIndependent()) {
            return;
        }
        for (TreeItem<T> child : item.getChildren()) {
            print((CheckBoxTreeItem<T>) child);
        }
    }


    private JFXDialogLayout createResultPane(){
        JFXDialogLayout layout=new JFXDialogLayout();
        Label title=new Label(RESULT_TITLE_TIP);
        title.getStyleClass().add(Styles.TITLE_3);
        layout.setHeading(title);


        Label success=new Label();
        success.getStyleClass().add(Styles.TITLE_4);
        success.setWrapText(true);
        List<Track> failList = viewModel.getFailList();

        if (failList.size() == 0){
            success.setText(String.format(RESULT_CONTENT_1,viewModel.getSuccessList().size()));
            success.setPrefWidth(350);
            layout.setPrefSize(400,120);
            layout.setBody(success);
        }else {
            success.setText(String.format(RESULT_CONTENT_2,viewModel.getSuccessList().size(),viewModel.getFailList().size()));
            StringBuilder sb=new StringBuilder();
            List<Response> responses = viewModel.getResponses();
            for (int i = 0; i < failList.size(); i++) {
                sb.append(String.format("%s 添加失败，错误:%d,说明:%s\n"
                        ,failList.get(i).getTitle()
                        ,responses.get(i).getCode()
                        ,responses.get(i).getMessage()));

            }

            TextArea textArea=new TextArea();
            textArea.setText(sb.toString());
            textArea.setEditable(false);
            layout.setPrefSize(800,550);
            textArea.setPrefHeight(400);
            VBox center=new VBox(success,textArea);
            center.setSpacing(10);
            layout.setBody(center);
        }


        Button closeBtn=new Button("关闭");
        closeBtn.setOnAction(event -> EventBusUtil.getDefault().post(new MainDialogEvent(null)));

        layout.setActions(closeBtn);

        layout.getStylesheets().add(CssLoader.getCss(CssLoader.download));
        layout.getStyleClass().add("background");





        layout.requestFocus();
        return layout;


    }




    public StackPane getRoot() {
        return root;
    }
}