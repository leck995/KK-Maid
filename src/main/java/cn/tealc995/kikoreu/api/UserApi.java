package cn.tealc995.kikoreu.api;

import cn.tealc995.kikoreu.NewHttpClient;
import cn.tealc995.kikoreu.model.Response;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.User;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainNotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-19 08:59
 */
public class UserApi extends BaseApi {
    public UserApi(NewHttpClient httpClient) {
        super(httpClient);
    }

    public ResponseBody<User> checkLogin() {
        Response response = httpClient.get("/api/auth/me");
        if (response.isSuccess()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode jsonNode = mapper.readTree(response.getMessage());
                User user = mapper.convertValue(jsonNode.get("user"), User.class);
                return ResponseBody.create(response.getCode(), response.getMessage(),user);
            } catch (JsonProcessingException e) {
                return ResponseBody.create(-1,"请求内容JSON解析失败",null);
            }
        } else {
            return ResponseBody.create(response.getCode(), response.getMessage(),null);
        }
    }

    /**
     * @return ResponseBody<String>
     * @description: 获取Token
     * @name: login
     * @author: Leck
     * @param: url
     * @param: username
     * @param: password
     * @date: 2023/7/19
     */
    public ResponseBody<String> login(String username, String password) {
        String row = String.format("{\"name\":\"%s\",\"password\":\"%s\"}", username, password);
        Response response = httpClient.post("/api/auth/me", row);
        if (response.isSuccess()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode jsonNode = mapper.readTree(response.getMessage());
                return ResponseBody.create(response.getCode(), response.getMessage(),jsonNode.get("token").asText());
            } catch (JsonProcessingException e) {
                return ResponseBody.create(-1,"请求内容JSON解析失败",null);
            }
        } else {
            return ResponseBody.create(response.getCode(), response.getMessage(),null);
        }
    }
}