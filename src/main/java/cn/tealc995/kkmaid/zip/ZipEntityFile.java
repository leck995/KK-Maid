package cn.tealc995.kkmaid.zip;

import java.util.List;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-17 04:39
 */
public class ZipEntityFile {
    private String name;
    private String path;
    private String type;
    private List<ZipEntityFile> children;
    private boolean root;

    public ZipEntityFile(String name, List<ZipEntityFile> children) {
        this.name = name;
        this.children = children;
    }

    public ZipEntityFile(String filePath) {
        path=filePath;
        if (filePath.endsWith("/")){
            type="folder";
            String temp = filePath.substring(0, filePath.length() - 1);
            int startIndex=temp.indexOf("/");
            if (startIndex==-1){
                name=temp;//根文件
                root=true;
            }else{
                int endIndex=temp.lastIndexOf("/");
                if (startIndex==endIndex){
                    name=temp.substring(startIndex+1);
                    root=false;
                }else {
                    name=temp.substring(endIndex+1);
                    root=false;
                }
            }
        }else {
            String lowerCase = filePath.toLowerCase();
            if (lowerCase.endsWith(".lrc")){
                type="lrc";
            }else {
                type="unknown";
            }
            int startIndex=filePath.lastIndexOf("/");
            if (startIndex !=-1){
                name=filePath.substring(startIndex+1);
                root=false;
            }else {
                name=filePath;
                root=true;
            }
        }




    }

    public boolean isFolder() {
        return type.equals("folder");
    }

    public boolean isLrc() {
        return type.equals("lrc");
    }
    public String getParentPath() {
        if (root)
            return path;
        else {
            if (isFolder()){//目录以"/"结尾
                String temp = path.substring(0, path.length() - 2);
                return  temp.substring(0,temp.lastIndexOf("/")+1);
            }else {
                return  path.substring(0,path.lastIndexOf("/")+1);
            }


        }
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ZipEntityFile> getChildren() {
        return children;
    }

    public void setChildren(List<ZipEntityFile> children) {
        this.children = children;
    }
}