package com.pop_al.poptv.miscelleneious.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import org.joda.time.LocalDateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class Utils {
    public static AlertDialog showAlertBox(Context context, String message) {
        if (context == null || message.isEmpty()) {
            return null;
        }
        AlertDialog.Builder builder1 = new AlertDialog.Builder (context);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener () {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();
        return alert11;
    }

    public static void showToast(Context context, String message) {
        if (context != null && message != "" && !message.isEmpty()) {
            Toast.makeText(context, message, 0).show();
        }
    }

    public static Retrofit retrofitObject(Context context) {
        if (context != null) {
            String serverUrl = context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_SERVER_URL, 0).getString(AppConst.LOGIN_PREF_SERVER_URL_MAG, "");
            if (!(serverUrl.startsWith("http://") || serverUrl.startsWith("https://"))) {
                serverUrl = "http://" + serverUrl;
            }
            if (serverUrl.endsWith("/c")) {
                serverUrl = serverUrl.substring(0, serverUrl.length() - 2);
            }
            if (!serverUrl.endsWith("/")) {
                serverUrl = serverUrl + "/";
            }
            if (Patterns.WEB_URL.matcher(serverUrl).matches()) {
                return new Retrofit.Builder().baseUrl(serverUrl).client(new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build()).addConverterFactory(GsonConverterFactory.create()).build();
            }
        }
        return null;
    }

    public static Retrofit retrofitObjectXML(Context context) {
        if (context != null) {
            String serverUrl = context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_SERVER_URL, 0).getString(AppConst.LOGIN_PREF_SERVER_URL_MAG, "");
            if (!(serverUrl.startsWith("http://") || serverUrl.startsWith("https://"))) {
                serverUrl = "http://" + serverUrl;
            }
            if (serverUrl.endsWith("/c")) {
                serverUrl = serverUrl.substring(0, serverUrl.length() - 2);
            }
            if (!serverUrl.endsWith("/")) {
                serverUrl = serverUrl + "/";
            }
            if (Patterns.WEB_URL.matcher(serverUrl).matches()) {
                return new Retrofit.Builder().baseUrl(serverUrl).client(new OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS).writeTimeout(100, TimeUnit.SECONDS).readTimeout(100, TimeUnit.SECONDS).build()).addConverterFactory(SimpleXmlConverterFactory.create()).build();
            }
        }
        return null;
    }

    public static boolean getNetworkType(Context context) {
        boolean networkType = false;
        if (context == null) {
            return 0;
        }
        NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetwork == null) {
            return 0;
        }
        if (activeNetwork.getType() == 1) {
            networkType = true;
        } else if (activeNetwork.getType() == 0) {
            networkType = true;
        }
        return networkType;
    }

    public static int getNumberOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((((float) displayMetrics.widthPixels) / displayMetrics.density) / 180.0f);
    }

    public static long epgTimeConverter(String str) {
        int i = 15;
        int i2 = 0;
        if (str == null) {
            return 0;
        }
        try {
            if (str.length() >= 18) {
                if (str.charAt(15) == '+') {
                    i = 16;
                }
                i2 = Integer.parseInt(str.substring(i, 18)) * 60;
            }
            if (str.length() >= 19) {
                i2 += Integer.parseInt(str.substring(18));
            }
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateTimeFormat.parse(str.substring(0, 14)).getTime() - ((long) ((i2 * 60) * 1000));
        } catch (Throwable e) {
            Log.e("XMLTVReader", "Exception", e);
            return 0;
        }
    }

    public static void playWithPlayer(Context context, String selectedPlayer, int streamId, String streamType, String num, String name, String epgChannelId, String epgChannelLogo) {
        if (context == null) {
            return;
        }
        if (selectedPlayer.equals(context.getResources().getString(R.string.vlc_player))) {
            Intent VlcPlayerIntent = new Intent(context, VLCPlayerLiveStreamsActivity.class);
            VlcPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            VlcPlayerIntent.putExtra("STREAM_TYPE", streamType);
            context.startActivity(VlcPlayerIntent);
        } else if (selectedPlayer.equals(context.getResources().getString(R.string.mx_player))) {
            Intent MxPlayerIntent = new Intent(context, MxPlayerLiveStreamsActivity.class);
            MxPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            MxPlayerIntent.putExtra("STREAM_TYPE", streamType);
            context.startActivity(MxPlayerIntent);
        } else {
            Intent NSTPlayerIntent = new Intent(context, NSTPlayerActivity.class);
            NSTPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            NSTPlayerIntent.putExtra("STREAM_TYPE", streamType);
            NSTPlayerIntent.putExtra("VIDEO_NUM", Integer.parseInt(num));
            NSTPlayerIntent.putExtra("VIDEO_TITLE", name);
            NSTPlayerIntent.putExtra("EPG_CHANNEL_ID", epgChannelId);
            NSTPlayerIntent.putExtra("EPG_CHANNEL_LOGO", epgChannelLogo);
            context.startActivity(NSTPlayerIntent);
        }
    }

    public static void playWithPlayerVOD(Context context, String selectedPlayer, int streamId, String streamType, String containerExtension, String num, String name) {
        if (context == null) {
            return;
        }
        if (selectedPlayer.equals(context.getResources().getString(R.string.vlc_player))) {
            Intent VlcPlayerIntent = new Intent(context, VLCPlayerVodActivity.class);
            VlcPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            VlcPlayerIntent.putExtra("STREAM_TYPE", streamType);
            VlcPlayerIntent.putExtra("CONTAINER_EXTENSION", containerExtension);
            context.startActivity(VlcPlayerIntent);
        } else if (selectedPlayer.equals(context.getResources().getString(R.string.mx_player))) {
            Intent MxPlayerIntent = new Intent(context, MxPlayerVodActivity.class);
            MxPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            MxPlayerIntent.putExtra("STREAM_TYPE", streamType);
            MxPlayerIntent.putExtra("CONTAINER_EXTENSION", containerExtension);
            context.startActivity(MxPlayerIntent);
        } else {
            Intent NSTPlayerVodIntent = new Intent(context, NSTPlayerVodActivity.class);
            NSTPlayerVodIntent.putExtra("VIDEO_ID", streamId);
            NSTPlayerVodIntent.putExtra("VIDEO_TITLE", name);
            NSTPlayerVodIntent.putExtra("EXTENSION_TYPE", containerExtension);
            NSTPlayerVodIntent.putExtra("VIDEO_NUM", Integer.parseInt(num));
            context.startActivity(NSTPlayerVodIntent);
        }
    }

    public static String parseDateToddMMyyyy(String time) {
        String updatedDate = "";
        try {
            return new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US).parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return updatedDate;
        }
    }

    public static String parseDateToddMMyyyy1(String time) {
        String dateStr = "Mon Jun 18 00:00:00 IST 2012";
        Date date = null;
        try {
            date = new SimpleDateFormat ("E MMM dd HH:mm:ss Z yyyy", Locale.US).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String formatedDate = cal.get(5) + "/" + (cal.get(2) + 1) + "/" + cal.get(1);
        System.out.println("formatedDate : " + formatedDate);
        return formatedDate;
    }

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return TextUtils.isEmpty(str.trim());
    }

    public static int getPositionOfEPG(String string) {
        Object obj = -1;
        switch (string.hashCode()) {
            case 48:
                if (string.equals(AppConst.PASSWORD_UNSET)) {
                    obj = 10;
                    break;
                }
                break;
            case 1382:
                if (string.equals("+1")) {
                    obj = 11;
                    break;
                }
                break;
            case 1383:
                if (string.equals("+2")) {
                    obj = 12;
                    break;
                }
                break;
            case 1384:
                if (string.equals("+3")) {
                    obj = 13;
                    break;
                }
                break;
            case 1385:
                if (string.equals("+4")) {
                    obj = 14;
                    break;
                }
                break;
            case 1386:
                if (string.equals("+5")) {
                    obj = 15;
                    break;
                }
                break;
            case 1387:
                if (string.equals("+6")) {
                    obj = 16;
                    break;
                }
                break;
            case 1388:
                if (string.equals("+7")) {
                    obj = 17;
                    break;
                }
                break;
            case 1389:
                if (string.equals("+8")) {
                    obj = 18;
                    break;
                }
                break;
            case 1390:
                if (string.equals("+9")) {
                    obj = 19;
                    break;
                }
                break;
            case 1444:
                if (string.equals("-1")) {
                    obj = 9;
                    break;
                }
                break;
            case 1445:
                if (string.equals("-2")) {
                    obj = 8;
                    break;
                }
                break;
            case 1446:
                if (string.equals("-3")) {
                    obj = 7;
                    break;
                }
                break;
            case 1447:
                if (string.equals("-4")) {
                    obj = 6;
                    break;
                }
                break;
            case 1448:
                if (string.equals("-5")) {
                    obj = 5;
                    break;
                }
                break;
            case 1449:
                if (string.equals("-6")) {
                    obj = 4;
                    break;
                }
                break;
            case 1450:
                if (string.equals("-7")) {
                    obj = 3;
                    break;
                }
                break;
            case 1451:
                if (string.equals("-8")) {
                    obj = 2;
                    break;
                }
                break;
            case 1452:
                if (string.equals("-9")) {
                    obj = 1;
                    break;
                }
                break;
            case 42890:
                if (string.equals("+10")) {
                    obj = 20;
                    break;
                }
                break;
            case 44812:
                if (string.equals("-10")) {
                    obj = null;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 13;
            case 14:
                return 14;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            case 19:
                return 19;
            case 20:
                return 20;
            default:
                return 10;
        }
    }

    public static int getMilliSeconds(String epgShift) {
        if (epgShift.contains("+")) {
            return ((Integer.parseInt(epgShift.split("\\+")[1]) * 60) * 60) * 1000;
        }
        if (epgShift.contains("-")) {
            return (((-Integer.parseInt(epgShift.split("\\-")[1])) * 60) * 60) * 1000;
        }
        return 0;
    }

    public static boolean isEventVisible(long start, long end) {
        long currentTime = LocalDateTime.now().toDateTime().getMillis();
        if (start > currentTime || end < currentTime) {
            return false;
        }
        return true;
    }

    public static int getPercentageLeft(long start, long end) {
        long now = LocalDateTime.now().toDateTime().getMillis();
        if (start >= end || now >= end) {
            return 0;
        }
        if (now <= start) {
            return 100;
        }
        return (int) (((end - now) * 100) / (end - start));
    }
}