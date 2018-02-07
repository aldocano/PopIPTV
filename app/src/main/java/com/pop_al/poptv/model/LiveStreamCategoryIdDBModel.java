package com.pop_al.poptv.model;

import java.io.Serializable;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class LiveStreamCategoryIdDBModel implements Serializable {
    private int id;
    private String liveStreamCategoryID;
    private String liveStreamCategoryName;
    private int parentId;

    public int getParentId() {
        return this.parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public LiveStreamCategoryIdDBModel(String liveStreamCategoryID, String liveStreamCategoryName, int parentId) {
        this.liveStreamCategoryID = liveStreamCategoryID;
        this.liveStreamCategoryName = liveStreamCategoryName;
        this.parentId = parentId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLiveStreamCategoryID() {
        return this.liveStreamCategoryID;
    }

    public void setLiveStreamCategoryID(String liveStreamCategoryID) {
        this.liveStreamCategoryID = liveStreamCategoryID;
    }

    public String getLiveStreamCategoryName() {
        return this.liveStreamCategoryName;
    }

    public void setLiveStreamCategoryName(String liveStreamCategoryName) {
        this.liveStreamCategoryName = liveStreamCategoryName;
    }
}