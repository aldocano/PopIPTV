package com.pop_al.poptv.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class PanelCategoriesPojo {
    @SerializedName("live")
    @Expose
    private ArrayList<PanelLivePojo> live = null;
    @SerializedName("movie")
    @Expose
    private ArrayList<PanelMoviePojo> movie = null;

    public ArrayList<PanelMoviePojo> getMovie() {
        return this.movie;
    }

    public void setMovie(ArrayList<PanelMoviePojo> movie) {
        this.movie = movie;
    }

    public ArrayList<PanelLivePojo> getLive() {
        return this.live;
    }

    public void setLive(ArrayList<PanelLivePojo> live) {
        this.live = live;
    }
}