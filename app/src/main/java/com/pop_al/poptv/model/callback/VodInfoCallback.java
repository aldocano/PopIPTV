package com.pop_al.poptv.model.callback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pop_al.poptv.model.pojo.VodInfoPojo;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class VodInfoCallback {
    @SerializedName("info")
    @Expose
    private VodInfoPojo info;

    public VodInfoPojo getInfo() {
        return this.info;
    }

    public void setInfo(VodInfoPojo info) {
        this.info = info;
    }
}
