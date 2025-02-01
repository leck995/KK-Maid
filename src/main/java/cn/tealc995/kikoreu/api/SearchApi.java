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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 06:07
 */
public class SearchApi extends BaseApi{
    public SearchApi(NewHttpClient httpClient) {
        super(httpClient);
    }

    public ResponseBody<MainWorks> search(String key, Map<String,String> params){
        Response response = httpClient.get("/api/search/"+ URLEncoder.encode(key, StandardCharsets.UTF_8),params);
        if (response.isSuccess()){
            ObjectMapper mapper=new ObjectMapper();
            try {
                MainWorks mainWorks = mapper.readValue(response.getMessage(), MainWorks.class);
                return ResponseBody.create(response.getCode(),response.getMessage(),mainWorks);
            } catch (JsonProcessingException e) {
                return ResponseBody.create(-1,"请求内容JSON解析失败",null);
            }
        }else {
            return ResponseBody.create(response.getCode(),response.getMessage(),null);
        }


    }





}