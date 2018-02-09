package com.pop_al.poptv.view.interfaces;

import com.pop_al.poptv.model.callback.LoginCallback;

/**
 * Created by DIVITECH ICT on 18-02-09.
 */

public interface LoginInterface extends BaseInterface {
    void stopLoader();

    void validateLogin(LoginCallback loginCallback, String str);
}