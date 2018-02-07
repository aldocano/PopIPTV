package com.pop_al.poptv.model.webrequest;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public interface RetrofitPost {
    @FormUrlEncoded
    @POST("/xmltv.php")
    Call<XMLTVCallback> epgXMLTV(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<LiveStreamCategoriesCallback>> liveStreamCategories(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<LiveStreamsCallback>> liveStreams(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4, @Field("category_id") String str5);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<LiveStreamsEpgCallback> liveStreamsEpg(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4, @Field("stream_id") Integer num);

    @FormUrlEncoded
    @POST("/panel_api.php")
    Call<XtreamPanelAPICallback> panelAPI(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<LoginCallback> validateLogin(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<VodCategoriesCallback>> vodCategories(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<VodInfoCallback> vodInfo(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4, @Field("vod_id") int i);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<VodStreamsCallback>> vodStreams(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4, @Field("category_id") String str5);
}
