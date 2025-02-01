package cn.tealc995.kkmaid.model.lrc;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: LrcBean <br>
 * date: 2021/4/22 18:28 <br>
 * author: Leck <br>
 * version: 1.0 <br>
 */
public class LrcBean {
    private long longTime;
    private String time;
    private String rowText;
    private String transText;

    public LrcBean(long longTime, String time, String rowText) {
        this.longTime = longTime;
        this.time = time;
        //判断歌词是否有翻译
        Matcher matcher=Pattern.compile("(.+)(「.*」)").matcher(rowText);
        if (matcher.find()){
            this.rowText=matcher.group(1);
            this.transText=matcher.group(2).substring(1,matcher.group(2).length()-1);
        }else{
            this.rowText = rowText;
        }
    }



    public long getLongTime() {
        return longTime;
    }

    public void setLongTime(long longTime) {
        this.longTime = longTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRowText() {
        return rowText;
    }

    public void setRowText(String rowText) {
        this.rowText = rowText;
    }

    public String getTransText() {
        return transText;
    }

    public void setTransText(String transText) {
        this.transText = transText;
    }

    @Override
    public String toString() {
        return "LrcBean{" +
                "longTime=" + longTime +
                ", time='" + time + '\'' +
                ", rowText='" + rowText + '\'' +
                ", transText='" + transText + '\'' +
                '}';
    }
}
