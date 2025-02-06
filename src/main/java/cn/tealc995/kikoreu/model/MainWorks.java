package cn.tealc995.kikoreu.model;

import java.util.List;

/**
 * @description:
 * @author: Leck
 * @create: 2023-07-12 19:37
 */

public class MainWorks {
    private Pagination pagination;
    private List<Work> works;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }
}