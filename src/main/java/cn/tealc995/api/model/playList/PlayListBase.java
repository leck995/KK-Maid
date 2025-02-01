package cn.tealc995.api.model.playList;

/**
 * @program: Asmr-Online
 * @description: PlayList基础类
 * @author: Leck
 * @create: 2023-08-08 06:06
 */
public class PlayListBase {
    private String name;
    private Integer privacy;
    private String description;

    public PlayListBase(String name, Integer privacy, String description) {
        this.name = name;
        this.privacy = privacy;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}