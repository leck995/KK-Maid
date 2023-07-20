package cn.tealc995.teaFX.config;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.BoundingBox;

/**
 * @program: AmsrPlayer
 * @description:
 * @author: Leck
 * @create: 2023-01-20 21:43
 */
public class Config {
    public static BoundingBox ORIGINAL_SIZE;
    public static SimpleBooleanProperty SCREEN_MAXED=new SimpleBooleanProperty(false);
    public static SimpleBooleanProperty SCREEN_FULL=new SimpleBooleanProperty(false);
}