package com.pop_al.poptv.view.utility.epg.service;

import android.content.Context;

import com.google.common.collect.Maps;
import com.pop_al.poptv.R;
import com.pop_al.poptv.miscelleneious.common.Utils;
import com.pop_al.poptv.model.LiveStreamsDBModel;
import com.pop_al.poptv.model.database.LiveStreamDBHandler;
import com.pop_al.poptv.model.database.PasswordStatusDBModel;
import com.pop_al.poptv.model.pojo.XMLTVProgrammePojo;
import com.pop_al.poptv.view.utility.epg.EPGData;
import com.pop_al.poptv.view.utility.epg.domain.EPGChannel;
import com.pop_al.poptv.view.utility.epg.domain.EPGEvent;
import com.pop_al.poptv.view.utility.epg.misc.EPGDataListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by DIVITECH ICT on 18-02-09.
 */

public class EPGService {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static SimpleDateFormat nowFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    private Context context;
    private ArrayList<String> listPassword = new ArrayList();
    private ArrayList<LiveStreamsDBModel> liveListDetail;
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlckedDetail;
    LiveStreamDBHandler liveStreamDBHandler;

    public EPGService(Context context) {
        this.context = context;
    }

    public EPGData getData(EPGDataListener listener, int dayOffset, String catID) {
        try {
            return parseData(catID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private EPGData parseData(String catID) {
        Throwable ex;
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        EPGChannel firstChannel = null;
        EPGChannel prevChannel = null;
        EPGChannel currentChannel = null;
        EPGEvent prevEvent = null;
        Map<EPGChannel, List<EPGEvent>> map = Maps.newLinkedHashMap();
        ArrayList<LiveStreamsDBModel> allChannels = new LiveStreamDBHandler (this.context).getAllLiveStreasWithCategoryId(catID, "live");
        this.categoryWithPasword = new ArrayList();
        this.liveListDetailUnlcked = new ArrayList();
        this.liveListDetailUnlckedDetail = new ArrayList();
        this.liveListDetailAvailable = new ArrayList();
        this.liveListDetail = new ArrayList();
        if (this.liveStreamDBHandler.getParentalStatusCount() <= 0 || allChannels == null) {
            this.liveListDetailAvailable = allChannels;
        } else {
            this.listPassword = getPasswordSetCategories();
            if (this.listPassword != null) {
                this.liveListDetailUnlckedDetail = getUnlockedCategories(allChannels, this.listPassword);
            }
            this.liveListDetailAvailable = this.liveListDetailUnlckedDetail;
        }
        if (this.liveListDetailAvailable != null) {
            int k = 0;
            int i = 0;
            EPGChannel currentChannel2 = null;
            while (i < this.liveListDetailAvailable.size()) {
                try {
                    String channelName = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getName();
                    String channelID = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getEpgChannelId();
                    String streamIcon = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getStreamIcon();
                    String streamID = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getStreamId();
                    String num = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getNum();
                    String epgChannelId = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getEpgChannelId();
                    if (!channelID.equals("")) {
                        ArrayList<XMLTVProgrammePojo> xmltvProgrammePojos = this.liveStreamDBHandler.getEPG(channelID);
                        Long epgTempStop = null;
                        if (!(xmltvProgrammePojos == null || xmltvProgrammePojos.size() == 0)) {
                            currentChannel = new EPGChannel (streamIcon, channelName, k, streamID, num, epgChannelId);
                            k++;
                            if (firstChannel == null) {
                                firstChannel = currentChannel;
                            }
                            if (prevChannel != null) {
                                currentChannel.setPreviousChannel(prevChannel);
                                prevChannel.setNextChannel(currentChannel);
                            }
                            prevChannel = currentChannel;
                            List<EPGEvent> epgEvents = new ArrayList();
                            map.put(currentChannel, epgEvents);
                            for (int j = 0; j < xmltvProgrammePojos.size(); j++) {
                                long starttesting1;
                                long endtesting1;
                                EPGEvent ePGEvent;
                                String startDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStart();
                                String stopDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStop();
                                String Title = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getTitle();
                                String Desc = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getDesc();
                                Long epgStartDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(startDateTime));
                                Long epgStopDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(stopDateTime));
                                EPGEvent r20;
                                if (epgTempStop != null && epgStartDateToTimestamp.equals(epgTempStop)) {
                                    EPGEvent epgEvent = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
                                    if (prevEvent != null) {
                                        epgEvent.setPreviousEvent(prevEvent);
                                        prevEvent.setNextEvent(epgEvent);
                                    }
                                    prevEvent = epgEvent;
                                    currentChannel.addEvent(epgEvent);
                                    epgEvents.add(epgEvent);
                                } else if (epgTempStop != null) {
                                    try {
                                        r20 = new EPGEvent(currentChannel, epgTempStop.longValue(), epgStartDateToTimestamp.longValue(), this.context.getResources().getString(R.string.no_information), streamIcon, "");
                                        if (prevEvent != null) {
                                            r20.setPreviousEvent(prevEvent);
                                            prevEvent.setNextEvent(r20);
                                        }
                                        prevEvent = r20;
                                        currentChannel.addEvent(r20);
                                        epgEvents.add(r20);
                                        r20 = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
                                        if (prevEvent != null) {
                                            r20.setPreviousEvent(prevEvent);
                                            prevEvent.setNextEvent(r20);
                                        }
                                        prevEvent = r20;
                                        currentChannel.addEvent(r20);
                                        epgEvents.add(r20);
                                    } catch (Throwable th) {
                                        ex = th;
                                    }
                                } else {
                                    r20 = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
                                    if (prevEvent != null) {
                                        r20.setPreviousEvent(prevEvent);
                                        prevEvent.setNextEvent(r20);
                                    }
                                    prevEvent = r20;
                                    currentChannel.addEvent(r20);
                                    epgEvents.add(r20);
                                }
                                epgTempStop = epgStopDateToTimestamp;
                                long nowTime = System.currentTimeMillis();
                                if (j == xmltvProgrammePojos.size() - 1 && epgTempStop.longValue() < nowTime) {
                                    starttesting1 = epgTempStop.longValue();
                                    endtesting1 = starttesting1 + Long.parseLong("7200000");
                                    for (int l = 0; l < 50; l++) {
                                        ePGEvent = new EPGEvent(currentChannel, starttesting1, endtesting1, this.context.getResources().getString(R.string.no_information), streamIcon, "");
                                        if (prevEvent != null) {
                                            ePGEvent.setPreviousEvent(prevEvent);
                                            prevEvent.setNextEvent(ePGEvent);
                                        }
                                        prevEvent = ePGEvent;
                                        currentChannel.addEvent(ePGEvent);
                                        epgEvents.add(ePGEvent);
                                        starttesting1 = endtesting1;
                                        endtesting1 = starttesting1 + Long.parseLong("7200000");
                                    }
                                }
                                if (j == 0 && epgStartDateToTimestamp.longValue() > nowTime) {
                                    starttesting1 = nowTime - Long.parseLong("86400000");
                                    endtesting1 = epgStartDateToTimestamp.longValue();
                                    for (int m = 0; m < 50; m++) {
                                        ePGEvent = new EPGEvent(currentChannel, starttesting1, endtesting1, this.context.getResources().getString(R.string.no_information), streamIcon, "");
                                        if (prevEvent != null) {
                                            ePGEvent.setPreviousEvent(prevEvent);
                                            prevEvent.setNextEvent(ePGEvent);
                                        }
                                        prevEvent = ePGEvent;
                                        currentChannel.addEvent(ePGEvent);
                                        epgEvents.add(ePGEvent);
                                        starttesting1 = endtesting1;
                                        endtesting1 = starttesting1 + Long.parseLong("7200000");
                                    }
                                }
                            }
                            continue;
                            i++;
                            currentChannel2 = currentChannel;
                        }
                    }
                    currentChannel = currentChannel2;
                    i++;
                    currentChannel2 = currentChannel;
                } catch (Throwable th2) {
                    ex = th2;
                    currentChannel = currentChannel2;
                }
            }
            currentChannel = currentChannel2;
        }
        if (currentChannel != null) {
            currentChannel.setNextChannel(firstChannel);
        }
        if (firstChannel != null) {
            firstChannel.setPreviousChannel(currentChannel);
        }
        return new EPGDataImpl(map);
        throw new RuntimeException(ex.getMessage(), ex);
    }

    private ArrayList<LiveStreamsDBModel> getUnlockedCategories(ArrayList<LiveStreamsDBModel> liveListDetail, ArrayList<String> listPassword) {
        Iterator it = liveListDetail.iterator();
        while (it.hasNext()) {
            LiveStreamsDBModel user1 = (LiveStreamsDBModel) it.next();
            boolean flag = false;
            Iterator it2 = listPassword.iterator();
            while (it2.hasNext()) {
                if (user1.getCategoryId().equals((String) it2.next())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.liveListDetailUnlcked.add(user1);
            }
        }
        return this.liveListDetailUnlcked;
    }

    private ArrayList<String> getPasswordSetCategories() {
        this.categoryWithPasword = this.liveStreamDBHandler.getAllPasswordStatus();
        if (this.categoryWithPasword != null) {
            Iterator it = this.categoryWithPasword.iterator();
            while (it.hasNext()) {
                PasswordStatusDBModel listItemLocked = (PasswordStatusDBModel) it.next();
                if (listItemLocked.getPasswordStatus().equals("1")) {
                    this.listPassword.add(listItemLocked.getPasswordStatusCategoryId());
                }
            }
        }
        return this.listPassword;
    }
}
