package cn.tealc995.kkmaid.service.subtitle.beans;

import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kkmaid.filter.SupportSubtitleFormat;
import cn.tealc995.kkmaid.model.lrc.LrcBean;
import cn.tealc995.kkmaid.util.LrcFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @program: KK-Maid
 * @description: 读取网络中的字幕文件；
 * @author: Leck
 * @create: 2025-02-03 21:05
 */
public class SubtitleBeansByNetTask extends SubtitleBeansBaseTask {
    private static final Logger LOG = LoggerFactory.getLogger(SubtitleBeansByNetTask.class);
    private final String httpUrl;
    private Charset charset = StandardCharsets.UTF_8;

    public SubtitleBeansByNetTask(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public SubtitleBeansByNetTask(String httpUrl, Charset charset) {
        this.httpUrl = httpUrl;
        this.charset = charset;
    }

    @Override
    protected ResponseBody<List<LrcBean>> call() {
        try {
            if (SupportSubtitleFormat.getType(httpUrl) == SupportSubtitleFormat.LRC) {
                String content = download();
                List<LrcBean> list = LrcFormatUtil.getLrcListFromLrcText(content);
                return ResponseBody.create(200, "成功下载字幕文件", list);
            } else if (SupportSubtitleFormat.getType(httpUrl) == SupportSubtitleFormat.VTT) {
                String content = download();
                List<LrcBean> list = LrcFormatUtil.getLrcListFromVttText(content);
                return ResponseBody.create(200, "成功下载字幕文件", list);
            } else {
                return ResponseBody.create(-1, "字幕格式不正确，无法加载", null);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return ResponseBody.create(-1, "下载在线字幕失败，原因：" + e.getMessage(), null);
        }
    }


    private String download() throws IOException {
        URL url = URI.create(httpUrl).toURL();
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), charset))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\\n");
            }
            return sb.toString();
        }
    }
}