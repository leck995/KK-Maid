package cn.tealc995.kikoreu.model.playList;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 创建歌单时的参数类
 * @author: Leck
 * @create: 2023-08-05 09:24
 */
public class PlayListCreate {
   private String name;
   private Integer privacy;
   private String locale;
   private String description;
   private List<String> works;

    public PlayListCreate() {
    }

    public PlayListCreate(String name, Integer privacy, String description, List<String> works) {
        this.name = name;
        this.privacy = privacy;
        this.locale = "zh-CN";
        this.description = description;
        this.works = works;
    }

    public PlayListCreate(String name, Integer privacy, String description, String worksRow) {
        this.name = name;
        this.privacy = privacy;
        this.locale = "zh-CN";
        this.description = description;
        this.works=new ArrayList<>();
        worksRow=worksRow.toUpperCase();
        Pattern pattern=Pattern.compile("(?<=RJ)\\d+");
        Matcher matcher = pattern.matcher(worksRow);
        while (matcher.find()){

            works.add(matcher.group());
        }

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getWorks() {
        return works;
    }

    public void setWorks(List<String> works) {
        this.works = works;
    }
}