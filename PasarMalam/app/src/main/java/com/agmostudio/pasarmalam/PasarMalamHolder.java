package com.agmostudio.pasarmalam;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by agmostudio on 09/12/2017.
 */

public class PasarMalamHolder extends RecyclerView.ViewHolder {

    TextView nameTextView;
    TextView hoursTextView;

    public PasarMalamHolder(View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.name);
        hoursTextView = itemView.findViewById(R.id.hours);
    }
}
