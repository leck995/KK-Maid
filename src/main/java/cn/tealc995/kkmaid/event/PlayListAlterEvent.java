package cn.tealc995.kkmaid.event;

import cn.tealc995.kikoreu.model.playList.PlayList;

/**
 * @description:
 * @author: Leck
 * @create: 2023-08-07 04:20
 */
public class PlayListAlterEvent {
    private PlayList playList;

    public PlayListAlterEvent(PlayList playList) {
        this.playList = playList;
    }

    public PlayList getPlayList() {
        return playList;
    }

    public void setPlayList(PlayList playList) {
        this.playList = playList;
    }

}