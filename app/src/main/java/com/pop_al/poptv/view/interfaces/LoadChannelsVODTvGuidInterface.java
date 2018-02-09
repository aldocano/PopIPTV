package com.pop_al.poptv.view.interfaces;

import com.pop_al.poptv.model.callback.XMLTVCallback;
import com.pop_al.poptv.model.callback.XtreamPanelAPICallback;

/**
 * Created by DIVITECH ICT on 18-02-09.
 */

public interface LoadChannelsVODTvGuidInterface {
    void laodTvGuideFailed(String str, String str2);

    void loadChannelsAndVOD(XtreamPanelAPICallback xtreamPanelAPICallback, String str);

    void loadChannelsAndVodFailed(String str, String str2);

    void loadTvGuide(XMLTVCallback xMLTVCallback);
}
