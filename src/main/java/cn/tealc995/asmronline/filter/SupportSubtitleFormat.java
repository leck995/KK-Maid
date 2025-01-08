package cn.tealc995.asmronline.filter;

public enum SupportSubtitleFormat {
    LRC("lrc"),
    VTT("vtt");

    private String fileSuffix;

    private SupportSubtitleFormat(String filesuffix) {
        this.fileSuffix = filesuffix;
    }

    public String getFileSuffix() {
        return this.fileSuffix;
    }


    /**
     * @description: 直接对类型比较
     * @name: compareSuffix
     * @author: Leck
     * @param:	suffix
     * @return  boolean
     * @date:   2023/9/12
     */
    public static boolean compareSuffix(String suffix)
    {
        suffix=suffix.toUpperCase();
        for(SupportSubtitleFormat choice:values())
            if (choice.name().equals(suffix))
                return true;
        return false;
    }

    /**
     * @description: 对文件名称进行类型比较
     * @name: compareFile
     * @author: Leck
     * @param:	name
     * @return  boolean
     * @date:   2023/9/12
     */
    public static boolean compareFile(String name)
    {
        name = name.toUpperCase().substring(name.lastIndexOf(".") + 1);
        for(SupportSubtitleFormat choice:values())
            if (choice.name().equals(name))
                return true;
        return false;
    }


    public static SupportSubtitleFormat getType(String name)
    {
        name = name.toUpperCase().substring(name.lastIndexOf(".") + 1);
        for(SupportSubtitleFormat choice:values())
            if (choice.name().equals(name))
                return choice;
        return null;
    }
}
