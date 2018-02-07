package com.pop_al.poptv.view;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class AppPref {
    private static Context appContext;
    private static AppPref appPref;
    private Editor editor = this.preferences.edit();
    private SharedPreferences preferences = appContext.getSharedPreferences(appContext.getString(R.string.app_name), 0);

    public static AppPref getInstance(Context appContext) {
        appContext = appContext;
        if (appPref != null) {
            return appPref;
        }
        appPref = new AppPref();
        return appPref;
    }

    public int getResizeMode() {
        return this.preferences.getInt("resize_mode", 0);
    }

    public void setResizeMode(int mode) {
        this.editor.putInt("resize_mode", mode);
        this.editor.commit();
    }
}
