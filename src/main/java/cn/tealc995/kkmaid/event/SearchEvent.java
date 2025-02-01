package cn.tealc995.kkmaid.event;

import cn.tealc995.kkmaid.ui.CategoryType;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-14 21:02
 */
public class SearchEvent {
    private String key;
    private String info;//备用
    private CategoryType type;

    public SearchEvent(String key) {
        this.key = key;
    }

    public SearchEvent(CategoryType type,String key) {
        this.key = key;
        this.type = type;
    }

    public SearchEvent(CategoryType type,String key, String info) {
        this.key = key;
        this.info = info;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}