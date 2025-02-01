package cn.tealc995.kikoreu.api;

import cn.tealc995.kikoreu.HttpUtils;
import cn.tealc995.kikoreu.NewHttpClient;
import cn.tealc995.kikoreu.model.Response;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.Track;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainNotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @description: 获取指定作品的文件列表
 * @author: Leck
 * @create: 2023-07-13 21:06
 */
public class TrackApi extends BaseApi{
    public TrackApi(NewHttpClient httpClient) {
        super(httpClient);
    }

    public ResponseBody<List<Track>> track(String key){
        Response response = httpClient.get("/api/tracks/" + key);
        if (response.isSuccess()){
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<Track> tracks = mapper.readValue(response.getMessage(), new TypeReference<>(){});
                return ResponseBody.create(response.getCode(), response.getMessage(), tracks);
            } catch (JsonProcessingException e) {
                return ResponseBody.create(-1,"请求内容JSON解析失败",null);
            }
        }else {
            return ResponseBody.create(response.getCode(), response.getMessage(),null);
        }
    }
}