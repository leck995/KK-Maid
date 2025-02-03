package cn.tealc995.kkmaid.service;

import cn.tealc995.kkmaid.config.Config;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.util.*;

/**
 * @description:
 * @author: Leck
 * @create: 2023-08-02 05:14
 */
public class SeekLrcFileExistService extends Service<Boolean> {
    private Set<String> folderList;//保存所有字幕
    private Set<String> zipList;//保存所有字幕
    @Override
    protected Task<Boolean> createTask() {
        Task<Boolean> task=new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                if (folderList == null && zipList ==null){ //此处有待优化，比如更新了目录地址，无法实时更新
                    updateList();
                }
                if (folderList != null){
                    //folderList.
                }


                return null;
            }
        };
        return task;
    }



    private void updateList(){
        String lrcFileFolder = Config.setting.getLrcFileFolder();
        if (lrcFileFolder != null && !lrcFileFolder.isEmpty()){
            folderList=new HashSet<>();
            File dir=new File(lrcFileFolder);
            File[] files = dir.listFiles((dir1, name) -> dir1.isDirectory());
            if (files != null) {
                for (File file : files) {
                    folderList.add(file.getName().toUpperCase());
                }
            }
        }

        String lrcZipFolder = Config.setting.getLrcZipFolder();
        if (lrcZipFolder != null && !lrcZipFolder.isEmpty()){
            zipList=new HashSet<>();
            File dir=new File(lrcZipFolder);
            File[] files = dir.listFiles((dir1, name) -> dir1.isFile() && name.toLowerCase().endsWith(".zip"));
            if (files != null) {
                for (File file : files) {
                    zipList.add(file.getName().toUpperCase());
                }
            }
        }
    }

}