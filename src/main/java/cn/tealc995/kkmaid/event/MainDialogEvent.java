package cn.tealc995.kkmaid.event;

import javafx.scene.layout.Pane;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 08:58
 */
public class MainDialogEvent {
    private Pane pane;

    public MainDialogEvent(Pane pane) {
        this.pane = pane;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }
}