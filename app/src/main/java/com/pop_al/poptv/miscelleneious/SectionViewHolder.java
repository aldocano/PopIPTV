package com.pop_al.poptv.miscelleneious;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pop_al.poptv.R;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class SectionViewHolder extends RecyclerView.ViewHolder {
    TextView name;

    public SectionViewHolder(View itemView) {
        super(itemView);
        this.name = (TextView) itemView.findViewById(R.id.section);
    }
}