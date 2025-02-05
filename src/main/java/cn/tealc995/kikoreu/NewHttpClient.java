package cn.tealc995.kikoreu;


import cn.tealc995.kikoreu.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

/**
 * @description: 负责处理Http请求
 * @author: Leck
 * @create: 2023-07-12 19:51
 */
public class NewHttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(NewHttpClient.class);
    private final HttpClient client = HttpClient.newHttpClient();
    private String token = null;
    private String host = null;

    public NewHttpClient() {
    }


    /**
     * @return boolean
     * @description: 判断是否已经准备好了
     * @param:
     * @date: 2025/2/1
     */
    public boolean ready() {
        return token != null && host != null;
    }

    /**
     * @description: 重新设置Token，使用前必须设置一次
     * @param: token
     * @date: 2025/2/1
     */
    public void setToken(String token) {
        if (token != null && token.endsWith("/")) {
            token = token.substring(0, token.length() - 1);
        }
        this.token = token;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Response get(String path, Map<String, String> params) {
        HttpRequest request = initRequest(host + path + "?" + mapToParamStr(params))
                .GET()
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Response(httpResponse.statusCode(), httpResponse.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return new Response(-1, e.getMessage());
        }
    }

    public Response get(String path, String param) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(host + path + "?" + param))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json; charset=utf-8")
                .header("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Response(httpResponse.statusCode(), httpResponse.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return new Response(-1, e.getMessage());
        }
    }

    public Response get(String path) {
        HttpRequest request = initRequest(host + path)
                .GET()
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Response(httpResponse.statusCode(), httpResponse.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return new Response(-1, e.getMessage());
        }
    }

    public Response put(String path, String json) {
        HttpRequest request = initRequest(host + path)
                .PUT(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Response(httpResponse.statusCode(), httpResponse.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return new Response(-1, e.getMessage());
        }
    }

    public Response delete(String path) {
        HttpRequest request = initRequest(host + path)
                .DELETE()
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Response(httpResponse.statusCode(), httpResponse.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return new Response(-1, e.getMessage());
        }
    }


    public Response download(String path) {
        HttpRequest request = initRequest(host + path)
                .GET()
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Response(httpResponse.statusCode(), httpResponse.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return new Response(-1, e.getMessage());
        }
    }
    public Response downloadWithoutHost(String path) {
        HttpRequest request = initRequest(path)
                .GET()
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Response(httpResponse.statusCode(), httpResponse.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return new Response(-1, e.getMessage());
        }
    }

    public Response post(String path, String json) {
        HttpRequest request = initRequest(host + path)
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Response(httpResponse.statusCode(), httpResponse.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return new Response(-1, e.getMessage());
        }
    }

    public String mapToParamStr(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (String s : map.keySet()) {
            sb.append(s).append("=").append(map.get(s)).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }


    private HttpRequest.Builder initRequest(String path) {
        return HttpRequest.newBuilder(URI.create(path))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json; charset=utf-8")
                .header("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
                .timeout(Duration.ofSeconds(3));
    }

}