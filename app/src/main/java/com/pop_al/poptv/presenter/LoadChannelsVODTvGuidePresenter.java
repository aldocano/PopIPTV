package com.pop_al.poptv.presenter;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class LoadChannelsVODTvGuidePresenter {
    private Context context;
    private LoadChannelsVODTvGuidInterface loadChannelsVODTvGuidInterface;

    public LoadChannelsVODTvGuidePresenter(Context contex, LoadChannelsVODTvGuidInterface loadChannelsVODTvGuidInterface) {
        this.context = contex;
        this.loadChannelsVODTvGuidInterface = loadChannelsVODTvGuidInterface;
    }

    public void loadChannelsAndVOD(final String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).panelAPI(AppConst.CONTENT_TYPE, username, password).enqueue(new Callback<XtreamPanelAPICallback>() {
                public void onResponse(@NonNull Call<XtreamPanelAPICallback> call, @NonNull Response<XtreamPanelAPICallback> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        LoadChannelsVODTvGuidePresenter.this.loadChannelsVODTvGuidInterface.loadChannelsAndVOD((XtreamPanelAPICallback) response.body(), username);
                    } else if (response.body() == null) {
                        LoadChannelsVODTvGuidePresenter.this.loadChannelsVODTvGuidInterface.loadChannelsAndVodFailed("null", "");
                    }
                }

                public void onFailure(@NonNull Call<XtreamPanelAPICallback> call, @NonNull Throwable t) {
                    LoadChannelsVODTvGuidePresenter.this.loadChannelsVODTvGuidInterface.loadChannelsAndVodFailed(AppConst.DB_UPDATED_STATUS_FAILED, t.getMessage());
                }
            });
        }
    }

    public void loadTvGuide(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObjectXML(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).epgXMLTV(AppConst.CONTENT_TYPE, username, password).enqueue(new Callback<XMLTVCallback>() {
                public void onResponse(@NonNull Call<XMLTVCallback> call, @NonNull Response<XMLTVCallback> response) {
                    if (response.isSuccessful()) {
                        LoadChannelsVODTvGuidePresenter.this.loadChannelsVODTvGuidInterface.loadTvGuide((XMLTVCallback) response.body());
                    } else if (response.body() == null) {
                        LoadChannelsVODTvGuidePresenter.this.loadChannelsVODTvGuidInterface.laodTvGuideFailed("null", "");
                    }
                }

                public void onFailure(@NonNull Call<XMLTVCallback> call, @NonNull Throwable t) {
                    LoadChannelsVODTvGuidePresenter.this.loadChannelsVODTvGuidInterface.laodTvGuideFailed(AppConst.DB_UPDATED_STATUS_FAILED, t.getMessage());
                }
            });
        }
    }
}