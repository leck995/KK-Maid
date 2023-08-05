package cn.tealc995.asmronline.api;

import cn.tealc995.asmronline.api.model.MainWorks;
import cn.tealc995.asmronline.api.model.Response;
import cn.tealc995.asmronline.api.model.Track;
import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.MainNotificationEvent;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Map;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 19:35
 */
public class WorksApi {
    public static MainWorks works(String url,Map<String,String> params){
        Response response = HttpUtils.get(url + "/api/works", params);
        if (response.isSuccess()){
            ObjectMapper mapper=new ObjectMapper();
            try {
                MainWorks mainWorks = mapper.readValue(response.getMessage(), MainWorks.class);
                return mainWorks;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }else {
            EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
            return null;
        }
    }

}