package cn.tealc995.kkmaid.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-19 09:24
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String name;
    private String group;
    private boolean loggedIn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}