package cn.tealc995.kkmaid.config;

import cn.tealc995.kikoreu.KKApi;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-12 21:08
 */
public class Config {
    private static final Logger LOG = LoggerFactory.getLogger(Config.class);
    public static String version = "1.3.0";
    public static Setting setting;
    public static BlackList blackList;


    static {
        ObjectMapper mapper = new ObjectMapper();
        File settingFile = new File("settings.json");
        if (settingFile.exists()) {
            try {
                setting = mapper.readValue(settingFile, Setting.class);
            } catch (IOException e) {
                LOG.error("配置信息解析失败，JSON文件可能损坏，重置设置选项",e);
                setting = new Setting();
            }
        } else {
            setting = new Setting();
        }

        blackList = new BlackList();
        try {
            File workBlacklistFile = new File("data/blacklist/works.json");
            if (workBlacklistFile.exists()) {
                blackList.getWorkBlackList().addAll(mapper.readValue(workBlacklistFile, new TypeReference<Set<String>>() {
                }));
            }
            File tagBlacklistFile = new File("data/blacklist/tags.json");
            if (tagBlacklistFile.exists()) {
                blackList.getTagBlackList().addAll(mapper.readValue(tagBlacklistFile, new TypeReference<Set<String>>() {
                }));
            }
            File textBlacklistFile = new File("data/blacklist/texts.json");
            if (textBlacklistFile.exists()) {
                blackList.getTextBlackList().addAll(mapper.readValue(textBlacklistFile, new TypeReference<Set<String>>() {
                }));
            }
        } catch (IOException e) {
            LOG.error("黑名单加载失败");
        }


        if (setting.getTOKEN() != null && !setting.getTOKEN().isEmpty()) {
            KKApi.getInstance().setToken(setting.getTOKEN());
        }
        if (setting.getHOST() != null && !setting.getHOST().isEmpty()) {
            KKApi.getInstance().setHost(setting.getHOST());
        }

        setting.TOKENProperty().addListener((observableValue, s, t1) -> {
            if (t1.isEmpty()) {
                KKApi.getInstance().setToken(null);
            } else {
                KKApi.getInstance().setToken(t1);
            }
        });

        setting.HOSTProperty().addListener((observableValue, s, t1) -> {
            if (t1.isEmpty()) {
                KKApi.getInstance().setHost(null);
            } else {
                KKApi.getInstance().setHost(t1);
            }
        });

    }


    /**
     * @return void
     * @description: 保存配置到本地
     * @name: saveProperties
     * @author: Leck
     * @param: close   程序退出
     * @date: 2023/3/2
     */
    public static void saveProperties() {
        try {
            /*=====================黑名单========================*/
            ObjectMapper mapper = new ObjectMapper();

            File settingFile = new File("settings.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(settingFile, setting);

            File file = new File("data/blacklist/works.json");
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, blackList.getWorkBlackList());

            File tagBlacklistFile = new File("data/blacklist/tags.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(tagBlacklistFile, blackList.getTagBlackList());

            File textBlacklistFile = new File("data/blacklist/texts.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(textBlacklistFile, blackList.getTextBlackList());
        } catch (IOException e) {
            LOG.error("保存配置文件出现错误", e);
        }
    }


}