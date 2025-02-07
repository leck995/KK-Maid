package cn.tealc995.kkmaid.ui;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-14 21:33
 */
public enum CategoryType {
    ALL("All Works",""),
    CIRCLE("Circle","$circle:%s$"),
    VA("Va","$va:%s$"),
    TAG("Tag","$tag:%s$"),
    STAR("Star",""),
    SEARCH("Search","%s"),

    PLAY_LIST("Play List","");
    private String title;
    private String format;

    CategoryType(String title, String format) {
        this.title = title;
        this.format = format;
    }

    public String getTitle() {
        return title;
    }

    public String getFormat() {
        return format;
    }
}