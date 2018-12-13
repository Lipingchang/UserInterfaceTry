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
import com.example.userinterfacetry.model.OrderStatus;
import com.example.userinterfacetry.model.Orientation;
import com.example.userinterfacetry.model.TimeLineModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.userinterfacetry.model.Orientation.HORIZONTAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserLogListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private Orientation mOrientation = HORIZONTAL;
    private boolean mWithLinePadding;

    public UserLogListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_log_list, container, false);
//    }




//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);

//        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        if(getSupportActionBar()!=null)
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mOrientation = (Orientation) getIntent().getSerializableExtra(MainActivity.EXTRA_ORIENTATION);
//        mWithLinePadding = getIntent().getBooleanExtra(MainActivity.EXTRA_WITH_LINE_PADDING, false);

//        setTitle(mOrientation == Orientation.HORIZONTAL ? getResources().getString(R.string.horizontal_timeline) : getResources().getString(R.string.vertical_timeline));

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
//        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

        initView();
        return v;
    }

//    private LinearLayoutManager getLinearLayoutManager() {
//        if (mOrientation == Orientation.HORIZONTAL) {
//            return new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        } else {
//            return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        }
//    }

    private void initView() {
        setDataListItems();
        mTimeLineAdapter =  new TimeLineAdapter(mDataList, mOrientation, mWithLinePadding);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private void setDataListItems(){
        mDataList.add(new TimeLineModel("Item successfully delivered", "", OrderStatus.INACTIVE));
        mDataList.add(new TimeLineModel("Courier is out to delivery your order", "2017-02-12 08:00", OrderStatus.ACTIVE));
        mDataList.add(new TimeLineModel("Item has reached courier facility at New Delhi", "2017-02-11 21:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Item has been given to the courier", "2017-02-11 18:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Item is packed and will dispatch soon", "2017-02-11 09:30", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order is being readied for dispatch", "2017-02-11 08:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order processing initiated", "2017-02-10 15:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order confirmed by seller", "2017-02-10 14:30", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order placed successfully", "2017-02-10 14:00", OrderStatus.COMPLETED));
    }

}
