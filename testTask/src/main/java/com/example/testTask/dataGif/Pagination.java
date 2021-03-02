package com.example.testTask.dataGif;

public class Pagination {
    private Integer total_count;
    private Integer count;
    private Integer offset;

    public Integer getOffset() { return offset; }

    public void setOffset(Integer offset) { this.offset = offset; }

    public Integer getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}