package cn.tealc995.kikoreu.api;

import cn.tealc995.kikoreu.HttpUtils;
import cn.tealc995.kikoreu.NewHttpClient;
import cn.tealc995.kikoreu.model.MainWorks;
import cn.tealc995.kikoreu.model.Response;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainNotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-14 23:12
 */
public class StarApi extends BaseApi{

    public StarApi(NewHttpClient httpClient) {
        super(httpClient);
    }

    public ResponseBody<MainWorks> star(Map<String,String> params){
        Response response = httpClient.get("/api/review", params);
        if (response.isSuccess()){
            ObjectMapper mapper=new ObjectMapper();
            try {
                MainWorks mainWorks = mapper.readValue(response.getMessage(), MainWorks.class);
                return ResponseBody.create(response.getCode(),response.getMessage(),mainWorks);
            } catch (JsonProcessingException e) {
                return ResponseBody.create(-1,"请求内容JSON解析失败",null);
            }
        } else {
            return ResponseBody.create(response.getCode(),response.getMessage(),null);
        }
    }

    public ResponseBody<Boolean> updateStar(String workId,Integer rating){
        String row=String.format("{\"work_id\":%s,\"rating\":%d}",workId,rating);
        Response response = httpClient.put( "/api/review", row);
        if (response.isSuccess()){
            return ResponseBody.create(response.getCode(),response.getMessage(),true);
        } else {
            return ResponseBody.create(response.getCode(),response.getMessage(),false);
        }
    }

    public ResponseBody<Boolean> deleteStar(String workId){
        Response response  = httpClient.delete("/api/review?work_id="+workId);
        if (response.isSuccess()){
            return ResponseBody.create(response.getCode(),response.getMessage(),true);
        } else {
            return ResponseBody.create(response.getCode(),response.getMessage(),false);
        }
    }
}