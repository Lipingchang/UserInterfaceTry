package com.example.userinterfacetry.MasterHome.loglist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.userinterfacetry.R;
import com.example.userinterfacetry.bean.UserLog;
import com.example.userinterfacetry.model.Orientation;
import com.example.userinterfacetry.model.TimeLineModel;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    private List<TimeLineModel> mFeedList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;

    public TimeLineAdapter(List<TimeLineModel> feedList, Orientation orientation, boolean withLinePadding) {
        mFeedList = feedList;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;

        if(mOrientation == Orientation.HORIZONTAL) {
            view = mLayoutInflater.inflate(mWithLinePadding ? R.layout.item_timeline_horizontal_line_padding : R.layout.item_timeline_horizontal, parent, false);
        } else {
            view = mLayoutInflater.inflate(mWithLinePadding ? R.layout.item_timeline_line_padding : R.layout.item_timeline, parent, false);
        }

        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

        TimeLineModel timeLineModel = mFeedList.get(position);

        if(timeLineModel.getStatus() == UserLog.PassType.Reject_e) {
            holder.mTimelineView.setMarker(this.mContext.getDrawable(R.drawable.forbiden));
        } else if (timeLineModel.getStatus() == UserLog.PassType.Pass_e ) {
            holder.mTimelineView.setMarker(this.mContext.getDrawable(R.drawable.pass));
        }

        if(!timeLineModel.getDate().isEmpty()) {
            holder.mDate.setVisibility(View.VISIBLE);
            holder.mDate.setText(timeLineModel.getDate());
        }
        else
            holder.mDate.setVisibility(View.GONE);

        switch (timeLineModel.getUserType()) {
            case Guest_e:
                holder.card_view.setBackgroundColor(this.mContext.getColor(R.color.guestColor));
                break;
            case Master_e:
                holder.card_view.setBackgroundColor(this.mContext.getColor(R.color.masterColor));
                break;
            case Unknow_e:
                holder.card_view.setBackgroundColor(this.mContext.getColor(R.color.unknowUserColor));
                break;

        }

        holder.mMessage.setText(timeLineModel.getMessage());
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }

}
