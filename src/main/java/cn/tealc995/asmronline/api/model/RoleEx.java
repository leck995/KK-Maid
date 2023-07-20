package cn.tealc995.asmronline.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 06:58
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleEx extends Role{
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}