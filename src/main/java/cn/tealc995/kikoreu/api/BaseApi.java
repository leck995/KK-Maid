package cn.tealc995.kikoreu.api;

import cn.tealc995.kikoreu.NewHttpClient;

/**
 * @program: KK-Maid
 * @description:
 * @author: Leck
 * @create: 2025-02-01 22:42
 */
public class BaseApi {
    protected NewHttpClient httpClient;

    public BaseApi(NewHttpClient httpClient) {
        this.httpClient = httpClient;
    }
}