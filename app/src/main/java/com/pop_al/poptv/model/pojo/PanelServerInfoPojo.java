package com.pop_al.poptv.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class PanelServerInfoPojo {
    @SerializedName("port")
    @Expose
    private String port;
    @SerializedName("url")
    @Expose
    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
