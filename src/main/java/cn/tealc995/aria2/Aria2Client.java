package cn.tealc995.aria2;


import cn.tealc995.api.model.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 19:51
 */
public class Aria2Client {






    public static Response post(String path,String json) {
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);


            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");// 设定
            conn.setRequestProperty("Content-Length", String.valueOf(bytes.length));
            OutputStream outwritestream = conn.getOutputStream();
            outwritestream.write(bytes);
            outwritestream.flush();
            outwritestream.close();
            if (conn.getResponseCode() == 200) {
                // 定义BufferedReader输入流来读取URL的响应
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(),StandardCharsets.UTF_8));
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
            }
            conn.disconnect();
            return new Response(conn.getResponseCode(),sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(404,e.getMessage());
        }
    }


}