package cn.tealc995.asmronline.api.model.playList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-05 09:24
 */
public class PlayListRemoveWork {
    private String id;
    private List<String> works;

    @JsonIgnore
    private Boolean remove;
    public PlayListRemoveWork(String id, List<String> works,Boolean remove) {
        this.id = id;
        this.works = works;
        this.remove=remove;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getWorks() {
        return works;
    }

    public void setWorks(List<String> works) {
        this.works = works;
    }

    public Boolean getRemove() {
        return remove;
    }

    public void setRemove(Boolean remove) {
        this.remove = remove;
    }
}