package cn.tealc995.kkmaid.ui;

import cn.tealc995.aria2.Aria2Client;
import cn.tealc995.aria2.Aria2Method;
import cn.tealc995.aria2.model.Aria2Option;
import cn.tealc995.aria2.model.Aria2Request;
import cn.tealc995.kkmaid.config.Config;
import cn.tealc995.kikoreu.model.Response;
import cn.tealc995.kikoreu.model.Track;
import cn.tealc995.kikoreu.model.Work;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-09-13 04:01
 */
public class DownloadViewModel {
    private static final String HOST="%s";
    private static final String TOKEN="token:%s";
    private static final String WORK_DIR="%s/RJ%s/%s";
    private static final String REFERER="*";
    private List<Track> successList;
    private List<Track> failList;
    private List<Response> responses;
    private Work work;
    public DownloadViewModel(Work work, Track rootTrack) {
        successList=new ArrayList<>();
        failList=new ArrayList<>();
        responses=new ArrayList<>();
    }

    public void download(Work work,Set<Track> set){
        successList.clear();
        failList.clear();
        responses.clear();

        ObjectMapper mapper=new ObjectMapper();
        for (Track track : set) {
            Aria2Option aria2Option=new Aria2Option(String.format(WORK_DIR,Config.setting.getDownloadDir(),work.getFullId(),track.getPath()),REFERER);
            String[] arr=new String[]{track.getMediaDownloadUrl()};
            List<Object> params= new ArrayList<>();
            params.add(String.format(TOKEN,Config.setting.getAriaRPCKey()));
            params.add(arr);
            params.add(aria2Option);
            Aria2Request request=new Aria2Request(UUID.randomUUID().toString(), Aria2Method.ADD_URI.getValue(), params);
            try {
                mapper.writeValueAsString(request);
                Response post = Aria2Client.post(String.format(HOST,Config.setting.getAria2Host()), mapper.writeValueAsString(request));
                if (post.isSuccess()){
                    successList.add(track);
                }else {
                    failList.add(track);
                    responses.add(post);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Track> getSuccessList() {
        return successList;
    }

    public List<Track> getFailList() {
        return failList;
    }

    public List<Response> getResponses() {
        return responses;
    }
}