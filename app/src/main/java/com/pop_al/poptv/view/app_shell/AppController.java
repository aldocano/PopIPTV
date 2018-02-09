package com.pop_al.poptv.view.app_shell;

/**
 * Created by DIVITECH ICT on 18-02-09.
 */

public class AppController extends Application {
    public static AppController sInstance;
    protected String userAgent;

    public static AppController getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static synchronized AppController getInstanceSyn() {
        AppController appController;
        synchronized (AppController.class) {
            appController = sInstance;
        }
        return appController;
    }

    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        sInstance = this;
        this.userAgent = Util.getUserAgent(this, getString(R.string.app_name));
        Realm.init(this);
        Realm.setDefaultConfiguration(new Builder().name("com.nst.iptvsmarters").deleteRealmIfMigrationNeeded().schemaVersion(1).build());
    }

    public Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory((Context) this, (TransferListener) bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(this.userAgent, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return "".equals("withExtensions");
    }

    public void onLowMemory() {
        Glide.get(this).clearMemory();
        super.onLowMemory();
    }

    public void onTrimMemory(int level) {
        Glide.get(this).trimMemory(level);
        super.onTrimMemory(level);
    }
}
