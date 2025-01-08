package cn.tealc995.asmronline.event;

import javafx.scene.layout.Pane;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 08:58
 */
public class MainPaneEvent {
    private Pane pane;
    private boolean add;

    public MainPaneEvent(Pane pane, boolean add) {
        this.pane = pane;
        this.add = add;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }
}