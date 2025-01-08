package cn.tealc995.asmronline.filter;

public enum SupportAudioFormat {
    OGG("ogg"),
    MP3("mp3"),
    FLAC("flac"),
    M4A("m4a"),
    M4P("m4p"),
    WMA("wma"),
    WAV("wav"),
    RA("ra"),
    RM("rm"),
    M4B("m4b");

    private String filesuffix;

    private SupportAudioFormat(String filesuffix) {
        this.filesuffix = filesuffix;
    }

    public String getFilesuffix() {
        return this.filesuffix;
    }


    public static boolean contains(String s)
    {
        s=s.toUpperCase();
        for(SupportAudioFormat choice:values())
            if (choice.name().equals(s))
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
        for(SupportAudioFormat choice:values())
            if (choice.name().equals(name))
                return true;
        return false;
    }
}
