package com.pop_al.poptv.miscelleneious;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pop_al.poptv.R;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

public class ChildViewHolder extends RecyclerView.ViewHolder {
    RecyclerView name;

    public ChildViewHolder(View itemView) {
        super(itemView);
        this.name = (RecyclerView) itemView.findViewById(R.id.my_recycler_view);
    }
}