package com.example.userinterfacetry.MasterHome;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.userinterfacetry.R;
import com.example.userinterfacetry.bean.UserLog;
import com.example.userinterfacetry.model.OrderStatus;
import com.example.userinterfacetry.model.Orientation;
import com.example.userinterfacetry.model.TimeLineModel;
import com.example.userinterfacetry.utils.UtilTools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.example.userinterfacetry.model.Orientation.HORIZONTAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserLogListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private Orientation mOrientation = Orientation.VERTICAL;

    public UserLogListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_timeline, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

        initView();
        return v;
    }

    private LinearLayoutManager getLinearLayoutManager() {
        if (mOrientation == Orientation.HORIZONTAL) {
            return new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            return new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        }
    }

    private void initView() {
        setDataListItems();
        mTimeLineAdapter =  new TimeLineAdapter(mDataList, mOrientation, false);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private void setDataListItems(){
        // 从日志类中拿数据
        Collections.sort(UserLog.logList, new Comparator<UserLog>() {
            @Override
            public int compare(UserLog o1, UserLog o2) {
                return (int)(o2.time.getTime()-o1.time.getTime());
            }
        });

        for( UserLog log : UserLog.logList ){
            String s = String.format("’%s‘在%s",log.name, UtilTools.elapsedTime(log.time));
            TimeLineModel m = new TimeLineModel(s,log.time.toGMTString(),log.pass,log.user);
            mDataList.add(m);
        }


    }

}
