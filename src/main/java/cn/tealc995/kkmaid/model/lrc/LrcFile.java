package cn.tealc995.kkmaid.model.lrc;

/**
 * @program: Asmr-Online
 * @description: 歌词文件信息，包括名称，地址。
 * @author: Leck
 * @create: 2023-07-18 07:35
 */
public class LrcFile {
    private String title;
    private String path;
    private LrcType type;
    private String zipPath;

    public LrcFile(String title, String path, LrcType type) {
        this.title = title;
        this.path = path;
        this.type = type;
    }

    public LrcFile(String title, String path, LrcType type, String zipPath) {
        this.title = title;
        this.path = path;
        this.type = type;
        this.zipPath = zipPath;
    }

    public String getTitleWithoutSuffix() {
        int index = title.toLowerCase().lastIndexOf(".mp3.vtt");
        if (index != -1) {
            return title.substring(0, index);
        }
        index = title.toLowerCase().lastIndexOf(".wav.vtt");
        if (index != -1) {
            return title.substring(0, index);
        }
        index = title.lastIndexOf(".");
        if (index != -1)
            return title.substring(0, index);
        else
            return title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LrcType getType() {
        return type;
    }

    public void setType(LrcType type) {
        this.type = type;
    }

    public String getZipPath() {
        return zipPath;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }

    @Override
    public String toString() {
        return title;
    }
}

