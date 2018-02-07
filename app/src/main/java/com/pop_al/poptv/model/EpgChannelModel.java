package com.pop_al.poptv.model;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class EpgChannelModel {
    String next = "";
    String nowPlaying = "";

    public String getNowPlaying() {
        return this.nowPlaying;
    }

    public void setNowPlaying(String nowPlaying) {
        this.nowPlaying = nowPlaying;
    }

    public String getNext() {
        return this.next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
