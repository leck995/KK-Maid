package cn.tealc995.kikoreu.api;

import cn.tealc995.kikoreu.HttpUtils;
import cn.tealc995.kikoreu.NewHttpClient;
import cn.tealc995.kikoreu.model.MainWorks;
import cn.tealc995.kikoreu.model.Response;
import cn.tealc995.kikoreu.model.ResponseBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-12 19:35
 */
public class WorksApi extends BaseApi{
    public WorksApi(NewHttpClient httpClient) {
        super(httpClient);
    }

    public ResponseBody<MainWorks> works(Map<String,String> params){
        Response response = httpClient.get("/api/works", params);
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