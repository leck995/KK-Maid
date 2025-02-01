package cn.tealc995.kkmaid.api;

import cn.tealc995.kkmaid.api.model.MainWorks;
import cn.tealc995.kkmaid.api.model.Response;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainNotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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