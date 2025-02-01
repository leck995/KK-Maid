package cn.tealc995.kkmaid.api;

import cn.tealc995.kkmaid.api.model.playList.MainPlayList;
import cn.tealc995.kkmaid.api.model.MainWorks;
import cn.tealc995.kkmaid.api.model.Response;
import cn.tealc995.kkmaid.api.model.playList.PlayListAlter;
import cn.tealc995.kkmaid.api.model.playList.PlayListCreate;
import cn.tealc995.kkmaid.api.model.playList.PlayListRemoveWork;
import cn.tealc995.kkmaid.event.EventBusUtil;
import cn.tealc995.kkmaid.event.MainNotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 19:35
 */
public class PlayListApi {
    private static final Logger logger = LoggerFactory.getLogger(PlayListApi.class);
    public static MainPlayList playList(String url,Map<String,String> params){
        Response response = HttpUtils.get(url +"/api/playlist/get-playlists", params);
        if (response.isSuccess()){
            ObjectMapper mapper=new ObjectMapper();
            try {
                MainPlayList mainPlayList = mapper.readValue(response.getMessage(), MainPlayList.class);
                return mainPlayList;
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }else {
            EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
            return null;
        }
    }



    public static MainWorks works(String url,Map<String,String> params){
        Response response = HttpUtils.get(url + "/api/playlist/get-playlist-works", params);
        if (response.isSuccess()){
            ObjectMapper mapper=new ObjectMapper();
            try {
                MainWorks mainWorks = mapper.readValue(response.getMessage(), MainWorks.class);
                return mainWorks;
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }else {
            EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
            return null;
        }
    }

    public static boolean remove(String url, PlayListRemoveWork work){
        ObjectMapper mapper=new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(work);
            Response response = HttpUtils.post(url +"/api/playlist/remove-works-from-playlist", json);
            if (response.isSuccess()){
                return true;
            }else {
                EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
                return false;
            }
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static boolean add(String url, PlayListRemoveWork work){
        ObjectMapper mapper=new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(work);
            Response response = HttpUtils.post(url +"/api/playlist/add-works-to-playlist", json);
            if (response.isSuccess()){
                return true;
            }else {
                EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
                return false;
            }
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public static MainPlayList workExistInPlayList(String url,Map<String,String> params){
        Response response = HttpUtils.get(url + "/api/playlist/get-work-exist-status-in-my-playlists", params);
        if (response.isSuccess()){
            ObjectMapper mapper=new ObjectMapper();
            try {
                MainPlayList mainPlayList = mapper.readValue(response.getMessage(), MainPlayList.class);
                return mainPlayList;
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }else {
            EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
            return null;
        }
    }


    public static boolean alter(String url, PlayListAlter param){
        ObjectMapper mapper=new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(param);
            Response response = HttpUtils.post(url +"/api/playlist/edit-playlist-metadata", json);
            if (response.isSuccess()){
                return true;
            }else {
                EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
                return false;
            }
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static boolean create(String url, PlayListCreate param){
        ObjectMapper mapper=new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(param);
            Response response = HttpUtils.post(url +"/api/playlist/create-playlist", json);
            if (response.isSuccess()){
                return true;
            }else {
                EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
                return false;
            }
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }



    public static boolean delete(String url, String id){
        String json = String.format("{\"id\":\"%s\"}",id);
        Response response = HttpUtils.post(url +"/api/playlist/delete-playlist", json);
        if (response.isSuccess()){
            return true;
        }else {
            EventBusUtil.getDefault().post(new MainNotificationEvent(response.getMessage()));
            return false;
        }
    }


}