package cn.tealc995.api;

import cn.tealc995.api.model.Response;
import cn.tealc995.api.model.RoleEx;
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
 * @create: 2023-07-13 07:00
 */
public class CategoryApi {
    public static List<RoleEx> circle(String url){
        Response response = HttpUtils.get(url + "/api/circles/");

        if (response.isSuccess()){
            ObjectMapper mapper=new ObjectMapper();
            try {
                List<RoleEx> circles = mapper.readValue(response.getMessage(), new TypeReference<>() {});
                return circles;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }else {
            EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
            return null;
        }

    }

    public static List<RoleEx> tag(String url){
        Response response = HttpUtils.get(url + "/api/tags/");
        if (response.isSuccess()){
            ObjectMapper mapper=new ObjectMapper();
            try {
                List<RoleEx> circles = mapper.readValue(response.getMessage(), new TypeReference<>() {});
                return circles;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }else {
            EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
            return null;
        }

    }

    public static List<RoleEx> va(String url){
        Response response = HttpUtils.get(url+"/api/vas/");
        if (response.isSuccess()){
            ObjectMapper mapper=new ObjectMapper();
            try {
                List<RoleEx> circles = mapper.readValue(response.getMessage(), new TypeReference<>() {});
                return circles;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }else {
            EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
            return null;
        }

    }
}