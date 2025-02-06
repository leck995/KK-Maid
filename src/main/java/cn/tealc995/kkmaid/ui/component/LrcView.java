package cn.tealc995.kkmaid.ui.component;


import cn.tealc995.kkmaid.model.lrc.LrcBean;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * @ClassName LrcView
 * @Description: 非常简单的歌词显示控件
 * @Author Leck
 * @Date 2022/2/20
 **/
public class LrcView<T> extends ListView<LrcBean> {

    private final SimpleDoubleProperty currentTime; //播放时间(进度)
    private final SimpleBooleanProperty showTrans;
    private boolean isScrolling = false;
    private final PauseTransition timer;

    public LrcView(SimpleDoubleProperty currentTime) {
        this.currentTime = currentTime;
        showTrans = new SimpleBooleanProperty(true);
        Label placeholder = new Label("敬请欣赏");
        placeholder.setFont(Font.font(24));
        setPlaceholder(placeholder);

        setCellFactory(_ -> {
            ListCell<LrcBean> cell = new ListCell<>() {
                @Override
                protected void updateItem(LrcBean lrcBean, boolean b) {
                    super.updateItem(lrcBean, b);
                    if (lrcBean != null) {
                        setText(lrcBean.getRowText());
                    } else {
                        setText("");
                    }
                }
            };
            cell.setPrefWidth(300);
            cell.setWrapText(true);
            cell.prefWidthProperty().bind(widthProperty().multiply(0.65));
            return cell;
        });

        this.currentTime.addListener((_, _, t1) -> update(t1));

        // 屏蔽所有鼠标事件
        addEventFilter(MouseEvent.ANY, Event::consume);
        // 添加滚动计时器,2s内不滚动回到当前选中字幕
        timer = new PauseTransition(Duration.seconds(2));
        timer.setOnFinished(_ -> {
            isScrolling = false;
            update(currentTime.get());
        });
        // 添加滚动事件过滤器
        addEventFilter(ScrollEvent.SCROLL, event -> {
            if (!isScrolling) {
                isScrolling = true;
                timer.play();
            } else {
                resetTimer();
            }
        });
    }

    private void update(Number time) {
        if (time == null || isScrolling) return;
        if (getItems() != null & !getItems().isEmpty()) {
            long millis = time.longValue() * 1000;
            int selectIndex = getSelectionModel().getSelectedIndex();
            if (millis < getItems().getFirst().getLongTime()) {
                if (selectIndex != 0) {
                    getSelectionModel().selectFirst();
                    scrollTo(0);
                    return;
                }
            }
            int size = getItems().size() - 1;
            for (int i = 0; i < size; i++) {
                if (millis > getItems().get(i).getLongTime() && millis < getItems().get(i + 1).getLongTime()) {
                    if (selectIndex != i) {
                        getSelectionModel().select(i);
                        scrollTo(i, false);
                        return;
                    }
                }
            }
            if (millis > getItems().get(size).getLongTime()) {
                if (selectIndex != size) {
                    getSelectionModel().select(size);
                    scrollTo(size, false);
                }
            }
        }
    }



    /**
     * @description: 重置计时器
     * @param:
     * @return  void
     * @date:   2025/2/6
     */
    private void resetTimer() {
        if (timer != null) {
            timer.stop(); // 停止当前计时器
            timer.playFromStart(); // 重新启动计时器
        }
    }


    /**
     * @param index:    歌词所在的index
     * @param hasTrans: 是否有翻译
     * @Description: 跳转listview到指定行, 目前找到我能接收的方法判断是否有翻译，故默认hasTrans没有用
     * @MethodName: scrollTo
     * @Return: void
     * @Author: Leck
     * @Date: 2022/2/15
     */
    private void scrollTo(int index, boolean hasTrans) {
        if (getItems().isEmpty() || getChildren().isEmpty()) return;
        VirtualFlow<ListCell<LrcBean>> virtualFlow = (VirtualFlow<ListCell<LrcBean>>) getChildren().getFirst();
        if (virtualFlow.getLastVisibleCell() != null) {
            int i = index - (int) ((virtualFlow.getLastVisibleCell().getIndex() - virtualFlow.getFirstVisibleCell().getIndex()) * 0.5) + 1;
            scrollTo(Math.max(i, 0));
            //virtualFlow.scrollTo(Math.max(i, 0));
        }

    }


    public boolean isShowTrans() {
        return showTrans.get();
    }

    public SimpleBooleanProperty showTransProperty() {
        return showTrans;
    }

    public void setShowTrans(boolean showTrans) {
        this.showTrans.set(showTrans);
    }
}
