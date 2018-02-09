package com.pop_al.poptv.view.utility.epg;

/**
 * Created by DIVITECH ICT on 18-02-09.
 */

public interface EPGData {
    EPGChannel addNewChannel(String str, String str2, String str3, String str4, String str5);

    EPGChannel getChannel(int i);

    int getChannelCount();

    EPGEvent getEvent(int i, int i2);

    List<EPGEvent> getEvents(int i);

    EPGChannel getOrCreateChannel(String str, String str2, String str3, String str4, String str5);

    boolean hasData();
}
