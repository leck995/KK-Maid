package cn.tealc995.kkmaid.service;

import cn.tealc995.kkmaid.Config;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.util.*;

/**
 * @program: Asmr-Online
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
        if (Config.lrcFileFolder.get() != null && Config.lrcFileFolder.get().length() > 0){
            folderList=new HashSet<>();
            File dir=new File(Config.lrcFileFolder.get());
            File[] files = dir.listFiles((dir1, name) -> dir1.isDirectory());
            for (File file : files) {
                folderList.add(file.getName().toUpperCase());
            }
        }

        if (Config.lrcZipFolder.get() != null && Config.lrcZipFolder.get().length() > 0){
            zipList=new HashSet<>();
            File dir=new File(Config.lrcZipFolder.get());
            File[] files = dir.listFiles((dir1, name) -> dir1.isFile() && name.toLowerCase().endsWith(".zip"));
            for (File file : files) {
                zipList.add(file.getName().toUpperCase());
            }
        }
    }

}