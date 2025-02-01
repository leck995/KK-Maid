package cn.tealc995.kkmaid.api;

import cn.tealc995.kkmaid.api.model.Response;
import cn.tealc995.kkmaid.api.model.Track;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainNotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

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