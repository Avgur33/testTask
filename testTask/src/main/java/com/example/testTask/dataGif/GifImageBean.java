package com.example.testTask.dataGif;

import java.util.ArrayList;

public class GifImageBean {
    private ArrayList<Object> data;
    private Pagination pagination;
    private Meta meta;

    public GifImageBean() {
    }

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
