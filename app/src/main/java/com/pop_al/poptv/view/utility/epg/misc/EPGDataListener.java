package com.pop_al.poptv.view.utility.epg.misc;

/**
 * Created by DIVITECH ICT on 18-02-09.
 */

public class EPGDataListener {
    private EPG epg;

    public EPGDataListener(EPG epg) {
        this.epg = epg;
    }

    public void processData(EPGData data) {
        this.epg.setEPGData(data);
        this.epg.recalculateAndRedraw(null, false, null, null);
    }
}