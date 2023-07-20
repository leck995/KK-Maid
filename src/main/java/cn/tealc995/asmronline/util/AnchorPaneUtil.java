package cn.tealc995.asmronline.util;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * @program: AmsrPlayer
 * @description:
 * @author: Leck
 * @create: 2023-01-18 22:35
 */
public class AnchorPaneUtil {
    public static void setPosition(Node pane, Double top, Double right, Double bottom, Double left){
        AnchorPane.setTopAnchor(pane,top);
        AnchorPane.setRightAnchor(pane,right);
        AnchorPane.setBottomAnchor(pane,bottom);
        AnchorPane.setLeftAnchor(pane,left);
    }
}