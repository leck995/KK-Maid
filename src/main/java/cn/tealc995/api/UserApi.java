package cn.tealc995.api;

import cn.tealc995.api.model.Response;
import cn.tealc995.api.model.User;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainNotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-19 08:59
 */
public class UserApi {
    public static User checkLogin(String url){
        Response response = HttpUtils.get(url + "/api/auth/me");
        if (response.isSuccess()){
            ObjectMapper mapper=new ObjectMapper();
            try {
                JsonNode jsonNode = mapper.readTree(response.getMessage());
                User user = mapper.convertValue(jsonNode.get("user"), User.class);
                return user;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }else {
            EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
            return null;
        }
    }

    /**
     * @description: 获取Token
     * @name: login
     * @author: Leck
     * @param:	url
     * @param:	username
     * @param:	password
     * @return  java.lang.String
     * @date:   2023/7/19
     */
    public static String login(String url,String username,String password){
        String row=String.format("{\"name\":\"%s\",\"password\":\"%s\"}",username,password);
        Response response = HttpUtils.post(url + "/api/auth/me",row);
        if (response.isSuccess()){
            ObjectMapper mapper=new ObjectMapper();
            try {
                JsonNode jsonNode = mapper.readTree(response.getMessage());
                return jsonNode.get("token").asText();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }else {
            //System.out.println(response.getMessage());
            EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
            return null;
        }
    }
}