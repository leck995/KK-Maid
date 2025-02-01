package cn.tealc995.kkmaid.api.model;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 03:35
 */
public enum SortType {
    release("发售时间"),
    create_date("收录"),

    rate_average_2dp("评分"),
    dl_count("销量"),
    price("价格"),
    nsfw("全年龄"),
    review_count("评论数量"),
    random("随机");

    private String value;

     SortType(String value) {
        this.value=value;
    }


    public String getValue() {
        return value;
    }
}