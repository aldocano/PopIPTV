package com.pop_al.poptv.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pop_al.poptv.R;
import com.pop_al.poptv.miscelleneious.common.AppConst;
import com.pop_al.poptv.miscelleneious.common.Utils;
import com.pop_al.poptv.model.callback.XtreamPanelAPICallback;
import com.pop_al.poptv.model.webrequest.RetrofitPost;
import com.pop_al.poptv.view.interfaces.XtreamPanelAPIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class XtreamPanelAPIPresenter {
    private Context context;
    private XtreamPanelAPIInterface xtreamPanelAPIInterface;

    public XtreamPanelAPIPresenter(XtreamPanelAPIInterface xtreamPanelAPIInterface, Context context) {
        this.xtreamPanelAPIInterface = xtreamPanelAPIInterface;
        this.context = context;
    }

    public void panelAPI(final String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).panelAPI(AppConst.CONTENT_TYPE, username, password).enqueue(new Callback<XtreamPanelAPICallback> () {
                public void onResponse(@NonNull Call<XtreamPanelAPICallback> call, @NonNull Response<XtreamPanelAPICallback> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        XtreamPanelAPIPresenter.this.xtreamPanelAPIInterface.panelAPI((XtreamPanelAPICallback) response.body(), username);
                    } else if (response.body() == null) {
                        XtreamPanelAPIPresenter.this.xtreamPanelAPIInterface.panelApiFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                        XtreamPanelAPIPresenter.this.xtreamPanelAPIInterface.onFinish();
                        if (XtreamPanelAPIPresenter.this.context != null) {
                            XtreamPanelAPIPresenter.this.xtreamPanelAPIInterface.onFailed(XtreamPanelAPIPresenter.this.context.getResources().getString(R.string.invalid_request));
                        }
                    }
                }

                public void onFailure(@NonNull Call<XtreamPanelAPICallback> call, @NonNull Throwable t) {
                    XtreamPanelAPIPresenter.this.xtreamPanelAPIInterface.panelApiFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                    XtreamPanelAPIPresenter.this.xtreamPanelAPIInterface.onFinish();
                    XtreamPanelAPIPresenter.this.xtreamPanelAPIInterface.onFailed(t.getMessage());
                }
            });
        }
    }
}
