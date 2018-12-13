package com.example.userinterfacetry.MasterHome;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.userinterfacetry.MainActivity;
import com.example.userinterfacetry.R;


public class MasterHomeRelatedToLock extends Fragment {
    private android.support.v7.widget.AppCompatButton related_btn ;
    private TextView info_textview;
    private View layout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_master_home_related_to_lock,container,false);
        this.layout = v;
        return inflater.inflate(R.layout.fragment_master_home_related_to_lock, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        info_textview = getActivity().findViewById(R.id.id_master_home_related_to_lock_textview1);
        related_btn =  getActivity().findViewById(R.id.id_master_home_related_to_lock_btn1);

        related_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    info_textview.setText("靠近门锁");
                    MainActivity.DoingRegister = true;
                } else if (event.getAction() == MotionEvent.ACTION_UP){
                    info_textview.setText("按住Button之后贴近门锁来认证");
                    MainActivity.DoingRegister = false;
                }
                related_btn.performClick();
                return true;
            }
        });
    }
    public void setRelated(){
        related_btn.setClickable(false);
        MainActivity.DoingRegister = false;

        info_textview.setText("关联成功");
    }
    public void setReRelated(){
        related_btn.setClickable(false);
        MainActivity.DoingRegister = false;

        info_textview.setText("关联失败");
        related_btn.setClickable(true);
    }



}