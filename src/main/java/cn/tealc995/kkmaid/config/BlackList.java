package cn.tealc995.kkmaid.config;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: KK-Maid
 * @description: 黑名单
 * @author: Leck
 * @create: 2025-02-03 23:13
 */
public class BlackList {
    private static final Logger LOG = LoggerFactory.getLogger(BlackList.class);
    private ObservableSet<String> workBlackList= FXCollections.observableSet();
    private ObservableSet<String> tagBlackList= FXCollections.observableSet();
    private ObservableSet<String> textBlackList= FXCollections.observableSet();

    public ObservableSet<String> getWorkBlackList() {
        return workBlackList;
    }

    public void setWorkBlackList(ObservableSet<String> workBlackList) {
        this.workBlackList = workBlackList;
    }

    public ObservableSet<String> getTagBlackList() {
        return tagBlackList;
    }

    public void setTagBlackList(ObservableSet<String> tagBlackList) {
        this.tagBlackList = tagBlackList;
    }

    public ObservableSet<String> getTextBlackList() {
        return textBlackList;
    }

    public void setTextBlackList(ObservableSet<String> textBlackList) {
        this.textBlackList = textBlackList;
    }
}