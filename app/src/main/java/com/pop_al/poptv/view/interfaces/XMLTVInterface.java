package com.pop_al.poptv.view.interfaces;

import com.pop_al.poptv.model.callback.XMLTVCallback;

/**
 * Created by DIVITECH ICT on 18-02-09.
 */

public interface XMLTVInterface extends BaseInterface {
    void epgXMLTV(XMLTVCallback xMLTVCallback);

    void epgXMLTVUpdateFailed(String str);
}