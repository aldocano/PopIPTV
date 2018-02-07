package com.pop_al.poptv.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pop_al.poptv.miscelleneious.common.AppConst;
import com.pop_al.poptv.miscelleneious.common.Utils;
import com.pop_al.poptv.model.callback.VodInfoCallback;
import com.pop_al.poptv.model.webrequest.RetrofitPost;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class VodPresenter {
    private Context context;
    private VodInterface vodInteface;

    public VodPresenter(VodInterface vodInteface, Context context) {
        this.vodInteface = vodInteface;
        this.context = context;
    }

    public void vodInfo(String username, String password, int streamId) {
        this.vodInteface.atStart();
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).vodInfo(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_VOD_INFO, streamId).enqueue(new Callback<VodInfoCallback> () {
                public void onResponse(@NonNull Call<VodInfoCallback> call, @NonNull Response<VodInfoCallback> response) {
                    VodPresenter.this.vodInteface.onFinish();
                    if (response.isSuccessful()) {
                        VodPresenter.this.vodInteface.vodInfo((VodInfoCallback) response.body());
                    } else if (response.body() == null) {
                        VodPresenter.this.vodInteface.onFailed(AppConst.INVALID_REQUEST);
                    }
                }

                public void onFailure(@NonNull Call<VodInfoCallback> call, @NonNull Throwable t) {
                    VodPresenter.this.vodInteface.onFinish();
                    VodPresenter.this.vodInteface.onFailed(t.getMessage());
                }
            });
        }
    }
}