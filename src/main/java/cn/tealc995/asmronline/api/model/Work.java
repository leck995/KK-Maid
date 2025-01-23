package cn.tealc995.asmronline.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 03:23
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Work {
    private Role circle;
    private String create_date;
    private String dl_count;
    private boolean has_subtitle;

    private String id;
    private String mainCoverUrl;

    private boolean nsfw;
    private double price;
    private double rate_average_2dp;
    private int rate_count;
    private String release;
    private int review_count;
    private String thumbnailCoverUrl;
    private String title;
    private Double userRating;
    private List<Role> vas;

    private List<Role> tags;
    private List<CountDetail> rate_count_detail;
    private List<Rank> rank;

    @JsonDeserialize(using = LanguageEditionDeserialize.class)
    private List<LanguageEdition> language_editions;

    private boolean black; //黑名单
    private boolean hasLocalSubtitle;//本地字幕



    /**
     * @description: 判断是否有多语言版本
     * @name: hasLanguages
     * @author: Leck
     * @param:
     * @return  java.util.List<java.lang.String>
     * @date:   2023/7/19
     */
    public boolean hasLanguages(){
        return language_editions != null && language_editions.size() > 0;
    }
    /**
     * @description: 获取多语言版本的所有rjid
     * @name: getAllId
     * @author: Leck
     * @param:
     * @return  java.util.List<java.lang.String>
     * @date:   2023/7/19
     */
    public List<String> getAllId(){
        List<String> ids=new ArrayList<>();
        if (hasLanguages()){
            for (LanguageEdition languageEdition : language_editions) {
                ids.add(languageEdition.getWorkno());
            }
        }
        if (ids.isEmpty()){
            ids.add(id);
        }
        return ids;
    }



    public Role getCircle() {
        return circle;
    }

    public void setCircle(Role circle) {
        this.circle = circle;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getDl_count() {
        return dl_count;
    }

    public void setDl_count(String dl_count) {
        this.dl_count = dl_count;
    }

    public boolean isHas_subtitle() {
        return has_subtitle;
    }

    public void setHas_subtitle(boolean has_subtitle) {
        this.has_subtitle = has_subtitle;
    }

    public String getId() {
        return id;
    }
    public String getFullId() {
        if (id.length() == 7)
            return "0"+id;
        return id;
    }

    public void setId(String id) {
            this.id=id;
    }

    public String getMainCoverUrl() {
        return mainCoverUrl;
    }

    public void setMainCoverUrl(String mainCoverUrl) {
        this.mainCoverUrl = mainCoverUrl;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRate_average_2dp() {
        return rate_average_2dp;
    }

    public void setRate_average_2dp(double rate_average_2dp) {
        this.rate_average_2dp = rate_average_2dp;
    }

    public int getRate_count() {
        return rate_count;
    }

    public void setRate_count(int rate_count) {
        this.rate_count = rate_count;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public String getThumbnailCoverUrl() {
        return thumbnailCoverUrl;
    }

    public void setThumbnailCoverUrl(String thumbnailCoverUrl) {
        this.thumbnailCoverUrl = thumbnailCoverUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public List<Role> getVas() {
        return vas;
    }

    public void setVas(List<Role> vas) {
        this.vas = vas;
    }

    public List<Role> getTags() {
        return tags;
    }

    public void setTags(List<Role> tags) {
        this.tags = tags;
    }

    public List<CountDetail> getRate_count_detail() {
        return rate_count_detail;
    }

    public void setRate_count_detail(List<CountDetail> rate_count_detail) {
        this.rate_count_detail = rate_count_detail;
    }

    public List<Rank> getRank() {
        return rank;
    }

    public void setRank(List<Rank> rank) {
        this.rank = rank;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    public List<LanguageEdition> getLanguage_editions() {
        return language_editions;
    }

    public void setLanguage_editions(List<LanguageEdition> language_editions) {
        this.language_editions = language_editions;
    }

    public boolean isBlack() {
        return black;
    }

    public void setBlack(boolean black) {
        this.black = black;
    }

    @Override
    public boolean equals(Object obj) {
        Work temp= (Work) obj;
        return temp.getId().equals(this.getId());
    }

    public boolean isHasLocalSubtitle() {
        return hasLocalSubtitle;
    }

    public void setHasLocalSubtitle(boolean hasLocalSubtitle) {
        this.hasLocalSubtitle = hasLocalSubtitle;
    }
}