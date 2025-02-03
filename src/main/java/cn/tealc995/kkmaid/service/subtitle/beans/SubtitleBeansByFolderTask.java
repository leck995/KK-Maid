package cn.tealc995.kkmaid.service.subtitle.beans;

import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kkmaid.model.lrc.LrcBean;
import cn.tealc995.kkmaid.util.LrcFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @program: KK-Maid
 * @description: 读取字幕文件夹内的指定字幕文件。用处过小，考虑弃用该功能
 * @author: Leck
 * @create: 2025-02-03 21:05
 */
public class SubtitleBeansByFolderTask extends SubtitleBeansBaseTask{
    private static final Logger LOG = LoggerFactory.getLogger(SubtitleBeansByFolderTask.class);
    private final String path; //要读取的字幕文件信息
    private Charset charset = StandardCharsets.UTF_8;

    public SubtitleBeansByFolderTask(String path) {
        this.path = path;
    }

    public SubtitleBeansByFolderTask(String path, Charset charset) {
        this.path = path;
        this.charset = charset;
    }

    @Override
    protected ResponseBody<List<LrcBean>> call() throws Exception {
        File file=new File(path);
        if (file.exists()){
            StringBuilder row;
            try {
                BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
                String line;
                row = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    row.append(line);
                    row.append(System.lineSeparator());
                }
                br.close();
                List<LrcBean> list = LrcFormatUtil.getLrcListFromLrcText(row.toString());
                if (list == null || list.isEmpty()) {
                    return ResponseBody.create(-1,"无法成功解析字幕",null);
                }else {
                    return ResponseBody.create(200,"成功解析字幕",list);
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(),e);
                return ResponseBody.create(-1,"读取字幕失败"+e.getMessage(),null);
            }

        }else{
            return ResponseBody.create(-1,"找不到字幕文件："+path,null);
        }
    }

}