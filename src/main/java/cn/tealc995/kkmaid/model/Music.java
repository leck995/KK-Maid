package cn.tealc995.kkmaid.model;

import cn.tealc995.kikoreu.model.Work;
import cn.tealc995.kkmaid.model.lrc.LrcFile;

import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-14 00:10
 */
public class Music {
    private Work work;
    private List<Audio> audios;
    private List<LrcFile> lrcFiles;
    public Music(Work work, List<Audio> audios) {
        this.work = work;
        this.audios = audios;
    }

    public Music(Work work, List<Audio> audios, List<LrcFile> lrcFiles) {
        this.work = work;
        this.audios = audios;
        this.lrcFiles = lrcFiles;
    }

    public Music() {
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public List<Audio> getAudios() {
        return audios;
    }

    public void setAudios(List<Audio> audios) {
        this.audios = audios;
    }

    public List<LrcFile> getLrcFiles() {
        return lrcFiles;
    }

    public void setLrcFiles(List<LrcFile> lrcFiles) {
        this.lrcFiles = lrcFiles;
    }
}