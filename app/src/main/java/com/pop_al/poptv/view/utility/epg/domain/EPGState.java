package com.pop_al.poptv.view.utility.epg.domain;

import android.os.Parcelable;
import android.view.View;

/**
 * Created by DIVITECH ICT on 18-02-09.
 */

public class EPGState extends View.BaseSavedState {
    private EPGEvent currentEvent = null;

    public EPGState(Parcelable superState) {
        super(superState);
    }

    public EPGEvent getCurrentEvent() {
        return this.currentEvent;
    }

    public void setCurrentEvent(EPGEvent currentEvent) {
        this.currentEvent = currentEvent;
    }
}
