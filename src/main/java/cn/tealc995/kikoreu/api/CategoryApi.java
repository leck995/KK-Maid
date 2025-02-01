package cn.tealc995.kikoreu.api;

import cn.tealc995.kikoreu.NewHttpClient;
import cn.tealc995.kikoreu.model.Response;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.RoleEx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-13 07:00
 */
public class CategoryApi extends BaseApi {
    public CategoryApi(NewHttpClient httpClient) {
        super(httpClient);
    }

    public ResponseBody<List<RoleEx>> circle() {
        Response response = httpClient.get("/api/circles/");
        if (response.isSuccess()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<RoleEx> circles = mapper.readValue(response.getMessage(), new TypeReference<>() {
                });
                return ResponseBody.create(response.getCode(), response.getMessage(), circles);
            } catch (JsonProcessingException e) {
                return ResponseBody.create(-1, "请求内容JSON解析失败", null);
            }
        } else {
            return ResponseBody.create(response.getCode(), response.getMessage(), null);
        }

    }

    public ResponseBody<List<RoleEx>> tag() {
        Response response = httpClient.get("/api/tags/");
        if (response.isSuccess()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<RoleEx> circles = mapper.readValue(response.getMessage(), new TypeReference<>() {
                });
                return ResponseBody.create(response.getCode(), response.getMessage(), circles);
            } catch (JsonProcessingException e) {
                return ResponseBody.create(-1, "请求内容JSON解析失败", null);
            }
        } else {
            return ResponseBody.create(response.getCode(), response.getMessage(), null);
        }

    }

    public ResponseBody<List<RoleEx>> va() {
        Response response = httpClient.get("/api/vas/");
        if (response.isSuccess()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<RoleEx> circles = mapper.readValue(response.getMessage(), new TypeReference<>() {
                });
                return ResponseBody.create(response.getCode(), response.getMessage(), circles);
            } catch (JsonProcessingException e) {
                return ResponseBody.create(-1, "请求内容JSON解析失败", null);
            }
        } else {
            return ResponseBody.create(response.getCode(), response.getMessage(), null);
        }

    }
}