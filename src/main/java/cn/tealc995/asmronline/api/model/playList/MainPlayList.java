package cn.tealc995.asmronline.api.model.playList;

import cn.tealc995.asmronline.api.model.Pagination;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-02 08:17
 */
public class MainPlayList {
    private Pagination pagination;
    @JsonAlias("playlists")
    private List<PlayList> playLists;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<PlayList> getPlayLists() {
        return playLists;
    }

    public void setPlayLists(List<PlayList> playLists) {
        this.playLists = playLists;
    }
}