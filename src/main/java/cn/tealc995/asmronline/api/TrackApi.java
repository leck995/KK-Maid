package cn.tealc995.asmronline.api;

import cn.tealc995.asmronline.api.model.MainWorks;
import cn.tealc995.asmronline.api.model.Response;
import cn.tealc995.asmronline.api.model.Track;
import cn.tealc995.asmronline.event.EventBusUtil;
import cn.tealc995.asmronline.event.MainNotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 21:06
 */
public class TrackApi {
    public static List<Track> track(String url, String key){
        Response response = HttpUtils.get(url + "/api/tracks/" + key);
        if (response.isSuccess()){
            ObjectMapper mapper=new ObjectMapper();
            try {
                List<Track> tracks = mapper.readValue(response.getMessage(), new TypeReference<>(){});
                return tracks;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }else {
            EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
            return null;
        }


    }
}