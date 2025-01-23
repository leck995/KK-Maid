package cn.tealc995.asmronline.service;

import cn.tealc995.asmronline.Config;
import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.MainNotificationEvent;
import cn.tealc995.asmronline.model.lrc.LrcFile;
import cn.tealc995.asmronline.model.lrc.LrcType;
import cn.tealc995.asmronline.zip.ZipEntityFile;
import cn.tealc995.asmronline.zip.ZipUtil;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-19 14:47
 */
public class SeekLrcFileService extends Service<List<LrcFile>> {
    private String id;
    private List<String> ids;
    private Charset charset = Charset.forName("GBK");

    @Override
    protected void succeeded() {
        super.succeeded();
        System.out.println("SeekLrcFileService success");
    }

    @Override
    protected Task<List<LrcFile>> createTask() {
        Task<List<LrcFile>> task=new Task<List<LrcFile>>() {
            @Override
            protected List<LrcFile> call() throws Exception {
                if (ids != null){
                    for (String id : ids) {
                        List<LrcFile> lrcFiles = seekLrcFile(id);
                        if (lrcFiles != null){
                            return lrcFiles;
                        }
                    }
                }
                if (id != null){
                    List<LrcFile> lrcFiles = seekLrcFile(id);
                    if (lrcFiles != null){
                        return lrcFiles;
                    }
                }
                return null;
            }
        };
        return task;
    }

    public void setId(String id) {
        ids=null;
        this.id = id;
    }

    public void setIds(List<String> ids) {
        id=null;
        this.ids = ids;
    }

    public void setCharset(Charset charset){
        this.charset=charset;
    }



    /**
     * @description:
     * @name: seekLrcFile
     * @author: Leck
     * @param:	id	记住这个id传过来7位的id变成8位
     * @return  void
     * @date:   2023/7/19
     */
    private List<LrcFile> seekLrcFile(String id) {
        System.out.println("SeekLrcFileService搜寻的id:"+id);
        String folder = Config.lrcFileFolder.get();
        if (folder != null && !folder.isEmpty()) {
            System.out.println("在字幕文件夹中寻找");
            File file = new File(folder);
            if (file.exists() && file.isDirectory()) {
                String finalId;
                if (id.toLowerCase().contains("rj")){
                    finalId=id.toLowerCase();
                }else {
                    finalId = "rj" + id;
                }

                System.out.println(finalId);
                File[] files = file.listFiles(file1 -> file1.isDirectory() && file1.getName().toLowerCase().contains(finalId));
                if (files != null && files.length > 0) {

                    System.out.println("在字幕文件夹中找到相关字幕");
                    File rjFolder = files[0];
                    File[] files1 = rjFolder.listFiles(file1 -> file1.isFile() && file1.getName().toLowerCase().endsWith(".lrc"));
                    List<LrcFile> list = new ArrayList<>();
                    if (files1 != null && files1.length > 0) {
                        for (File file1 : files1) {

                            list.add(new LrcFile(file1.getName(), file1.getPath(), LrcType.FOLDER));
                        }
                    }
                    return list;
                }
            }
        }

        folder = Config.lrcZipFolder.get();
        if (folder != null && folder.length() > 0) {
            System.out.println("在字幕包文件夹中寻找");
            File rootFolder = new File(folder);
            if (rootFolder.exists() && rootFolder.isDirectory()) {
                String finalId = id.toLowerCase().replace("rj","");
                if (finalId.length() == 7){
                    finalId = "0" + finalId;
                }
                String searchId = finalId;
                File[] files = rootFolder.listFiles(file -> {
                    String name = file.getName().toLowerCase().replace("rj","");
                    if (name.length() == 7){
                        name = "0" + name;
                    }
                    return file.isFile() && name.contains(searchId) && name.endsWith(".zip");
                });
                if (files != null && files.length > 0) {
                    System.out.println("在字幕压缩包文件夹中找到相关字幕");
                    File file = files[0];
                    List<ZipEntityFile> allLrcFile = ZipUtil.getAllLrcFile(file,charset);
                    List<LrcFile> list = new ArrayList<>();
                    for (ZipEntityFile zipEntityFile : allLrcFile) {
                        list.add(new LrcFile(zipEntityFile.getName(), zipEntityFile.getPath(), LrcType.ZIP, file.getPath()));
                    }
                    return list;
                }
            }
        }
        return null;
    }
}