package cn.tealc995.asmronline.util;

import cn.tealc995.asmronline.api.HttpUtils;
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
    public static List<LrcBean> getLrcFromZip(LrcFile lrcFile){
 /*       ZipFile zipFile = new ZipFile(lrcFile.getZipPath());
        zipFile.setCharset(Charset.forName("GBK"));
        FileHeader fileHeader = null;
        try {
            fileHeader = zipFile.getFileHeader(lrcFile.getPath());
            InputStream inputStream = zipFile.getInputStream(fileHeader);
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sb=new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine())!= null){
                sb.append(line).append("\\n");
            }
            bufferedReader.close();
            return LrcFormatUtil.getLrcListFromLrcText(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
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
    public static List<LrcBean> getLrcFromNet(String path){
        String s = HttpUtils.download(path);
        return LrcFormatUtil.getLrcListFromLrcText(s);
    }
}