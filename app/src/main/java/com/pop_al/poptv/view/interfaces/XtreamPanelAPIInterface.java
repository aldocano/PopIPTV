package com.pop_al.poptv.view.interfaces;

import com.pop_al.poptv.model.callback.XtreamPanelAPICallback;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public interface XtreamPanelAPIInterface extends BaseInterface {
    void panelAPI(XtreamPanelAPICallback xtreamPanelAPICallback, String str);

    void panelApiFailed(String str);
}
