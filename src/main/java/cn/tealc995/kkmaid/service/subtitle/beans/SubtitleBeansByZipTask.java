package cn.tealc995.kkmaid.service.subtitle.beans;

import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kkmaid.model.lrc.LrcBean;
import cn.tealc995.kkmaid.model.lrc.LrcFile;
import cn.tealc995.kkmaid.util.LrcFormatUtil;
import cn.tealc995.kkmaid.zip.NewZipUtil;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

/**
 * @program: KK-Maid
 * @description: 读取字幕压缩包内的指定字幕文件
 * @author: Leck
 * @create: 2025-02-03 21:05
 */
public class SubtitleBeansByZipTask extends SubtitleBeansBaseTask {
    private static final Logger LOG = LoggerFactory.getLogger(SubtitleBeansByZipTask.class);
    private final LrcFile source; //要读取的字幕文件信息
    private Charset zipCharset; //编码，压缩包的
    private Charset textCharset; //编码，压缩包的文本编码


    public SubtitleBeansByZipTask(LrcFile source) {
        this.source = source;
    }

    public SubtitleBeansByZipTask(LrcFile source, Charset zipCharset, Charset textCharset) {
        this.source = source;
        this.zipCharset = zipCharset;
        this.textCharset = textCharset;
    }

    @Override
    protected ResponseBody<List<LrcBean>> call() throws Exception {
        ZipFile zipFile = null;
        Optional<BufferedReader> readerOptional = null;
        try {
            if (zipCharset == null || textCharset == null) {
                zipFile = NewZipUtil.getZipFile(source.getZipPath());
                readerOptional = NewZipUtil.getSubtitleFileBufferedReader(zipFile, source.getPath());
            } else {
                zipFile = NewZipUtil.getZipFile(source.getZipPath(), zipCharset);
                readerOptional = NewZipUtil.getSubtitleFileBufferedReader(zipFile, source.getPath(),textCharset);
            }

            if (readerOptional.isPresent()) {
                BufferedReader bufferedReader = readerOptional.get();
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line).append("\\n");
                }
                return ResponseBody.create(200,"成功加载字幕",LrcFormatUtil.getLrcListFromLrcText(sb.toString()));
            }else {
                return ResponseBody.create(0,String.format("无法正确读取压缩包 %s 内字幕文件：%s",source.getZipPath(),source.getPath()),null);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return ResponseBody.create(-1,"加载字幕出现IO错误,具体原因查看日志",null);
        } finally {
            if (readerOptional != null && readerOptional.isPresent()) {
                readerOptional.get().close();
            }
            if (zipFile != null) {
                zipFile.close();
            }
        }
    }

}