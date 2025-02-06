package cn.tealc995.kkmaid.zip;

import cn.tealc995.kkmaid.model.lrc.LrcBean;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @program: KK-Maid
 * @description:
 * @author: Leck
 * @create: 2025-02-03 19:37
 */
public class NewZipUtil {
    /**
     * @description: 获取所有的文件，由层级
     * @param:	zipFile
     * @return  java.util.List<cn.tealc995.kkmaid.zip.ZipEntityFile>
     * @date:   2025/2/3
     */
    public static List<ZipEntityFile> getAllFile(File zipFile) throws IOException {
        return getAllFile(zipFile.getAbsolutePath());
    }

    /**
     * @description: 获取所有的文件，由层级
     * @param:	filepath
     * @return  java.util.List<cn.tealc995.kkmaid.zip.ZipEntityFile>
     * @date:   2025/2/3
     */
    public static List<ZipEntityFile> getAllFile(String filepath) throws IOException {
        List<ZipEntityFile> zipEntityFiles = getAllFileWithoutLevel(filepath);
        //获取所有目录，并设置children
        List<ZipEntityFile> folders = zipEntityFiles.stream().filter(ZipEntityFile::isFolder).toList();
        for (ZipEntityFile folder : folders) {
            List<ZipEntityFile> list = zipEntityFiles.stream().filter(zipEntityFile1 -> !zipEntityFile1.getPath().equals(folder.getPath()) && zipEntityFile1.getParentPath().equals(folder.getPath())).toList();
            folder.setChildren(list);
        }
        //获取根文件和根目录
        List<ZipEntityFile> rootFile = zipEntityFiles.stream().filter(zipEntityFile1 -> !zipEntityFile1.isFolder() && zipEntityFile1.isRoot()).toList();
        List<ZipEntityFile> rootFolder = folders.stream().filter(ZipEntityFile::isRoot).toList();

        return Stream.concat(rootFolder.stream(), rootFile.stream()).toList();
    }

    /**
     * @return java.util.List<cn.tealc995.kkmaid.zip.ZipEntityFile>
     * @description: 获取所有的字幕文件
     * @param: zipFile
     * @date: 2025/2/3
     */
    public static List<ZipEntityFile> getAllLrcFile(File zipFile) throws IOException {
        List<ZipEntityFile> zipEntityFiles = getAllFileWithoutLevel(zipFile.getAbsolutePath());
        return zipEntityFiles.stream().filter(ZipEntityFile::isLrc).toList();
    }

    /**
     * @description: 获取所有的字幕文件
     * @param:	zipFile
     * @param:	charset	编码
     * @return  java.util.List<cn.tealc995.kkmaid.zip.ZipEntityFile>
     * @date:   2025/2/3
     */
    public static List<ZipEntityFile> getAllLrcFile(File zipFile, Charset charset) throws IOException {
        List<ZipEntityFile> zipEntityFiles = getAllFileWithoutLevel(zipFile.getAbsolutePath(),charset);
        return zipEntityFiles.stream().filter(ZipEntityFile::isLrc).toList();
    }


    /**
     * @return java.util.List<cn.tealc995.kkmaid.zip.ZipEntityFile>
     * @description: 获取全部文件，不限制文件层级
     * @param: filepath
     * @date: 2025/2/3
     */
    public static List<ZipEntityFile> getAllFileWithoutLevel(String filepath) throws IOException {
        List<ZipEntityFile> zipEntityFiles = new ArrayList<>();
        try (ZipFile zip = ZipFile.builder().setPath(filepath).get()) {
            Enumeration<? extends ZipArchiveEntry> entries = zip.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                zipEntityFiles.add(new ZipEntityFile(entry.getName()));
            }
        }
        return zipEntityFiles;
    }

    /**
     * @description: 获取全部文件，不限制文件层级
     * @param:	filepath
     * @param:	charset
     * @return  java.util.List<cn.tealc995.kkmaid.zip.ZipEntityFile>
     * @date:   2025/2/3
     */
    public static List<ZipEntityFile> getAllFileWithoutLevel(String filepath, Charset charset) throws IOException {
        List<ZipEntityFile> zipEntityFiles = new ArrayList<>();
        try (ZipFile zip = ZipFile.builder().setPath(filepath).setCharset(charset).get()) {
            Enumeration<? extends ZipArchiveEntry> entries = zip.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                zipEntityFiles.add(new ZipEntityFile(entry.getName()));
            }
        }
        return zipEntityFiles;
    }

    /**
     * @description: 获取指定Zip文件中的指定文件流
     * @param:	zipFile
     * @param:	subtitlePath
     * @return  java.util.Optional<java.io.BufferedReader>
     * @date:   2025/2/3
     */
    public static Optional<BufferedReader> getSubtitleFileBufferedReader(ZipFile zipFile, String subtitlePath) throws IOException {
        ZipArchiveEntry entry = zipFile.getEntry(subtitlePath);
        if (entry != null) {
            System.out.println("not null");
            System.out.println(entry.getName());
            InputStream inputStream = zipFile.getInputStream(entry);
            return Optional.of(new BufferedReader(new InputStreamReader(inputStream)));
        } else {
            System.out.println("null");
            System.out.println(subtitlePath);
            return Optional.empty();
        }
    }

    /**
     * @description: 获取指定Zip文件中的指定文件流
     * @param:	zipFile
     * @param:	subtitlePath
     * @param:	charset	编码
     * @return  java.util.Optional<java.io.BufferedReader>
     * @date:   2025/2/3
     */
    public static Optional<BufferedReader> getSubtitleFileBufferedReader(ZipFile zipFile,String subtitlePath,Charset charset) throws IOException {
        ZipArchiveEntry entry = zipFile.getEntry(subtitlePath);
        if (entry != null) {
            System.out.println("not null");
            System.out.println(subtitlePath);
            InputStream inputStream = zipFile.getInputStream(entry);
            return Optional.of(new BufferedReader(new InputStreamReader(inputStream,charset)));
        } else {
            System.out.println("null");
            System.out.println(subtitlePath);
            return Optional.empty();
        }
    }

    /**
     * @description: 获取ZipFile
     * @param:	zipFilePath
     * @return  org.apache.commons.compress.archivers.zip.ZipFile
     * @date:   2025/2/3
     */
    public static ZipFile getZipFile(String zipFilePath) throws IOException {
        return ZipFile.builder().setPath(zipFilePath).get();
    }

    /**
     * @description: 获取ZipFile
     * @param:	zipFilePath
     * @param:	charset
     * @return  org.apache.commons.compress.archivers.zip.ZipFile
     * @date:   2025/2/3
     */
    public static ZipFile getZipFile(String zipFilePath,Charset charset) throws IOException {
        return ZipFile.builder().setPath(zipFilePath).setCharset(charset).get();
    }


}