package cn.tealc995.kkmaid.model;

import cn.tealc995.kkmaid.config.Config;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: KK-Maid
 * @description: 存储字幕列表，一般用于后台线程执行，故无需单独线程优化
 * @author: Leck
 * @create: 2025-02-04 19:49
 */
public class SubtitleData {
    private static SubtitleData instance;
    private Set<String> folderList;//保存根文件夹下的字幕目录名称，全部小写
    private Set<String> zipList;//保存所有字幕Zip的名称，全部小写

    private SubtitleData() {
        updateList();
    }

    public static SubtitleData getInstance() {
        if (instance == null) { // 第一次检查
            synchronized (SubtitleData.class) { // 加锁
                if (instance == null) { // 第二次检查
                    instance = new SubtitleData();
                }
            }
        }
        return instance;
    }

    /**
     * @description: 获取字幕列表
     * @param:
     * @return  void
     * @date:   2025/2/4
     */
    private void updateList() {
        String lrcFileFolder = Config.setting.getLrcFileFolder();
        if (lrcFileFolder != null && !lrcFileFolder.isEmpty()) {
            folderList = new HashSet<>();
            File dir = new File(lrcFileFolder);
            File[] files = dir.listFiles((dir1, name) -> dir1.isDirectory());
            if (files != null) {
                for (File file : files) {
                    folderList.add(file.getName().toLowerCase());
                }
            }
        }

        String lrcZipFolder = Config.setting.getLrcZipFolder();
        if (lrcZipFolder != null && !lrcZipFolder.isEmpty()) {
            zipList = new HashSet<>();
            File dir = new File(lrcZipFolder);
            File[] files = dir.listFiles(file -> file.isFile() && file.getName().toLowerCase().endsWith(".zip"));
            if (files != null) {
                for (File file : files) {
                    zipList.add(file.getName().toLowerCase());
                }
            }
        }
    }


    public Set<String> getFolderList() {
        return folderList;
    }

    public Set<String> getZipList() {
        return zipList;
    }
}