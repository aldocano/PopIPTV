package com.pop_al.poptv.view.utility.epg;

/**
 * Created by DIVITECH ICT on 18-02-09.
 */

public interface EPGClickListener {
    void onChannelClicked(int i, EPGChannel ePGChannel);

    void onEventClicked(int i, int i2, EPGEvent ePGEvent);

    void onResetButtonClicked();
}