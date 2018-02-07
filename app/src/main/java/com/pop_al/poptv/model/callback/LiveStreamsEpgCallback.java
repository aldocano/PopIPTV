package com.pop_al.poptv.model.callback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pop_al.poptv.model.pojo.EpgListingPojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class LiveStreamsEpgCallback implements Serializable {
    @SerializedName("epg_listings")
    @Expose
    private List<EpgListingPojo> epgListingPojos = null;

    public List<EpgListingPojo> getEpgListingPojos() {
        return this.epgListingPojos;
    }

    public void setEpgListingPojos(List<EpgListingPojo> epgListingPojos) {
        this.epgListingPojos = epgListingPojos;
    }
}
