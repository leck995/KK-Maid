package cn.tealc995.asmronline.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * @program: AmsrPlayer
 * @description: 目录过滤，所有非目录过滤掉
 * @author: Leck
 * @create: 2023-01-09 17:59
 */
public class DirectoryFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory())
            return true;
        else
            return false;
    }
}