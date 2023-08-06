package cn.tealc995.asmronline.api.model.playList;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-08-02 08:09
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayList {
    private String id;
    private String name;
    private String description;

    @JsonAlias("user_name")
    private String username;
    private String mainCoverUrl;

    private Integer privacy;
    @JsonAlias("updated_at")
    private String updatedTime;
    @JsonAlias("created_at")
    private String createdTime;
    @JsonAlias("works_count")
    private Integer worksCount;

    private Boolean exist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if (name.equals("__SYS_PLAYLIST_LIKED"))
            return "我喜欢的";
        else if (name.equals("__SYS_PLAYLIST_MARKED")) {
            return "我标记的";
        }else
            return name;
    }

    public boolean canDelete() {
        return name.equals("__SYS_PLAYLIST_LIKED") || name.equals("__SYS_PLAYLIST_MARKED");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMainCoverUrl() {
        return mainCoverUrl;
    }

    public void setMainCoverUrl(String mainCoverUrl) {
        this.mainCoverUrl = mainCoverUrl;
    }

    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getWorksCount() {
        return worksCount;
    }

    public void setWorksCount(Integer worksCount) {
        this.worksCount = worksCount;
    }

    public Boolean getExist() {
        return exist;
    }

    public void setExist(Boolean exist) {
        this.exist = exist;
    }
}