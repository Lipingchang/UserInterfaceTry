package com.example.userinterfacetry.MasterHome;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.userinterfacetry.R;
import com.github.vipulasri.timelineview.*;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineViewHolder extends RecyclerView.ViewHolder {

    TextView mDate;
    TextView mMessage;
    TimelineView mTimelineView;
    CardView card_view;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        card_view = (CardView)itemView.findViewById(R.id.card_view);
        mDate = (TextView)itemView.findViewById(R.id.text_timeline_date);
        mMessage = (TextView)itemView.findViewById(R.id.text_timeline_title);
        mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);
        mTimelineView.initLine(viewType);
    }
}
