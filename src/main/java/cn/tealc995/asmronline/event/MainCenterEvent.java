package cn.tealc995.asmronline.event;

import javafx.scene.layout.Pane;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 08:58
 */
public class MainCenterEvent {
    private Pane pane;
    private boolean add;
    private boolean toBase;//返回到最底层的root,当为添加时不生效

    public MainCenterEvent(Pane pane, boolean add,boolean toBase) {
        this.pane = pane;
        this.add = add;
        this.toBase=toBase;
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

    public boolean isToBase() {
        return toBase;
    }

    public void setToBase(boolean toBase) {
        this.toBase = toBase;
    }
}