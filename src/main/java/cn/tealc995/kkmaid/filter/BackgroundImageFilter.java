package cn.tealc995.kkmaid.filter;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @program: AmsrPlayer
 * @description: 目录过滤，所有非目录过滤掉
 * @author: Leck
 * @create: 2023-01-09 17:59
 */
public class BackgroundImageFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        if (name.contains("background") && SupportImageFormat.contains(name.toUpperCase().substring(name.lastIndexOf(".") + 1))) {
            return true;
        }
        return false;
    }
}