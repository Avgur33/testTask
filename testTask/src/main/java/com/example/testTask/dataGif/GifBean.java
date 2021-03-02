package com.example.testTask.dataGif;

public class GifBean {
    private GifObject[] data;
    private Pagination pagination;
    private Meta meta;

    public GifObject[] getData() {
        return data;
    }

    public void setData(GifObject[] data) {
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
