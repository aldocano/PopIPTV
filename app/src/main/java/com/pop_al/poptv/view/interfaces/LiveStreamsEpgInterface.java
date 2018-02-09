package com.pop_al.poptv.view.interfaces;

import com.pop_al.poptv.model.callback.LiveStreamsEpgCallback;

/**
 * Created by DIVITECH ICT on 18-02-09.
 */

public interface LiveStreamsEpgInterface extends BaseInterface {
    void liveStreamsEpg(LiveStreamsEpgCallback liveStreamsEpgCallback);
}
