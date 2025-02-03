package cn.tealc995.kkmaid.util;

import cn.tealc995.kikoreu.HttpUtils;
import cn.tealc995.kkmaid.filter.SupportSubtitleFormat;
import cn.tealc995.kkmaid.model.lrc.LrcBean;
import cn.tealc995.kkmaid.model.lrc.LrcFile;
import cn.tealc995.kkmaid.zip.NewZipUtil;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.Lists;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

/**
 * @description: 导入并解析歌词的工具类
 * @author: Leck
 * @create: 2023-07-18 08:28
 */
public class LrcImportUtil {
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
        try(ZipFile zipFile = NewZipUtil.getZipFile(lrcFile.getZipPath())) {
            Optional<BufferedReader> readerOptional = NewZipUtil.getSubtitleFileBufferedReader(zipFile, lrcFile.getPath());
            if (readerOptional.isPresent()) {
                BufferedReader bufferedReader = readerOptional.get();
                StringBuilder sb=new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine())!= null){
                    sb.append(line).append("\\n");
                }
                bufferedReader.close();
                return LrcFormatUtil.getLrcListFromLrcText(sb.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Lists.newArrayList();
    }

    public static String getLrcRowFromZip(LrcFile lrcFile){
        try(ZipFile zipFile = NewZipUtil.getZipFile(lrcFile.getZipPath())) {
            Optional<BufferedReader> readerOptional = NewZipUtil.getSubtitleFileBufferedReader(zipFile, lrcFile.getPath());
            if (readerOptional.isPresent()) {
                BufferedReader bufferedReader = readerOptional.get();
                StringBuilder sb=new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine())!= null){
                    sb.append(line).append("\\n");
                }
                bufferedReader.close();
                return sb.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public static String getLrcRowFromZip(LrcFile lrcFile,Charset charset){
        try(ZipFile zipFile = NewZipUtil.getZipFile(lrcFile.getZipPath(),charset)) {
            Optional<BufferedReader> readerOptional = NewZipUtil.getSubtitleFileBufferedReader(zipFile, lrcFile.getPath());
            if (readerOptional.isPresent()) {
                BufferedReader bufferedReader = readerOptional.get();
                StringBuilder sb=new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine())!= null){
                    sb.append(line).append("\\n");
                }
                bufferedReader.close();
                return sb.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    public static String getLrcRowFromNet(String path){
        String s = HttpUtils.download(path);
        return s;
    }
}