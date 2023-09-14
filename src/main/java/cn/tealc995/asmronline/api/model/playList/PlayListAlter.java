package cn.tealc995.asmronline.api.model.playList;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-08 06:07
 */
public class PlayListAlter {
    private String id;
    private PlayListBase data;

    public PlayListAlter(String id, PlayListBase data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PlayListBase getData() {
        return data;
    }

    public void setData(PlayListBase date) {
        this.data = date;
    }
}