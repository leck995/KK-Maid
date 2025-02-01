package cn.tealc995.kkmaid.filter;

/**
 * @program: AmsrPlayer
 * @description:
 * @author: Leck
 * @create: 2023-01-13 18:02
 */
public enum SupportImageFormat {
    JPG("jpg"),
    PNG("png"),
    JPEG("jpeg");


    private String filesuffix;

    private SupportImageFormat(String filesuffix) {
        this.filesuffix = filesuffix;
    }

    public String getFilesuffix() {
        return this.filesuffix;
    }

    public static boolean contains(String s)
    {
        s=s.toUpperCase();
        for(SupportImageFormat choice:values())
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
        for(SupportImageFormat choice:values())
            if (choice.name().equals(name))
                return true;
        return false;
    }
}