package com.pop_al.poptv.presenter;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class LoginPresenter {
    private Context context;
    private LoginInterface loginInteface;

    public LoginPresenter(LoginInterface loginInteface, Context context) {
        this.loginInteface = loginInteface;
        this.context = context;
    }

    public void validateLogin(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).validateLogin(AppConst.CONTENT_TYPE, username, password).enqueue(new Callback<LoginCallback>() {
                public void onResponse(@NonNull Call<LoginCallback> call, @NonNull Response<LoginCallback> response) {
                    LoginPresenter.this.loginInteface.atStart();
                    if (response.isSuccessful()) {
                        LoginPresenter.this.loginInteface.validateLogin((LoginCallback) response.body(), AppConst.VALIDATE_LOGIN);
                        LoginPresenter.this.loginInteface.onFinish();
                    } else if (response.code() == 404) {
                        LoginPresenter.this.loginInteface.onFinish();
                        LoginPresenter.this.loginInteface.onFailed(AppConst.NETWORK_ERROR_OCCURED);
                    } else if (response.body() == null) {
                        LoginPresenter.this.loginInteface.onFinish();
                        if (LoginPresenter.this.context != null) {
                            LoginPresenter.this.loginInteface.onFailed(LoginPresenter.this.context.getResources().getString(R.string.invalid_request));
                        }
                    }
                }

                public void onFailure(@NonNull Call<LoginCallback> call, @NonNull Throwable t) {
                    if (t.getMessage() != null && t.getMessage().contains("Unable to resolve host")) {
                        LoginPresenter.this.loginInteface.onFinish();
                        LoginPresenter.this.loginInteface.onFailed(AppConst.NETWORK_ERROR_OCCURED);
                    } else if (t.getMessage() == null || !t.getMessage().contains("Failed to connect")) {
                        LoginPresenter.this.loginInteface.onFinish();
                        if (t.getMessage() != null) {
                            LoginPresenter.this.loginInteface.onFailed(t.getMessage());
                        } else {
                            LoginPresenter.this.loginInteface.onFailed(AppConst.NETWORK_ERROR_OCCURED);
                        }
                    } else {
                        LoginPresenter.this.loginInteface.onFinish();
                        LoginPresenter.this.loginInteface.onFailed(AppConst.FAILED_TO_CONNECT);
                    }
                }
            });
        } else if (retrofitObject == null) {
            this.loginInteface.stopLoader();
        }
    }
}
