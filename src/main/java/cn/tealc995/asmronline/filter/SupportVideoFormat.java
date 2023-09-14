package cn.tealc995.asmronline.filter;

public enum SupportVideoFormat {
    MP4("mp4"),
    MKV("mkv"),
    AVI("avi");


    private String filesuffix;

    private SupportVideoFormat(String filesuffix) {
        this.filesuffix = filesuffix;
    }

    public String getFilesuffix() {
        return this.filesuffix;
    }

    public static boolean contains(String s)
    {
        for(SupportVideoFormat choice:values())
            if (choice.name().equals(s))
                return true;
        return false;
    }
}
