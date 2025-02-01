package cn.tealc995.kikoreu.api;

import cn.tealc995.kikoreu.NewHttpClient;
import cn.tealc995.kikoreu.model.MainWorks;
import cn.tealc995.kikoreu.model.Response;
import cn.tealc995.kikoreu.model.ResponseBody;
import cn.tealc995.kikoreu.model.playList.MainPlayList;
import cn.tealc995.kikoreu.model.playList.PlayListAlter;
import cn.tealc995.kikoreu.model.playList.PlayListCreate;
import cn.tealc995.kikoreu.model.playList.PlayListRemoveWork;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @program: Asmr-Online
 * @description: 播放列表API，使用前必须已经设置好Token
 * @author: Leck
 * @create: 2023-07-12 19:35
 */
public class PlayListApi extends BaseApi {
    private static final Logger LOG = LoggerFactory.getLogger(PlayListApi.class);

    public PlayListApi(NewHttpClient httpClient) {
        super(httpClient);
    }

    public ResponseBody<MainPlayList> playList(Map<String, String> params) {
        Response response = httpClient.get("/api/playlist/get-playlists", params);
        if (response.isSuccess()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                MainPlayList mainPlayList = mapper.readValue(response.getMessage(), MainPlayList.class);
                return ResponseBody.create(response.getCode(), response.getMessage(), mainPlayList);
            } catch (JsonProcessingException e) {
                LOG.error(e.getMessage());
                return ResponseBody.create(-1, "请求内容JSON解析失败", null);
            }
        } else {
            return ResponseBody.create(response.getCode(), response.getMessage(), null);
        }
    }


    public ResponseBody<MainWorks> works(Map<String, String> params) {
        Response response = httpClient.get("/api/playlist/get-playlist-works", params);
        if (response.isSuccess()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                MainWorks mainWorks = mapper.readValue(response.getMessage(), MainWorks.class);
                return ResponseBody.create(response.getCode(), response.getMessage(), mainWorks);
            } catch (JsonProcessingException e) {
                return ResponseBody.create(-1, "请求内容JSON解析失败", null);
            }
        } else {
            return ResponseBody.create(response.getCode(), response.getMessage(), null);
        }
    }

    public ResponseBody<Boolean> remove(PlayListRemoveWork work) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(work);
            Response response = httpClient.post("/api/playlist/remove-works-from-playlist", json);
            return ResponseBody.create(response.getCode(), response.getMessage(), response.isSuccess());
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
            return ResponseBody.create(-1, "请求内容JSON解析失败", null);
        }
    }

    public ResponseBody<Boolean> add(PlayListRemoveWork work) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(work);
            Response response = httpClient.post("/api/playlist/add-works-to-playlist", json);
            return ResponseBody.create(response.getCode(), response.getMessage(), response.isSuccess());
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
            return ResponseBody.create(-1, "请求内容JSON解析失败", null);
        }
    }


    public ResponseBody<MainPlayList> workExistInPlayList(Map<String, String> params) {
        Response response = httpClient.get("/api/playlist/get-work-exist-status-in-my-playlists", params);
        if (response.isSuccess()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                MainPlayList mainPlayList = mapper.readValue(response.getMessage(), MainPlayList.class);
                return ResponseBody.create(response.getCode(), response.getMessage(), mainPlayList);
            } catch (JsonProcessingException e) {
                LOG.error(e.getMessage());
                return ResponseBody.create(-1, "请求内容JSON解析失败", null);
            }
        } else {
            return ResponseBody.create(response.getCode(), response.getMessage(), null);
        }
    }


    public ResponseBody<Boolean> alter(PlayListAlter param) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(param);
            Response response = httpClient.post("/api/playlist/edit-playlist-metadata", json);
            return ResponseBody.create(response.getCode(), response.getMessage(), response.isSuccess());
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
            return ResponseBody.create(-1, "请求内容JSON解析失败", null);
        }
    }

    public ResponseBody<Boolean> create(PlayListCreate param) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(param);
            Response response = httpClient.post("/api/playlist/create-playlist", json);
            return ResponseBody.create(response.getCode(), response.getMessage(), response.isSuccess());
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
            return ResponseBody.create(-1, "请求内容JSON解析失败", null);
        }
    }


    public ResponseBody<Boolean> delete(String id) {
        String json = String.format("{\"id\":\"%s\"}", id);
        Response response = httpClient.post("/api/playlist/delete-playlist", json);
        return ResponseBody.create(response.getCode(), response.getMessage(), response.isSuccess());
    }


}