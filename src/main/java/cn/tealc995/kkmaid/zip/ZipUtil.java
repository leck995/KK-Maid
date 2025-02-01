package cn.tealc995.kkmaid.zip;

import cn.tealc995.kkmaid.model.lrc.LrcBean;
import cn.tealc995.kkmaid.util.LrcFormatUtil;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-18 08:22
 */
public class ZipUtil {

    /**
     * @description: 从压缩包中回去字幕并解析
     * @name: getLrc
     * @author: Leck
     * @param:	path
     * @return  java.util.List<cn.tealc995.kkmaid.model.lrc.LrcBean>
     * @date:   2023/7/18
     */
    public static List<LrcBean> getLrc(String path){
        ZipFile zipFile = new ZipFile(path);
        zipFile.setCharset(Charset.forName("GBK"));
        FileHeader fileHeader = null;
        try {
            fileHeader = zipFile.getFileHeader(path);
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
        }
    }



    public static List<ZipEntityFile> getAllFile(File file){
        ZipFile zipFile=new ZipFile(file);
        if (isWindows()){
            zipFile.setCharset(Charset.forName("GBK"));
        }
        //获取所有的文件
        List<FileHeader> fileHeaders = null;
        try {
            fileHeaders = zipFile.getFileHeaders();
        } catch (ZipException e) {
            throw new RuntimeException(e);
        }
        if (fileHeaders == null) return null;

        List<ZipEntityFile> zipEntityFiles=new ArrayList<>();
        for (FileHeader fileHeader : fileHeaders) {
            String fileName = fileHeader.getFileName();
            zipEntityFiles.add(new ZipEntityFile(fileName));
        }

        //获取所有目录，并设置children
        List<ZipEntityFile> folders = zipEntityFiles.stream().filter(zipEntityFile1 -> zipEntityFile1.isFolder()).toList();
        for (ZipEntityFile folder : folders) {
            List<ZipEntityFile> list = zipEntityFiles.stream().filter(zipEntityFile1 -> !zipEntityFile1.getPath().equals(folder.getPath()) && zipEntityFile1.getParentPath().equals(folder.getPath()) ).toList();
            folder.setChildren(list);
        }


        //获取根文件和根目录
        List<ZipEntityFile> rootFile = zipEntityFiles.stream().filter(zipEntityFile1 -> !zipEntityFile1.isFolder() && zipEntityFile1.isRoot()).toList();
        List<ZipEntityFile> rootFolder = folders.stream().filter(zipEntityFile1 -> zipEntityFile1.isRoot()).toList();

        List<ZipEntityFile> list = Stream.concat(rootFolder.stream(), rootFile.stream()).toList();
        return list;
    }



    public static List<ZipEntityFile> getAllLrcFile(File file){
        ZipFile zipFile=new ZipFile(file);
        if (isWindows()){
            zipFile.setCharset(Charset.forName("GBK"));
        }

        //获取所有的文件
        List<FileHeader> fileHeaders = null;
        try {
            fileHeaders = zipFile.getFileHeaders();
        } catch (ZipException e) {
            throw new RuntimeException(e);
        }
        if (fileHeaders == null) return null;

        List<ZipEntityFile> zipEntityFiles=new ArrayList<>();
        for (FileHeader fileHeader : fileHeaders) {
            String fileName = fileHeader.getFileName();
            zipEntityFiles.add(new ZipEntityFile(fileName));
        }
        List<ZipEntityFile> list =zipEntityFiles.stream().filter(zipEntityFile -> zipEntityFile.isLrc()).toList();
        return list;
    }

    public static List<ZipEntityFile> getAllLrcFile(File file,Charset charset){
        ZipFile zipFile=new ZipFile(file);
        if (charset == null){
            if (isWindows()){
                zipFile.setCharset(Charset.forName("GBK"));
            }
        }else {
            zipFile.setCharset(charset);
        }

        //获取所有的文件
        List<FileHeader> fileHeaders = null;
        try {
            fileHeaders = zipFile.getFileHeaders();
        } catch (ZipException e) {
            throw new RuntimeException(e);
        }
        if (fileHeaders == null) return null;

        List<ZipEntityFile> zipEntityFiles=new ArrayList<>();
        for (FileHeader fileHeader : fileHeaders) {
            String fileName = fileHeader.getFileName();
            zipEntityFiles.add(new ZipEntityFile(fileName));
        }
        List<ZipEntityFile> list =zipEntityFiles.stream().filter(zipEntityFile -> zipEntityFile.isLrc()).toList();
        return list;
    }




    public static BufferedReader getLrcBufferedReader(String zipPath,String lrcPath){
        ZipFile zipFile = new ZipFile(zipPath);
        if (isWindows()){
            zipFile.setCharset(Charset.forName("GBK"));
        }

        FileHeader fileHeader = null;
        try {
            fileHeader = zipFile.getFileHeader(lrcPath);
            InputStream inputStream = zipFile.getInputStream(fileHeader);
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            return bufferedReader;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static BufferedReader getLrcBufferedReader(String zipPath,String lrcPath,Charset charset){
        ZipFile zipFile = new ZipFile(zipPath);
        if (charset == null){
            if (isWindows()){
                zipFile.setCharset(Charset.forName("GBK"));
            }
        }else {
            zipFile.setCharset(charset);
        }
        FileHeader fileHeader = null;
        try {
            fileHeader = zipFile.getFileHeader(lrcPath);
            InputStream inputStream = zipFile.getInputStream(fileHeader);
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            return bufferedReader;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @description: 判断操作系统从而获取编码，win默认gbk,而mac和linux默认utf-8
     * @name: isWindows
     * @author: Leck
     * @param:
     * @return  boolean
     * @date:   2023/7/20
     */
    public static boolean isWindows() {
        String osName = System.getProperty("os.name");

        return osName != null && osName.startsWith("Windows");
    }
}