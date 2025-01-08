package cn.tealc995.asmronline.model;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-09-12 22:53
 */
public class StringKeyIntValue {
    private String key;
    private Integer value;

    public StringKeyIntValue(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s(%d)",key,value);
    }
}