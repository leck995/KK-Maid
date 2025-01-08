package cn.tealc995.asmronline.ui.component;


import cn.tealc995.asmronline.model.lrc.LrcBean;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

/**
 * @ClassName LrcView
 * @Description: TODO
 * @Author Leck
 * @Date 2022/2/20
 * @Version V1.0
 **/
public class LrcView<T> extends ListView<LrcBean> {
    /**
     * 播放时间(进度)
     */
    private SimpleDoubleProperty currentTime;
    private ObjectProperty<T> playingValue;
    private ObjectProperty<T> nextValue;

    private SimpleBooleanProperty showTrans;
    public LrcView(SimpleDoubleProperty currentTime) {

        this.currentTime = currentTime;
        showTrans=new SimpleBooleanProperty(true);
        Label placeholder=new Label("暂无歌词");
        placeholder.setFont(Font.font(24));
        setPlaceholder(placeholder);

        setCellFactory(lrcBeanListView -> {
/*            LrcListCell cell = new LrcListCell();
            //cell.setAlignment(Pos.CENTER);
            //cell.setWrapText(true);
            cell.prefWidthProperty().bind(widthProperty().multiply(0.65));
              return cell;*/

            ListCell<LrcBean> cell=new ListCell<>(){
                @Override
                protected void updateItem(LrcBean lrcBean, boolean b) {
                    super.updateItem(lrcBean, b);
                    if (lrcBean != null){
                        setText(lrcBean.getRowText());
                      /*  Label label=new Label(lrcBean.getRowText());
                        label.setWrapText(true);
                        setGraphic(label);*/
                    }else {
                       // setGraphic(null);
                        setText("");
                    }
                }
            };
            cell.setPrefWidth(300);
            cell.setWrapText(true);
            cell.prefWidthProperty().bind(widthProperty().multiply(0.65));
            return cell;

        });




        currentTime.addListener((observableValue, number, t1) -> {
            if (t1==null){
                return;
            }
            if (getItems()!=null &getItems().size()>0){
                long millis=t1.longValue()*1000;
                int selectIndex=getSelectionModel().getSelectedIndex();
                if (millis < getItems().get(0).getLongTime()) {
                    if (selectIndex!=0){
                        getSelectionModel().selectFirst();
                        scrollTo(0);
                        //scrollTo(0,false);

                        return;
                    }
                }
                int size=getItems().size()-1;
                for (int i = 0; i < size; i++) {
                    if (millis > getItems().get(i).getLongTime() && millis < getItems().get(i+1).getLongTime()){
                        if (selectIndex!=i){
                            getSelectionModel().select(i);
                            scrollTo(i,false);
                            return;
                        }
                    }
                }
                if (millis > getItems().get(size).getLongTime()){
                    if (selectIndex!=size){
                        getSelectionModel().select(size);
                        scrollTo(size,false);
                    }
                }
            }
        });


        //屏蔽所有事件
        addEventFilter(MouseEvent.ANY, click ->{
            click.consume();
        });



    }

    /**
     * 歌词地址
     * */
    private SimpleStringProperty lrcFilePath;



    /**
     * @Description: 跳转listview到指定行,目前找到我能接收的方法判断是否有翻译，故默认hasTrans没有用
     * @MethodName: scrollTo
     * @param index: 歌词所在的index
     * @param hasTrans: 是否有翻译
     * @Return: void
     * @Author: Leck
     * @Date: 2022/2/15
     */
    private void scrollTo(int index,boolean hasTrans){
        if (getItems().size() == 0 || getChildren().size() == 0) return;
        VirtualFlow<ListCell<LrcBean>> virtualFlow= (VirtualFlow<ListCell<LrcBean>>) getChildren().get(0);
        if (virtualFlow.getLastVisibleCell() != null){
            int i = index-(int) ((virtualFlow.getLastVisibleCell().getIndex() - virtualFlow.getFirstVisibleCell().getIndex()) * 0.5)+1;
            scrollTo(Math.max(i, 0));
            //virtualFlow.scrollTo(Math.max(i, 0));
        }

    }






    public  String getLrcFilePath() {
        return lrcFilePath.get();
    }

    public  SimpleStringProperty lrcFilePathProperty() {
        return lrcFilePath;
    }

    public  void setLrcFilePath(String lrcFilePath) {
        this.lrcFilePath.set(lrcFilePath);
    }


    public boolean isShowTrans() {
        return showTrans.get();
    }

    public SimpleBooleanProperty showTransProperty() {
        return showTrans;
    }

    public void setShowTrans(boolean showTrans) {
        this.showTrans.set(showTrans);
    }
}
