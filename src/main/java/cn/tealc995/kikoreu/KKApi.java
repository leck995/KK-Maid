package cn.tealc995.kikoreu;

import cn.tealc995.kikoreu.api.*;

/**
 * @program: KK-Maid
 * @description:
 * @author: Leck
 * @create: 2025-02-01 22:33
 */
public class KKApi {
    private static KKApi instance;
    private NewHttpClient client;
    private String token;
    private String host;
    private KKApi() {
        client = new NewHttpClient();
    }

    public static KKApi getInstance() {
        if (instance == null) {
            instance = new KKApi();
        }
        return instance;
    }

    public void setToken(String token) {
        this.token = token;
        client.setToken(token);
    }

    public void setHost(String host) {
        this.host = host;
        client.setHost(host);
    }




    public WorksApi worksApi(){
        return new WorksApi(client);
    }

    public UserApi userApi(){
        return new UserApi(client);
    }

    public TrackApi trackApi(){
        return new TrackApi(client);
    }

    public StarApi starApi(){
        return new StarApi(client);
    }
    public SearchApi searchApi(){
        return new SearchApi(client);
    }
    public PlayListApi playListApi(){
        return new PlayListApi(client);
    }
    public CategoryApi categoryApi(){
        return new CategoryApi(client);
    }

    public NewHttpClient getHttpClient() {
        return client;
    }
}