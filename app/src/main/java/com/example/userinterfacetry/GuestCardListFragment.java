package com.example.userinterfacetry;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.userinterfacetry.bean.GuestCard;
import com.example.userinterfacetry.bean.GuestCardManager;
import com.example.userinterfacetry.bean.SecondaryCard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class GuestCardListFragment extends Fragment {
    private List<SecondaryCard> mAppList;//=getApplicationContext().getPackageManager().getInstalledApplications(0);

    private AppAdapter mAdapter = new AppAdapter();
    private SwipeMenuListView listView;

    public GuestCardListFragment() {
        // Required empty public constructor
    }
    Context getApplicationContext(){
        return MainActivity.mainContext;
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)(MainActivity.mainContext)).MasterHomeRegisterSetTitle(
                MainActivity.mainContext.getString(R.string.guest_card_title)
                        + GuestCardManager.guestCardList.size()
                        +"张");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mAppList = new ArrayList<>();
        List<GuestCard> cards = GuestCardManager.guestCardList;
        for( GuestCard c : cards ){
            mAppList.add(new SecondaryCard(c.mastername+" 给 "+c.cardName ,c.startDate,c.expireDate,new Date(0),c.isValidCard()));
        }

        View view = inflater.inflate(R.layout.fragment_guest_card_list, container, false);
        listView = view.findViewById(R.id.listView);
        listView.setAdapter(this.mAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(getApplicationContext().getResources().getColor(R.color.colorPrimary)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle(getApplicationContext().getResources().getString(R.string.menue_detail));
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(70));
                // set a icon
                deleteItem.setIcon(R.drawable.trashbin);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        System.out.println(0);
                        break;
                    case 1:
                        System.out.println(1);
                        mAppList.remove(index-1);
                        GuestCardManager.guestCardList.remove(index-1);
                        mAdapter.notifyDataSetChanged();

                        // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        return view;
    }


    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public SecondaryCard getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            SecondaryCard item = getItem(position);
            holder.iv_icon.setImageDrawable(item.getIcon());
            holder.tv_name.setText(item.getName());
//            holder.tv_last_used_time.setText(item.getLastUseTime());
            holder.tv_last_used_time.setText( item.getValidityRange() );

            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_name;
            TextView tv_last_used_time;

            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_last_used_time = (TextView) view.findViewById(R.id.tv_last_use_time);
                view.setTag(this);
            }
        }
    }
}
