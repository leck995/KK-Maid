package cn.tealc995.asmronline.ui.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @program: AmsrPlayer
 * @description:
 * @author: Leck
 * @create: 2023-01-03 14:16
 */
public class PathViewPane extends HBox {
    private ObservableList<String> items;
    private String currentPath;
    private boolean isReplace;

    private SimpleStringProperty onAction; //onAction可能值不变但需要更新，古添加一个字符串same，来通知值不变更新

    public PathViewPane() {
        isReplace=false;
        items= FXCollections.observableArrayList();
        onAction=new SimpleStringProperty();
        items.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                while (change.next()) {
                    if (isReplace){//当新旧路径无关系时，根据button数量进行重设text，删除添加。
                        if (change.wasAdded()){
                            int childSize=getChildren().size();
                            int changSize=change.getAddedSubList().size();
                            Button button;
                            if (changSize<= childSize){//新路径节点数量小于等于旧路径节点数量是处理
                                for (int i = 0; i < changSize; i++) {//现有的button重命名
                                    button= (Button) getChildren().get(i);
                                    button.setText(change.getAddedSubList().get(i));
                                }
                                getChildren().remove(changSize,childSize);//多余的button删除
                            }else{//新路径节点数量大于等于旧路径节点数量是处理
                                for (int i = 0; i < changSize; i++) {
                                    if (i < childSize){//现有的button重命名
                                        button= (Button) getChildren().get(i);
                                        button.setText(change.getAddedSubList().get(i));
                                    }else {
                                        getChildren().add(createItem(change.getAddedSubList().get(i)));//缺少的button添加
                                    }
                                }
                            }
                            isReplace=false;
                            return;
                        }
                    }else{//当新旧路径存在关系时，父目录或者子目录等关系
                        if (change.wasAdded()){//新路径是旧路径下的
                            change.getAddedSubList().forEach(add ->getChildren().add(createItem(add)));
                        }

                        if (change.wasRemoved()){//旧路径是新路径下的
                            change.getRemoved().forEach(a -> System.out.println("删除的："+a));
                            items.forEach(a -> System.out.println("现在的："+a));
                            int childSize=getChildren().size();
                            getChildren().remove(childSize-change.getRemoved().size(),childSize);
                        }
                    }
                }

                //让最后一个button不允许点击
                int size= getChildren().size();
                if (size > 1){
                    getChildren().get(size-1).setDisable(true);
                    getChildren().get(size-2).setDisable(false);
                }else {
                    getChildren().get(size-1).setDisable(true);
                }

            }
        });
    }


    /**
     * @description: 设置路径，对路径进行分析，核心方法
     * @author: Leck
     * @name: setPath
     * @param:	path
     * @return  void
     * @date:   2023/1/4
     */
    public void setPath(String path){
        if (currentPath==null){
            currentPath=path;
            items.addAll(splitPathToList(path));
        }else {
            if (path.equals(currentPath)) return;//路径相同，不处理
            if (path.startsWith(currentPath)){//新路径是旧路径的子路径
                String temp=path.substring(currentPath.length()+1);
                items.addAll(splitPathToList(temp));
                currentPath=path;
                return;
            }
            if (currentPath.startsWith(path)){////新目录是旧路径的父路径
                String temp=currentPath.substring(path.length()+1);
                items.removeAll(splitPathToList(temp));
                currentPath=path;
                return;
            }
            //要更新的目录和旧目录不相同
            isReplace=true;
            items.clear();
            items.addAll(splitPathToList(path));
            currentPath=path;
        }

    }

    /**
     * @description: 生成Button
     * @author: Leck
     * @name: createItem
     * @param:	name	Button名称
     * @return  javafx.scene.control.Button
     * @date:   2023/1/4
     */
    private Handler handler=new Handler();
    private Button createItem(String name){
        Button button=new Button(name);
        button.setMaxWidth(300.0);
        //String s="-fx-shape:'"+paintSVG(name.length() * 15)+"'";
       // button.setStyle(s);
        button.setGraphic(new Region());
        button.setGraphicTextGap(10.0);
        button.setOnAction(handler);
        return button;
    }

    /**
     * @description: 绘制SVG路径
     * @author: Leck
     * @name: paintSVG
     * @param:	width
     * @return  java.lang.String
     * @date:   2023/1/4
     */
    private String paintSVG(int width){
        if (width>300)width=280;
        if (width<20) width=20;
        StringBuilder stringBuilder=new StringBuilder("M 0 0 L ")
                .append(width).append(" 0 L ")
                .append(width+5).append(" 15 L ")
                .append(width).append(" 30 L ")
                .append("0").append(" 30 L ")
                .append("5 15 Z");
        return stringBuilder.toString();
    }

    /**
     * @description: 将path路径根据分隔符分成list
     * @author: Leck
     * @name: splitPathToList
     * @param:	path
     * @return  java.util.List
     * @date:   2023/1/4
     */
    private List splitPathToList(String path){
        String[] split = path.split(Pattern.quote(File.separator));
        return  Arrays.stream(split).toList();
    }

    class Handler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent actionEvent) {
            Button button= (Button) actionEvent.getSource();
            String s=currentPath.substring(0,currentPath.indexOf(button.getText())+button.getText().length());
            if (onAction.get()!=null&& onAction.get().equals(s)){
                onAction.set(null);
                onAction.set(s);
                currentPath=s;
            }else{
                currentPath=s;
                onAction.set(s);
            }


            if (items.indexOf(button.getText()) != getChildren().size()){
                getChildren().remove(items.indexOf(button.getText())+1, getChildren().size());
            }
        }
    }

    public String getOnActionProperty() {
        return onAction.get();
    }

    public SimpleStringProperty onActionProperty() {
        return onAction;
    }
}