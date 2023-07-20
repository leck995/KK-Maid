package cn.tealc995.asmronline.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 03:24
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}