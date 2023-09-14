package cn.tealc995.asmronline.filter;


import java.io.File;
import java.io.FilenameFilter;

/**
 * @program: AmsrPlayer
 * @description:
 * @author: Leck
 * @create: 2023-01-13 19:15
 */
public class ImageFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        int i = name.lastIndexOf(".");
        String ext = i == -1 ? "" : name.substring(i + 1).toUpperCase();
        return   SupportImageFormat.contains(ext);
    }
}