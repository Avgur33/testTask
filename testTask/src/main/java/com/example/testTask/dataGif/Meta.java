package com.example.testTask.dataGif;

public class Meta {
    private Integer status;
    private String msg;
    private String response_id;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResponse_id() {
        return response_id;
    }

    public void setResponse_id(String response_id) {
        this.response_id = response_id;
    }

    public Meta(String msg, Integer status, String response_id) {
        this.msg = msg;
        this.status = status;
        this.response_id = response_id;
    }

    public Meta() {
    }
}
