package com.pop_al.poptv.miscelleneious;

import android.support.v7.widget.RecyclerView;

import com.intrusoft.sectionedrecyclerview.Section;

import java.util.List;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class SectionHeader implements Section<Child> {
    ArrayList<LiveStreamsDBModel> channelAvailable;
    RecyclerView childList;
    private List<Child> list;
    String sectionText;
    private SubCategoriesChildAdapter subCategoriesChildAdapter;

    public SectionHeader(RecyclerView childList, String sectionText, ArrayList<LiveStreamsDBModel> channelAvailable, SubCategoriesChildAdapter subCategoriesChildAdapter, List<Child> list) {
        this.childList = childList;
        this.sectionText = sectionText;
        this.channelAvailable = channelAvailable;
        this.subCategoriesChildAdapter = subCategoriesChildAdapter;
        this.list = list;
    }

    public String getSectionText() {
        return this.sectionText;
    }

    public List<Child> getChildItems() {
        return this.list;
    }

    public List<LiveStreamsDBModel> channelSelcted() {
        return this.channelAvailable;
    }
}