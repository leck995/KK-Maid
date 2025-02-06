package cn.tealc995.kkmaid.filter;


import java.io.File;
import java.io.FilenameFilter;

/**
 * @description:
 * @author: Leck
 * @create: 2023-01-13 19:15
 */
public class LrcFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        if (name.endsWith(".lrc") || name.endsWith(".LRC")){
            return true;
        }
        return false;
    }
}