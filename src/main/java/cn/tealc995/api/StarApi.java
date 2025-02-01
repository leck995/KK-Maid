package cn.tealc995.api;

import cn.tealc995.api.model.MainWorks;
import cn.tealc995.api.model.Response;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainNotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-14 23:12
 */
public class StarApi {

    public static MainWorks star(String url,Map<String,String> params){
        Response response = HttpUtils.get(url + "/api/review", params);
        if (response.isSuccess()){
            ObjectMapper mapper=new ObjectMapper();
            try {
                MainWorks mainWorks = mapper.readValue(response.getMessage(), MainWorks.class);
                return mainWorks;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
            return null;
        }
    }

    public static boolean updateStar(String url,String workid,Integer rating){
        String row=String.format("{\"work_id\":%s,\"rating\":%d}",workid,rating);
        Response resp = HttpUtils.put(url + "/api/review", row);
        if (!resp.isSuccess()){
            EventBusUtil.getDefault().post(new MainNotificationEvent(resp.getMessage()));
        }
        return resp.isSuccess();
    }

    public static boolean deleteStar(String url,String workid){
        Response resp  = HttpUtils.delete(url+"/api/review?work_id="+workid);
        if (!resp.isSuccess()){
            EventBusUtil.getDefault().post(new MainNotificationEvent(resp.getMessage()));
        }
        return resp.isSuccess();
    }
}