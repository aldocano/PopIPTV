package com.pop_al.poptv.view.player;

/**
 * Created by DIVITECH ICT on 18-02-09.
 */


        import android.app.Activity;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.media.AudioManager;
        import android.media.AudioManager.OnAudioFocusChangeListener;
        import android.net.Uri;
        import android.net.Uri.Builder;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.v7.widget.AppCompatButton;
        import android.support.v7.widget.AppCompatImageView;
        import android.support.v7.widget.AppCompatTextView;
        import android.telephony.PhoneStateListener;
        import android.telephony.TelephonyManager;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;
        import com.google.android.exoplayer2.C;
        import com.google.android.exoplayer2.DefaultLoadControl;
        import com.google.android.exoplayer2.DefaultRenderersFactory;
        import com.google.android.exoplayer2.ExoPlaybackException;
        import com.google.android.exoplayer2.ExoPlayer.EventListener;
        import com.google.android.exoplayer2.ExoPlayerFactory;
        import com.google.android.exoplayer2.PlaybackParameters;
        import com.google.android.exoplayer2.SimpleExoPlayer;
        import com.google.android.exoplayer2.Timeline;
        import com.google.android.exoplayer2.Timeline.Window;
        import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
        import com.google.android.exoplayer2.drm.DrmSessionManager;
        import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
        import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
        import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
        import com.google.android.exoplayer2.drm.UnsupportedDrmException;
        import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
        import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException;
        import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
        import com.google.android.exoplayer2.source.BehindLiveWindowException;
        import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
        import com.google.android.exoplayer2.source.ExtractorMediaSource;
        import com.google.android.exoplayer2.source.MediaSource;
        import com.google.android.exoplayer2.source.TrackGroupArray;
        import com.google.android.exoplayer2.source.dash.DashMediaSource;
        import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
        import com.google.android.exoplayer2.source.hls.HlsMediaSource;
        import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
        import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
        import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
        import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
        import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
        import com.google.android.exoplayer2.trackselection.TrackSelection;
        import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
        import com.google.android.exoplayer2.ui.DebugTextViewHelper;
        import com.google.android.exoplayer2.ui.PlaybackControlView.VisibilityListener;
        import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
        import com.google.android.exoplayer2.upstream.DataSource.Factory;
        import com.google.android.exoplayer2.upstream.DefaultAllocator;
        import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
        import com.google.android.exoplayer2.upstream.HttpDataSource;
        import com.google.android.exoplayer2.util.Util;
        import com.pop_al.poptv.R;
        import com.pop_al.poptv.miscelleneious.common.AppConst;
        import com.pop_al.poptv.miscelleneious.common.Utils;
        import com.pop_al.poptv.model.LiveStreamsDBModel;
        import com.pop_al.poptv.model.database.LiveStreamDBHandler;
        import com.pop_al.poptv.view.AppPref;
        import com.pop_al.poptv.view.app_shell.AppController;
        import io.realm.Realm;
        import java.net.CookieHandler;
        import java.net.CookieManager;
        import java.net.CookiePolicy;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.UUID;

public class PlayerActivity extends Activity implements OnClickListener, EventListener, VisibilityListener, OnAudioFocusChangeListener {
    public static final String ACTION_VIEW = "com.nst.exoplayer.app.action.VIEW";
    public static final String ACTION_VIEW_LIST = "com.nst.exoplayer.app.action.VIEW_LIST";
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final CookieManager DEFAULT_COOKIE_MANAGER = new CookieManager();
    public static final String DRM_KEY_REQUEST_PROPERTIES = "drm_key_request_properties";
    public static final String DRM_LICENSE_URL = "drm_license_url";
    public static final String DRM_SCHEME_UUID_EXTRA = "drm_scheme_uuid";
    public static final String EXTENSION_EXTRA = "extension";
    public static final String EXTENSION_LIST_EXTRA = "extension_list";
    public static final String EXTENSION_TYPE = "com.nst.exoplayer.app.extension_type";
    public static final String IS_VALID_URL = "com.nst.exoplayer.app.is_valid_url";
    public static final String KEY_CATEGORY_ID = "com.nst.category.id";
    private static final long MAX_POSITION_FOR_SEEK_TO_PREVIOUS = 3000;
    public static final String PREFER_EXTENSION_DECODERS = "prefer_extension_decoders";
    private static final Map<Integer, Integer> RESIZE_MODE = Collections.unmodifiableMap(new HashMap<Integer, Integer>() {
        {
            put(Integer.valueOf(3), Integer.valueOf(R.drawable.ic_center_focus_strong_black_24dp));
            put(Integer.valueOf(0), Integer.valueOf(R.drawable.ic_fullscreen_black_24dp));
            put(Integer.valueOf(1), Integer.valueOf(R.drawable.ic_center_focus_strong_black_24dp));
            put(Integer.valueOf(2), Integer.valueOf(R.drawable.ic_zoom_out_map_black_24dp));
        }
    });
    public static final String STREAM_TYPE = "com.nst.exoplayer.app.is_stream_type";
    public static final String URI_LIST_EXTRA = "uri_list";
    public static final String VIDEO_ID = "com.nst.exoplayer.app.video._id";
    public static final String VIDEO_NUM = "com.nst.exoplayer.app.video._num";
    public static final String VIDEO_TITLE = "com.nst.exoplayer.app.video.title";
    private int CURRENT_RESIZE_MODE = 0;
    private ArrayList<LiveStreamsDBModel> allStreams;
    private AppCompatImageView btn_screen;
    private AppCompatImageView btn_settings;
    private AppCompatImageView btn_sub;
    private String container_extension;
    private LinearLayout debugRootView;
    private TextView debugTextView;
    private DebugTextViewHelper debugViewHelper;
    private EventLogger eventLogger;
    private int isBanner = 0;
    private TrackGroupArray lastSeenTrackGroupArray;
    LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesSharedPref;
    private AudioManager mAudioManager;
    private Handler mainHandler;
    private Factory mediaDataSourceFactory;
    private TelephonyManager mgr;
    private boolean needRetrySource;
    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == 1) {
                PlayerActivity.this.releasePlayer();
            } else if (state == 0) {
                PlayerActivity.this.initializePlayer();
            } else if (state == 2) {
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    };
    private SimpleExoPlayer player;
    private ProgressBar progress;
    private Realm realm;
    private long resumePosition;
    private int resumeWindow;
    private Button retryButton;
    private boolean shouldAutoPlay;
    private SimpleExoPlayerView simpleExoPlayerView;
    private String stream_type;
    private TrackSelectionHelper trackSelectionHelper;
    private DefaultTrackSelector trackSelector;
    private int video_id;
    private int video_num;
    private AppCompatTextView video_title;

    static {
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != 0) {
            return false;
        }
        for (Throwable cause = e.getSourceException(); cause != null; cause = cause.getCause()) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
        }
        return false;
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        super.onCreate(savedInstanceState);
        this.liveStreamDBHandler = new LiveStreamDBHandler(this);
        this.allStreams = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(AppConst.PASSWORD_UNSET, "live");
        this.stream_type = getIntent().getStringExtra("com.nst.exoplayer.app.is_stream_type");
        this.mgr = (TelephonyManager) getSystemService("phone");
        this.video_id = getIntent().getIntExtra("com.nst.exoplayer.app.video._id", 0);
        this.video_num = getIntent().getIntExtra("com.nst.exoplayer.app.video._num", 0);
        this.shouldAutoPlay = true;
        clearResumePosition();
        this.mediaDataSourceFactory = buildDataSourceFactory(true);
        this.mainHandler = new Handler();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        setContentView(R.layout.player_activity);
        View rootView = findViewById(R.id.root);
        this.mAudioManager = (AudioManager) getSystemService("audio");
        this.mAudioManager.requestAudioFocus(this, 3, 1);
        this.progress = (ProgressBar) findViewById(R.id.progress);
        this.debugRootView = (LinearLayout) findViewById(R.id.controls_root);
        this.debugTextView = (TextView) findViewById(R.id.debug_text_view);
        this.retryButton = (AppCompatButton) findViewById(R.id.retry_button);
        this.retryButton.setOnClickListener(this);
        if (this.progress != null) {
            this.progress.setVisibility(0);
        }
        this.simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        this.simpleExoPlayerView.setUseController(true);
        this.simpleExoPlayerView.setControllerVisibilityListener(this);
        this.simpleExoPlayerView.requestFocus();
        this.CURRENT_RESIZE_MODE = AppPref.getInstance(this).getResizeMode();
        this.simpleExoPlayerView.setResizeMode(((Integer) RESIZE_MODE.keySet().toArray()[this.CURRENT_RESIZE_MODE]).intValue());
        this.video_title = (AppCompatTextView) findViewById(R.id.title);
        this.btn_settings = (AppCompatImageView) findViewById(R.id.btn_settings);
        ((AppCompatImageView) findViewById(R.id.btn_back)).setOnClickListener(this);
        this.btn_settings.setOnClickListener(this);
        this.btn_sub = (AppCompatImageView) findViewById(R.id.btn_sub);
        this.btn_sub.setOnClickListener(this);
        findViewById(R.id.exo_next).setOnClickListener(this);
        findViewById(R.id.exo_prev).setOnClickListener(this);
        this.btn_screen = (AppCompatImageView) findViewById(R.id.btn_screen);
        this.btn_screen.setImageResource(((Integer) RESIZE_MODE.get(Integer.valueOf(this.CURRENT_RESIZE_MODE))).intValue());
        this.btn_screen.setOnClickListener(this);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 19:
                findViewById(R.id.exo_next).performClick();
                return true;
            case 20:
                findViewById(R.id.exo_prev).performClick();
                return true;
            case 166:
                findViewById(R.id.exo_next).performClick();
                return true;
            case 167:
                findViewById(R.id.exo_prev).performClick();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public void onNewIntent(Intent intent) {
        releasePlayer();
        this.shouldAutoPlay = true;
        clearResumePosition();
        setIntent(intent);
    }

    public void onStart() {
        super.onStart();
        if (this.realm == null || this.realm.isClosed()) {
            this.realm = Realm.getDefaultInstance();
        }
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || this.player == null) {
            initializePlayer();
        }
    }

    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
        this.realm.close();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length <= 0 || grantResults[0] != 0) {
            showToast((int) R.string.storage_permission_denied);
            finish();
            return;
        }
        initializePlayer();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        this.simpleExoPlayerView.showController();
        return super.dispatchKeyEvent(event) || this.simpleExoPlayerView.dispatchMediaKeyEvent(event);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                return;
            case R.id.btn_screen:
                toggleResizeMode();
                return;
            case R.id.btn_settings:
                try {
                    if (this.trackSelector.getCurrentMappedTrackInfo() != null) {
                        this.trackSelectionHelper.showSelectionDialog(this, "Select", this.trackSelector.getCurrentMappedTrackInfo(), 0);
                        return;
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "onClick: " + e.getMessage());
                    return;
                }
            case R.id.btn_sub:
                try {
                    if (this.trackSelector.getCurrentMappedTrackInfo() != null) {
                        this.trackSelectionHelper.showSelectionDialog(this, "Audio", this.trackSelector.getCurrentMappedTrackInfo(), 1);
                        return;
                    }
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    Log.e("TAG", "onClick: " + e2.getMessage());
                    return;
                }
            case R.id.exo_next:
                next();
                int indexNext = this.player.getCurrentWindowIndex();
                Log.e("TAG", "indexNext: " + indexNext);
                if (indexNext <= this.allStreams.size() - 1) {
                    this.video_title.setText(((LiveStreamsDBModel) this.allStreams.get(indexNext)).getName());
                    if (this.needRetrySource) {
                        clearResumePosition();
                        getIntent().putExtra("com.nst.exoplayer.app.video._id", ((LiveStreamsDBModel) this.allStreams.get(indexNext)).getStreamId());
                        initializePlayer();
                        return;
                    }
                    return;
                }
                return;
            case R.id.exo_prev:
                previousLive();
                int indexPrev = this.player.getCurrentWindowIndex();
                Log.e("TAG", "indexPrev: " + indexPrev);
                if (indexPrev >= 0) {
                    this.video_title.setText(((LiveStreamsDBModel) this.allStreams.get(indexPrev)).getName());
                    if (this.needRetrySource) {
                        clearResumePosition();
                        getIntent().putExtra("com.nst.exoplayer.app.video._id", ((LiveStreamsDBModel) this.allStreams.get(indexPrev)).getStreamId());
                        initializePlayer();
                        return;
                    }
                    return;
                }
                return;
            case R.id.retry_button:
                initializePlayer();
                return;
            default:
                return;
        }
    }

    private void toggleResizeMode() {
        this.CURRENT_RESIZE_MODE++;
        if (this.CURRENT_RESIZE_MODE >= RESIZE_MODE.size()) {
            this.CURRENT_RESIZE_MODE = 0;
        }
        AppPref.getInstance(this).setResizeMode(this.CURRENT_RESIZE_MODE);
        this.simpleExoPlayerView.setResizeMode(((Integer) RESIZE_MODE.keySet().toArray()[this.CURRENT_RESIZE_MODE]).intValue());
        this.btn_screen.setImageResource(((Integer) RESIZE_MODE.get(Integer.valueOf(this.CURRENT_RESIZE_MODE))).intValue());
    }

    private void previous() {
        Timeline currentTimeline = this.simpleExoPlayerView.getPlayer().getCurrentTimeline();
        if (!currentTimeline.isEmpty()) {
            int currentWindowIndex = this.simpleExoPlayerView.getPlayer().getCurrentWindowIndex();
            Window currentWindow = currentTimeline.getWindow(currentWindowIndex, new Window());
            if (currentWindowIndex <= 0 || (this.player.getCurrentPosition() > MAX_POSITION_FOR_SEEK_TO_PREVIOUS && (!currentWindow.isDynamic || currentWindow.isSeekable))) {
                this.player.seekTo(0);
            } else {
                this.player.seekTo(currentWindowIndex - 1, C.TIME_UNSET);
            }
        }
    }

    private void previousLive() {
        if (!this.simpleExoPlayerView.getPlayer().getCurrentTimeline().isEmpty()) {
            int currentWindowIndex = this.simpleExoPlayerView.getPlayer().getCurrentWindowIndex();
            if (currentWindowIndex == 0) {
                this.player.seekTo(this.allStreams.size() - 1, C.TIME_UNSET);
            } else {
                this.player.seekTo(currentWindowIndex - 1, C.TIME_UNSET);
            }
        }
    }

    private void next() {
        if (!this.simpleExoPlayerView.getPlayer().getCurrentTimeline().isEmpty()) {
            int currentWindowIndex = this.simpleExoPlayerView.getPlayer().getCurrentWindowIndex();
            if (currentWindowIndex == this.allStreams.size() - 1) {
                this.player.seekTo(0, C.TIME_UNSET);
            } else {
                this.player.seekTo(currentWindowIndex + 1, C.TIME_UNSET);
            }
        }
    }

    private Uri buildURI(String stream_type, int stream_id, String container_extension) {
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        String username = this.loginPreferencesSharedPref.getString("username", "");
        String password = this.loginPreferencesSharedPref.getString("password", "");
        String allowedFormat = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
        String serverUrl = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_URL, "");
        String serverPort = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_PORT, "");
        Builder builder = new Builder();
        try {
            builder.scheme("http").encodedAuthority(serverUrl + ":" + serverPort).appendPath(stream_type).appendPath(username).appendPath(password).appendPath(Integer.toString(stream_id) + ".ts");
            return builder.build();
        } catch (Exception e) {
            Log.e("DB", "initializePlayer: " + e.getMessage());
            return null;
        }
    }

    public void onVisibilityChange(int visibility) {
        this.debugRootView.setVisibility(visibility);
    }

    private void initializePlayer() {
        Intent intent = getIntent();
        this.video_id = intent.getIntExtra("com.nst.exoplayer.app.video._id", 0);
        this.stream_type = intent.getStringExtra("com.nst.exoplayer.app.is_stream_type");
        this.container_extension = intent.getStringExtra("com.nst.exoplayer.app.extension_type");
        boolean needNewPlayer = this.player == null;
        if (this.realm == null || this.realm.isClosed()) {
            this.realm = Realm.getDefaultInstance();
        }
        if (this.video_title != null) {
            String vtitle = intent.getStringExtra("com.nst.exoplayer.app.video.title");
            if (!Utils.isEmpty(vtitle)) {
                this.video_title.setText(vtitle);
            }
        }
        if (needNewPlayer) {
            boolean preferExtensionDecoders = intent.getBooleanExtra("prefer_extension_decoders", false);
            UUID drmSchemeUuid = intent.hasExtra("drm_scheme_uuid") ? UUID.fromString(intent.getStringExtra("drm_scheme_uuid")) : null;
            DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
            if (drmSchemeUuid != null) {
                try {
                    drmSessionManager = buildDrmSessionManager(drmSchemeUuid, intent.getStringExtra("drm_license_url"), intent.getStringArrayExtra("drm_key_request_properties"));
                } catch (UnsupportedDrmException e) {
                    int errorStringId = Util.SDK_INT < 18 ? R.string.error_drm_not_supported : e.reason == 1 ? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown;
                    showToast(errorStringId);
                    return;
                }
            }
            int extensionRendererMode = ((AppController) getApplication()).useExtensionRenderers() ? preferExtensionDecoders ? 2 : 1 : 0;
            DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(this, drmSessionManager, extensionRendererMode);
            TrackSelection.Factory factory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            this.trackSelector = new DefaultTrackSelector(factory);
            this.trackSelectionHelper = new TrackSelectionHelper(this.trackSelector, factory);
            this.lastSeenTrackGroupArray = null;
            this.player = ExoPlayerFactory.newSimpleInstance(defaultRenderersFactory, this.trackSelector, new DefaultLoadControl(new DefaultAllocator(true, 1024), 15000, DefaultLoadControl.DEFAULT_MAX_BUFFER_MS, 2500, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS));
            this.player.addListener(this);
            this.eventLogger = new EventLogger(this.trackSelector);
            this.player.addListener(this.eventLogger);
            this.player.setAudioDebugListener(this.eventLogger);
            this.player.setVideoDebugListener(this.eventLogger);
            this.player.setMetadataOutput(this.eventLogger);
            this.simpleExoPlayerView.setPlayer(this.player);
            this.player.setPlayWhenReady(this.shouldAutoPlay);
            this.debugViewHelper = new DebugTextViewHelper(this.player, this.debugTextView);
            this.debugViewHelper.start();
        }
        if (needNewPlayer || this.needRetrySource) {
            Uri[] uris;
            String[] extensions;
            int i;
            String action = intent.getAction();
            if ("com.nst.exoplayer.app.action.VIEW".equals(action)) {
                Uri stream_uri = buildURI(this.stream_type, this.video_id, this.container_extension);
                uris = new Uri[]{stream_uri};
                extensions = new String[uris.length];
                findViewById(R.id.exo_next).setEnabled(false);
                findViewById(R.id.exo_prev).setEnabled(false);
            } else if ("com.nst.exoplayer.app.action.VIEW_LIST".equals(action)) {
                uris = new Uri[this.allStreams.size()];
                extensions = new String[this.allStreams.size()];
                for (i = 0; i < this.allStreams.size(); i++) {
                    uris[i] = buildURI(this.stream_type, Integer.parseInt(((LiveStreamsDBModel) this.allStreams.get(i)).getStreamId()), "ts");
                }
            } else {
                showToast(getString(R.string.unexpected_intent_action, new Object[]{action}));
                return;
            }
            MediaSource[] mediaSources = new MediaSource[uris.length];
            for (i = 0; i < uris.length; i++) {
                mediaSources[i] = buildMediaSource(uris[i], extensions[i]);
            }
            MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0] : new ConcatenatingMediaSource(mediaSources);
            boolean haveResumePosition = this.resumeWindow != -1;
            if (haveResumePosition) {
                this.player.seekTo(this.resumeWindow, this.resumePosition);
            } else {
                this.resumeWindow = this.video_num - 1;
                this.player.seekToDefaultPosition(this.resumeWindow);
            }
            this.player.prepare(mediaSource, !haveResumePosition, false);
            this.needRetrySource = false;
            updateButtonVisibilities();
        }
    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type;
        if (TextUtils.isEmpty(overrideExtension)) {
            type = Util.inferContentType(uri);
        } else {
            type = Util.inferContentType("." + overrideExtension);
        }
        switch (type) {
            case 0:
                return new DashMediaSource(uri, buildDataSourceFactory(false), new DefaultDashChunkSource.Factory(this.mediaDataSourceFactory), this.mainHandler, this.eventLogger);
            case 1:
                return new SsMediaSource(uri, buildDataSourceFactory(false), new DefaultSsChunkSource.Factory(this.mediaDataSourceFactory), this.mainHandler, this.eventLogger);
            case 2:
                return new HlsMediaSource(uri, this.mediaDataSourceFactory, this.mainHandler, this.eventLogger);
            case 3:
                return new ExtractorMediaSource(uri, this.mediaDataSourceFactory, new DefaultExtractorsFactory(), this.mainHandler, this.eventLogger);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManager(UUID uuid, String licenseUrl, String[] keyRequestPropertiesArray) throws UnsupportedDrmException {
        if (Util.SDK_INT < 18) {
            return null;
        }
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl, buildHttpDataSourceFactory(false));
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i], keyRequestPropertiesArray[i + 1]);
            }
        }
        return new DefaultDrmSessionManager(uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback, null, this.mainHandler, this.eventLogger);
    }

    private void releasePlayer() {
        if (this.player != null) {
            this.debugViewHelper.stop();
            this.debugViewHelper = null;
            this.shouldAutoPlay = this.player.getPlayWhenReady();
            updateResumePosition();
            this.player.release();
            this.player = null;
            this.trackSelector = null;
            this.trackSelectionHelper = null;
            this.eventLogger = null;
        }
    }

    private void updateResumePosition() {
        this.resumeWindow = this.player.getCurrentWindowIndex();
        this.resumePosition = this.player.isCurrentWindowSeekable() ? Math.max(0, this.player.getCurrentPosition()) : C.TIME_UNSET;
    }

    private void clearResumePosition() {
        this.resumeWindow = -1;
        this.resumePosition = C.TIME_UNSET;
    }

    private Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return AppController.getInstance().buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return AppController.getInstance().buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    public void onLoadingChanged(boolean isLoading) {
    }

    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case 1:
                if (this.progress != null) {
                    this.progress.setVisibility(8);
                    break;
                }
                break;
            case 2:
                if (this.progress != null) {
                    this.progress.setVisibility(0);
                    break;
                }
                break;
            case 3:
                if (this.progress != null) {
                    this.progress.setVisibility(8);
                    break;
                }
                break;
            case 4:
                if (this.progress != null) {
                    this.progress.setVisibility(8);
                }
                initializePlayer();
                showControls();
                break;
            default:
                if (this.progress != null) {
                    this.progress.setVisibility(8);
                    break;
                }
                break;
        }
        updateButtonVisibilities();
    }

    public void onPositionDiscontinuity() {
        if (this.needRetrySource) {
            updateResumePosition();
        }
    }

    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    public void onPlayerError(ExoPlaybackException e) {
        String errorString = null;
        if (e.type == 1) {
            Exception cause = e.getRendererException();
            if (cause instanceof DecoderInitializationException) {
                DecoderInitializationException decoderInitializationException = (DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName != null) {
                    errorString = getString(R.string.error_instantiating_decoder, new Object[]{decoderInitializationException.decoderName});
                } else if (decoderInitializationException.getCause() instanceof DecoderQueryException) {
                    errorString = getString(R.string.error_querying_decoders);
                } else if (decoderInitializationException.secureDecoderRequired) {
                    errorString = getString(R.string.error_no_secure_decoder, new Object[]{decoderInitializationException.mimeType});
                } else {
                    errorString = getString(R.string.error_no_decoder, new Object[]{decoderInitializationException.mimeType});
                }
            }
        }
        if (errorString != null) {
            showToast(errorString);
        }
        this.needRetrySource = true;
        if (isBehindLiveWindow(e)) {
            clearResumePosition();
            initializePlayer();
            return;
        }
        updateResumePosition();
        updateButtonVisibilities();
        showControls();
    }

    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        updateButtonVisibilities();
        if (trackGroups != this.lastSeenTrackGroupArray) {
            MappedTrackInfo mappedTrackInfo = this.trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                if (mappedTrackInfo.getTrackTypeRendererSupport(2) == 1) {
                    showToast((int) R.string.error_unsupported_video);
                }
                if (mappedTrackInfo.getTrackTypeRendererSupport(1) == 1) {
                    showToast((int) R.string.error_unsupported_audio);
                }
            }
            this.lastSeenTrackGroupArray = trackGroups;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mgr != null) {
            this.mgr.listen(this.phoneStateListener, 0);
        }
    }

    private void updateButtonVisibilities() {
        this.debugRootView.removeAllViews();
        this.retryButton.setVisibility(this.needRetrySource ? 0 : 8);
        this.debugRootView.addView(this.retryButton);
        if (this.player != null) {
            MappedTrackInfo mappedTrackInfo = this.trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                for (int i = 0; i < mappedTrackInfo.length; i++) {
                    if (mappedTrackInfo.getTrackGroups(i).length != 0) {
                        int label;
                        Button button = new Button(this);
                        switch (this.player.getRendererType(i)) {
                            case 1:
                                label = R.string.audio;
                                this.btn_sub.setVisibility(0);
                                break;
                            case 2:
                                label = R.string.video;
                                break;
                            case 3:
                                label = R.string.text;
                                break;
                            default:
                                break;
                        }
                        button.setText(label);
                        button.setTag(Integer.valueOf(i));
                        button.setOnClickListener(this);
                    }
                }
            }
        }
    }

    private void showControls() {
        this.debugRootView.setVisibility(0);
    }

    private void showToast(int messageId) {
        showToast(getString(messageId));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, 1).show();
    }

    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
        }
    }

    protected void onSaveInstanceState(Bundle intent) {
        super.onSaveInstanceState(intent);
    }
}