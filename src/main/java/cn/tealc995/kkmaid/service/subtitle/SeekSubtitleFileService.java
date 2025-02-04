package cn.tealc995.kkmaid.service.subtitle;

import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kkmaid.model.lrc.LrcFile;
import cn.tealc995.kkmaid.model.lrc.LrcType;
import cn.tealc995.kkmaid.util.comparator.LrcFileComparator;
import cn.tealc995.kkmaid.zip.NewZipUtil;
import cn.tealc995.kkmaid.zip.ZipEntityFile;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-19 14:47
 */
public class SeekSubtitleFileService extends Service<List<LrcFile>> {
    private static final Logger LOG = LoggerFactory.getLogger(SeekSubtitleFileService.class);
    private String id;
    private List<String> ids;
    private Charset charset = StandardCharsets.UTF_8;

    @Override
    protected Task<List<LrcFile>> createTask() {
        Task<List<LrcFile>> task = new Task<List<LrcFile>>() {
            @Override
            protected List<LrcFile> call() throws Exception {
                if (ids != null) {
                    for (String id : ids) {
                        List<LrcFile> lrcFiles = seekLrcFile(id);
                        if (lrcFiles != null) {
                            lrcFiles.sort(new LrcFileComparator());
                            return lrcFiles;
                        }
                    }
                }
                if (id != null) {
                    List<LrcFile> lrcFiles = seekLrcFile(id);
                    if (lrcFiles != null) {
                        lrcFiles.sort(new LrcFileComparator());
                        return lrcFiles;
                    }
                }
                return null;
            }
        };
        return task;
    }

    public void setId(String id) {
        ids = null;
        this.id = id;
    }

    public void setIds(List<String> ids) {
        id = null;
        this.ids = ids;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }


    /**
     * @return void
     * @description:
     * @name: seekLrcFile
     * @author: Leck
     * @param: id    记住这个id传过来7位的id变成8位
     * @date: 2023/7/19
     */
    private List<LrcFile> seekLrcFile(String id) {
        LOG.debug("开始搜索作品{}的字幕列表",id);
        String folder = Config.setting.getLrcFileFolder();
        if (folder != null && !folder.isEmpty()) {
            List<LrcFile> lrcFiles = seekInFolder(folder);
            if (lrcFiles != null && !lrcFiles.isEmpty()) {
                return lrcFiles;
            }
        }

        folder = Config.setting.getLrcZipFolder();
        if (folder != null && !folder.isEmpty()) {
            List<LrcFile> list = seekInZipFolder(id, folder);
            return list;
        }
        return null;
    }

    /**
     * @description: 从字幕文件夹搜索；目前逻辑是可以优化的，可以直接从SubtitleData获取处理，前提需要完成SubtitleData的重构，使用map而不是set
     * @param:	id
     * @param:	folder
     * @return  java.util.List<cn.tealc995.kkmaid.model.lrc.LrcFile>
     * @date:   2025/2/4
     */
    private List<LrcFile> seekInZipFolder(String id, String folder) {
        LOG.debug("在字幕包文件夹中寻找...");
        File rootFolder = new File(folder);
        if (rootFolder.exists() && rootFolder.isDirectory()) {
            String finalId = id.toLowerCase().replace("rj", "");
            if (finalId.length() == 7) {
                finalId = "0" + finalId;
            }
            String searchId = finalId;
            File[] files = rootFolder.listFiles(file -> {
                String name = file.getName().toLowerCase().replace("rj", "");
                if (name.length() == 7) {
                    name = "0" + name;
                }
                return file.isFile() && name.contains(searchId) && name.endsWith(".zip");
            });
            if (files != null && files.length > 0) {
                LOG.debug("在字幕压缩包文件夹中找到相关字幕");
                File file = files[0];
                try {
                    List<ZipEntityFile> allLrcFile = NewZipUtil.getAllLrcFile(file, charset);
                    List<LrcFile> list = new ArrayList<>();
                    for (ZipEntityFile zipEntityFile : allLrcFile) {
                        list.add(new LrcFile(zipEntityFile.getName(), zipEntityFile.getPath(), LrcType.ZIP, file.getPath()));
                    }
                    return list;
                } catch (IOException e) {
                    LOG.error("加载字幕Zip出现错误");
                }
            }
        }
        return null;
    }


    /**
     * @description: 从zip目录搜索；目前逻辑是可以优化的，可以直接从SubtitleData获取处理，前提需要完成SubtitleData的重构，使用map而不是set
     * @param:	folder
     * @return  java.util.List<cn.tealc995.kkmaid.model.lrc.LrcFile>
     * @date:   2025/2/4
     */
    private List<LrcFile> seekInFolder(String folder) {
        LOG.debug("在字幕文件夹中寻找...");
        File subtitleDir = new File(folder);
        if (subtitleDir.exists() && subtitleDir.isDirectory()) {
            String finalId = getFullWorkId(id);
            File[] files = subtitleDir.listFiles(file -> file.isDirectory() && file.getName().toLowerCase().contains(finalId));
            if (files != null && files.length > 0) {
                LOG.debug("成功在字幕文件夹中找到相关字幕");
                File rjFolder = files[0];
                File[] subtitleFiles = rjFolder.listFiles(
                        file -> {
                            if (file.isFile()) {
                                String fileName = file.getName().toLowerCase();
                                return fileName.endsWith(".lrc") || fileName.endsWith(".vtt");
                            }
                            return false;
                        }
                );

                if (subtitleFiles != null && subtitleFiles.length > 0) {
                    List<LrcFile> list = new ArrayList<>();
                    for (File subtitleFile : subtitleFiles) {
                        list.add(new LrcFile(subtitleFile.getName(), subtitleFile.getPath(), LrcType.FOLDER));
                    }
                    return list;
                }
            }
        }
        return null;
    }



    private static String getFullWorkId(String id) {
        String finalId;
        if (id.toLowerCase().contains("rj")) {
            finalId = id.toLowerCase();
        } else {
            finalId = "rj" + id;
        }
        return finalId;
    }
}