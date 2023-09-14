package cn.tealc995.aria2.model;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-09-13 01:26
 */
public class Aria2Option {
    private String dir;
    private String out;
    private String referer;

    public Aria2Option() {
    }

    public Aria2Option(String path, String referer) {
        int i = path.lastIndexOf("/");

        this.dir = path.substring(0,i);
        this.out = path.substring(i+1);
        this.referer = referer;
    }
    public Aria2Option(String dir, String out, String referer) {
        this.dir = dir;
        this.out = out;
        this.referer = referer;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }
}