package com.pop_al.poptv.view.utility.epg.misc;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.pop_al.poptv.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso$Listener;
import com.squareup.picasso.Picasso.Builder;
import com.squareup.picasso.Target;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by DIVITECH ICT on 18-02-09.
 */

public class EPGUtil {
    private static final String TAG = "EPGUtil";
    private static final DateTimeFormatter dtfShortTime = DateTimeFormat.forPattern("HH:mm");
    private static Picasso picasso = null;

    public static String getShortTime(long timeMillis) {
        return dtfShortTime.print(timeMillis);
    }

    public static String getWeekdayName(long dateMillis) {
        return new LocalDate(dateMillis).dayOfWeek().getAsText();
    }

    public static String getEPGdayName(long dateMillis) {
        LocalDate date = new LocalDate(dateMillis);
        return date.dayOfWeek().getAsShortText() + " " + date.getDayOfMonth() + "/" + date.getMonthOfYear();
    }

    public static void loadImageInto(Context context, String url, int width, int height, Target target) {
        initPicasso(context);
        if (url == null || url.equals("")) {
            picasso.load(R.drawable.iptv_placeholder).into(target);
        } else {
            picasso.load(url).resize(width, height).centerInside().into(target);
        }
    }

    private static void initPicasso(Context context) {
        if (picasso == null) {
            picasso = new Picasso.Builder (context).downloader(new OkHttpDownloader (new OkHttpClient ())).listener(new Picasso$Listener() {
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Log.e(EPGUtil.TAG, exception.getMessage());
                }
            }).build();
        }
    }
}
