package cn.tealc995.asmronline.event;

import cn.tealc995.asmronline.ui.CategoryType;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-14 21:02
 */
public class SearchEvent {
    private String key;
    private CategoryType type;

    public SearchEvent(String key) {
        this.key = key;
    }

    public SearchEvent(CategoryType type,String key) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }
}