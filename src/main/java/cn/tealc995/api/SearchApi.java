package cn.tealc995.api;

import cn.tealc995.api.model.MainWorks;
import cn.tealc995.api.model.Response;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainNotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 06:07
 */
public class SearchApi {
    public static MainWorks search(String url, String key,Map<String,String> params){
        Response response = HttpUtils.get(url+"/api/search/"+ URLEncoder.encode(key, StandardCharsets.UTF_8),params);
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