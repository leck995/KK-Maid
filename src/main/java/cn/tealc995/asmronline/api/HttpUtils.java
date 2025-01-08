package cn.tealc995.asmronline.api;


import cn.tealc995.asmronline.api.model.Response;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.SortedMap;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 19:51
 */
public class HttpUtils {
    public static String TOKEN = null;

    public static Response get(String path,Map<String,String> params) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = null;
            //String encode = URLEncoder.encode(formatter(params), StandardCharsets.UTF_8);
            URL url = new URL(path + "?" +formatter(params));
            System.out.println(path + "?" +formatter(params));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("GET");
            if (TOKEN != null){
                conn.setRequestProperty("Authorization", "Bearer "+TOKEN); //  TOKEN 如果没有token可以删除
            }
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");// 设定
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setUseCaches(false);
            if (conn.getResponseCode() == 200) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(),StandardCharsets.UTF_8));
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
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
    public static Response get(String path,String param) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = null;
            URL url = new URL(path+"?" + param);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (TOKEN != null){
                conn.setRequestProperty("Authorization", "Bearer "+TOKEN); //  TOKEN 如果没有token可以删除
            }
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");// 设定
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setUseCaches(false);
            if (conn.getResponseCode() == 200) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(),StandardCharsets.UTF_8));
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
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

    public static Response get(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = null;
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (TOKEN != null){
                conn.setRequestProperty("Authorization", "Bearer "+TOKEN); //  TOKEN 如果没有token可以删除
            }
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");// 设定
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setUseCaches(false);
            if (conn.getResponseCode() == 200) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(),StandardCharsets.UTF_8));
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
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




    public static Response put(String path,String json) {

        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            if (TOKEN != null){
                conn.setRequestProperty("Authorization", "Bearer "+TOKEN); //  TOKEN 如果没有token可以删除
            }
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //conn.connect();
            System.out.println(json);
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


    public static Response delete(String path) {

        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            if (TOKEN != null){
                conn.setRequestProperty("Authorization", "Bearer "+TOKEN); //  TOKEN 如果没有token可以删除
            }
            // 获取URLConnection对象对应的输出流
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


    public static String download(String path){
        System.out.println("download:"+path);
        try {
            URL url=new URL(path);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb=new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine())!= null){
                sb.append(line).append("\\n");
            }
            bufferedReader.close();
            return sb.toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

/*    public static String post(String path,Map<String,String> params){
        try {
            URL url=new URL(path);
            System.out.println(url);
            URLConnection urlConnection = url.openConnection();

            urlConnection.connect();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);


            PrintWriter printWriter = new PrintWriter(urlConnection.getOutputStream());
            printWriter.print(formatter(params));
            // flush输出流的缓冲
            printWriter.flush();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb=new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine())!= null){
                sb.append(line);
            }

            bufferedReader.close();
            printWriter.close();
            return sb.toString();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/


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
            if (TOKEN != null){
                conn.setRequestProperty("Authorization", "Bearer "+TOKEN); //  TOKEN 如果没有token可以删除
            }

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


    public static String formatter(Map<String,String> map){
        StringBuilder sb=new StringBuilder();
        for (String s : map.keySet()) {
            sb.append(s).append("=").append(map.get(s)).append("&");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}