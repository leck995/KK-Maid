package cn.tealc995.asmronline.util;

import cn.tealc995.asmronline.api.HttpUtils;
import cn.tealc995.asmronline.filter.SupportSubtitleFormat;
import cn.tealc995.asmronline.model.lrc.LrcBean;
import cn.tealc995.asmronline.model.lrc.LrcFile;
import cn.tealc995.asmronline.zip.ZipUtil;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @program: Asmr-Online
 * @description: 导入并解析歌词的工具类
 * @author: Leck
 * @create: 2023-07-18 08:28
 */
public class LrcImportUtil {
    public static List<LrcBean> getLrcFromFolder(String path){
        File file=new File(path);
        if (file.exists()){
            StringBuilder row;
            try {
                BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
                String line;
                row = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    row.append(line);
                    row.append(System.lineSeparator());
                }
                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return LrcFormatUtil.getLrcListFromLrcText(row.toString());
        }else{
            return null;
        }
    }


    public static String getLrcRowFromFolder(String path){
        File file=new File(path);
        if (file.exists()){
            StringBuilder row;
            try {
                BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
                String line;
                row = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    row.append(line);
                    row.append(System.lineSeparator());
                }
                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return row.toString();
        }else{
            return null;
        }
    }

    public static List<LrcBean> getLrcFromZip(LrcFile lrcFile){
        try {
            BufferedReader bufferedReader = ZipUtil.getLrcBufferedReader(lrcFile.getZipPath(), lrcFile.getPath());
            StringBuilder sb=new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine())!= null){
                sb.append(line).append("\\n");
            }
            bufferedReader.close();
            return LrcFormatUtil.getLrcListFromLrcText(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLrcRowFromZip(LrcFile lrcFile){
        try {
            BufferedReader bufferedReader = ZipUtil.getLrcBufferedReader(lrcFile.getZipPath(), lrcFile.getPath());
            StringBuilder sb=new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine())!= null){
                sb.append(line).append("\\n");
            }
            bufferedReader.close();
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getLrcRowFromZip(LrcFile lrcFile,Charset charset){
        try {
            BufferedReader bufferedReader = ZipUtil.getLrcBufferedReader(lrcFile.getZipPath(), lrcFile.getPath(),charset);
            StringBuilder sb=new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine())!= null){
                sb.append(line).append("\\n");
            }
            bufferedReader.close();
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<LrcBean> getLrcFromNet(String path){
        String s = HttpUtils.download(path);
        if (SupportSubtitleFormat.getType(path) == SupportSubtitleFormat.LRC){
            return LrcFormatUtil.getLrcListFromLrcText(s);
        }else if (SupportSubtitleFormat.getType(path) == SupportSubtitleFormat.VTT){
            return LrcFormatUtil.getLrcListFromVttText(s);
        }
        return null;
    }


    public static String getLrcRowFromNet(String path){
        String s = HttpUtils.download(path);
        return s;
    }
}