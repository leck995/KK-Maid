package cn.tealc995.kkmaid.ui;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.User;
import cn.tealc995.kkmaid.App;
import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kkmaid.event.*;
import cn.tealc995.kkmaid.service.api.login.CheckLoginTask;
import cn.tealc995.kkmaid.util.CssLoader;
import cn.tealc995.kkmaid.util.FXResourcesLoader;
import cn.tealc995.teaFX.controls.TitleBar;
import cn.tealc995.teaFX.controls.notification.MessageType;
import cn.tealc995.teaFX.controls.notification.Notification;
import cn.tealc995.teaFX.enums.TitleBarStyle;
import com.jfoenix.controls.JFXDialog;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-12 21:04
 */
public class MainUI {
    private MainViewModel viewModel;
    private TitleBar root;
    private BorderPane parent;
    private JFXDialog dialog;
    private List<JFXDialog> dialogs;

    private StackPane center;
    private StackPane content;

    public MainUI() {
        EventBusUtil.getDefault().register(this);
        viewModel = new MainViewModel();
        root = new TitleBar(App.mainStage, TitleBarStyle.ALL);
        ImageView iv = new ImageView(new Image(FXResourcesLoader.load("/cn/tealc995/kkmaid/image/title.png"), 100, 100, true, true));
        iv.setSmooth(true);
        iv.setTranslateX(20);
        root.setIcon(iv);


        parent = new BorderPane();
        parent.setLeft(createSideBar());
        MainGridUI gridUI = new MainGridUI();
        center = new StackPane(gridUI.getRoot());

        parent.setCenter(center);


        dialog = createDialog();
        dialog.setOnDialogClosed(jfxDialogEvent -> dialog.setContent(null));
        dialogs = new ArrayList<>();
        dialogs.add(dialog);


        Button settingBtn = new Button();
        settingBtn.setGraphic(new Region());
        settingBtn.getStyleClass().add("setting-btn");
        settingBtn.setOnAction(action -> {

            SettingUI settingUI = new SettingUI();
            addCenter(settingUI.getRoot());
        });
        root.getTitleBarRightPane().getChildren().add(0, settingBtn);

        //搜索框
        TextField searchField = new TextField();
        searchField.setFocusTraversable(false);
        Button searchBtn = new Button();
        searchBtn.setGraphic(new Region());
        HBox searchPane = new HBox(searchField, searchBtn);
        searchPane.getStyleClass().add("search-pane");
        searchField.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.isEmpty()) {
                search(t1);
            }
        });

        searchField.setOnAction(event -> search(searchField.getText()));
        searchBtn.setOnAction(event -> search(searchField.getText()));
        root.getTitleBarRightPane().getChildren().add(0, searchPane);


        root.setOnMouseClicked(mouseEvent -> root.requestFocus());
        root.getStylesheets().add(CssLoader.getCss(CssLoader.main));
        //root.getStyleClass().add("background");
        content = new StackPane(parent);
        root.setContent(content);

        viewModel.checkLogin();

    }


    private Pane createSideBar() {
        ToggleGroup group = new ToggleGroup();
        ToggleButton allBtn = new ToggleButton("全部");
        allBtn.setSelected(true);
        allBtn.getStyleClass().addAll("function-button", "all-btn");
        allBtn.setGraphic(new Region());
        allBtn.setToggleGroup(group);
        allBtn.setOnAction(actionEvent -> {
            allBtn.setSelected(true);
            backBaseCenter();
            EventBusUtil.getDefault().post(new SearchEvent(CategoryType.ALL, ""));
        });

        ToggleButton starBtn = new ToggleButton("收藏");
        starBtn.getStyleClass().addAll("function-button", "star-btn");
        starBtn.setGraphic(new Region());
        starBtn.setToggleGroup(group);
        starBtn.setOnAction(actionEvent -> {
            if (Config.setting.getTOKEN() != null && !Config.setting.getTOKEN().isEmpty()) {
                if (!starBtn.isSelected()) {
                    starBtn.setSelected(true);
                } else {
                    backBaseCenter();
                    EventBusUtil.getDefault().post(new SearchEvent(CategoryType.STAR, ""));
                }
            } else {
                starBtn.setSelected(false);
                Notification.show("使用该功能需要在设置中填写Token", MessageType.WARNING, 2000, Pos.TOP_CENTER, App.mainStage);
            }
        });


        ToggleButton playListBtn = new ToggleButton("歌单");
        playListBtn.getStyleClass().addAll("function-button", "play-list-btn");
        playListBtn.setGraphic(new Region());
        playListBtn.setToggleGroup(group);
        playListBtn.setOnAction(actionEvent -> {
            if (Config.setting.getTOKEN() != null && !Config.setting.getTOKEN().isEmpty()) {
                backBaseCenter();
                PlayListUI playListUI = new PlayListUI();
                addCenter(playListUI.getRoot());
                playListBtn.setSelected(true);
            } else {
                playListBtn.setSelected(false);
                Notification.show("使用该功能需要在设置中填写Token", MessageType.WARNING, 2000, Pos.TOP_CENTER, App.mainStage);
            }
        });


        ToggleButton circleBtn = new ToggleButton("社团");
        circleBtn.getStyleClass().addAll("function-button", "circle-btn");
        circleBtn.setGraphic(new Region());
        circleBtn.setToggleGroup(group);
        circleBtn.setOnAction(actionEvent -> {
            if (!circleBtn.isSelected()) {
                circleBtn.setSelected(true);
            }
            getCategory(CategoryType.CIRCLE);

        });


        ToggleButton tagBtn = new ToggleButton("标签");


        tagBtn.getStyleClass().addAll("function-button", "tag-btn");
        tagBtn.setGraphic(new Region());
        tagBtn.setToggleGroup(group);
        tagBtn.setOnAction(actionEvent -> {
            if (!tagBtn.isSelected()) {
                tagBtn.setSelected(true);
            }
            getCategory(CategoryType.TAG);
        });


        ToggleButton vaBtn = new ToggleButton("声优");
        vaBtn.getStyleClass().addAll("function-button", "va-btn");
        vaBtn.setGraphic(new Region());
        vaBtn.setToggleGroup(group);
        vaBtn.setOnAction(actionEvent -> {
            if (!vaBtn.isSelected()) {
                vaBtn.setSelected(true);
            }
            getCategory(CategoryType.VA);

        });


        VBox vBox = new VBox(allBtn, starBtn, playListBtn, circleBtn, tagBtn, vaBtn);
        vBox.setFillWidth(true);

        vBox.setPrefWidth(150);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(20, 10, 10, 10));
        vBox.setSpacing(6);


        SimplePlayerUI simplePlayerUI = new SimplePlayerUI();


        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("side-bar");
        borderPane.setTop(vBox);
        borderPane.setBottom(simplePlayerUI.getRoot());
        return borderPane;
    }


    private void search(String key) {
        if (key != null) {
            backBaseCenter();
            if (key.equals(""))
                EventBusUtil.getDefault().post(new SearchEvent(CategoryType.ALL, key));
            else
                EventBusUtil.getDefault().post(new SearchEvent(CategoryType.SEARCH, key));
        }
    }


    @Subscribe
    public void showDialog(MainDialogEvent event) {
        if (event.getPane() != null) {
            if (dialog.isVisible()) { //jfxDialog的show是无法获取状态的，故采用visible
                JFXDialog dialog1 = createDialog();
                dialog1.setContent(event.getPane());
                dialogs.add(dialog1);
                dialog1.setOnDialogClosed(jfxDialogEvent -> {
                    dialog1.setContent(null);
                    dialogs.remove(dialog1);
                });
                dialog1.show();
            } else {
                dialog.setVisible(true);
                dialog.setContent(event.getPane());
                dialog.show();
            }
        } else {
            if (dialogs.size() == 1) {
                dialog.close();
                dialog.setDisable(false);
            } else {
                JFXDialog dialog1 = dialogs.get(dialogs.size() - 1);
                dialog1.close();
            }
        }
    }


    /**
     * @return com.jfoenix.controls.JFXDialog
     * @description: 创建dialog
     * @name: createDialog
     * @author: Leck
     * @param:
     * @date: 2023/9/13
     */
    private JFXDialog createDialog() {
        JFXDialog dialog1 = new JFXDialog();
        dialog1.setDialogContainer(root);
        dialog1.setTransitionType(JFXDialog.DialogTransition.CENTER);
        StackPane dialogContainer = (StackPane) dialog1.getChildren().get(0);
        dialogContainer.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        return dialog1;
    }

    @Subscribe
    public void showNotification(MainNotificationEvent event) {
        if (event.getMessage() != null) {
            atlantafx.base.controls.Notification notification = new atlantafx.base.controls.Notification(event.getMessage());

            notification.getStyleClass().addAll(
                    Styles.WARNING, Styles.ELEVATED_1
            );
            notification.setPrefHeight(Region.USE_PREF_SIZE);
            notification.setMaxHeight(Region.USE_PREF_SIZE);
            StackPane.setAlignment(notification, Pos.TOP_RIGHT);
            StackPane.setMargin(notification, new Insets(10, 10, 0, 0));
            notification.setOnClose(e -> {
                var out = Animations.slideOutUp(notification, Duration.millis(250));
                out.setOnFinished(f -> content.getChildren().remove(notification));
                out.playFromStart();
            });

            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        var out = Animations.slideOutUp(notification, Duration.millis(250));
                        out.setOnFinished(f -> content.getChildren().remove(notification));
                        out.playFromStart();
                    });
                }
            }, 3000);


            Platform.runLater(() -> {
                content.getChildren().add(notification);
                var out = Animations.slideInDown(notification, Duration.millis(250));
                out.playFromStart();
            });


        }

    }


    @Subscribe
    public void addTopPane(MainPaneEvent event) {
        if (event.isAdd()) {
            root.getChildren().add(event.getPane());
        } else {
            if (root.getChildren().size() >= 2) {
                root.getChildren().remove(root.getChildren().size() - 1);
            }

        }

    }


    private void getCategory(CategoryType type) {
        CategoryUI categoryUI = new CategoryUI(type);
        addCenter(categoryUI.getRoot());
    }


    /**
     * @return void
     * @description: 对主中心位置界面进行管理，最底层的GridUI是永久存在的
     * @name: addCenter
     * @author: Leck
     * @param: node
     * @date: 2023/7/14
     */
    private void addCenter(Node node) {
        center.getChildren().add(node);
        int size = center.getChildren().size();
        if (size > 2) {
            center.getChildren().remove(1, center.getChildren().size() - 2);
        }
    }

    @Subscribe
    public void addCenter(MainCenterEvent event) {
        if (event.isAdd()) {
            addCenter(event.getPane());
        } else {
            if (event.isToBase()) {
                backBaseCenter();
            } else {
                int size = center.getChildren().size();
                if (size > 1) {
                    center.getChildren().remove(size - 1);
                }
            }
        }

    }


    /**
     * @return void
     * @description: 显示最底层的GridUI
     * @name: backBaseCenter
     * @author: Leck
     * @param:
     * @date: 2023/7/14
     */
    private void backBaseCenter() {
        int size = center.getChildren().size();
        if (size > 1) {
            center.getChildren().remove(1, center.getChildren().size());
        }

    }

    public Pane getRoot() {
        return root;
    }
}