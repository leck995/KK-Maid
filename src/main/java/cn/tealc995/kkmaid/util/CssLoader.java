package cn.tealc995.kkmaid.util;


import cn.tealc995.kkmaid.App;

/**
 * @program: AsmrPlayer
 * @description:
 * @author: Leck
 * @create: 2023-04-07 21:39
 */
public class CssLoader {
    public static final String baseUI="/BaseUI.css";
    public static final String colors="/colors.css";
    public static final String main="/main.css";
    public static final String setting="/setting.css";
    public static final String mainGrid="/main-grid.css";
    public static final String category="/category.css";
    public static final String detail="/detail.css";
    public static final String download="/download.css";

    public static final String simple_player="/simple-player.css";

    public static final String player="/player-detail.css";
    public static final String playing_list="/item/playing-list.css";

    public static final String lrc_stage="/item/lrc-stage.css";

    public static final String lrc_zip_dialog="/item/lrc-zip-dialog.css";
    public static final String lrc_file_dialog="/item/lrc-file-dialog.css";
    public static final String volume_popup="/item/volume-popup.css";
    public static String getCss(String cssName){
        return App.class.getResource("/cn/tealc995/kkmaid/css" +cssName).toExternalForm();
    }

}