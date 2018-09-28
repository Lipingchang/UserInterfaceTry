package com.example.userinterfacetry;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MasterHomeRelatedToLock extends Fragment {
    private Button related_btn ;
    private TextView info_textview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_master_home_related_to_lock,container,false);
        related_btn = v.findViewById(R.id.id_master_home_related_to_lock_btn1);
        info_textview = v.findViewById(R.id.id_master_home_related_to_lock_textview1);
        related_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if( action == MotionEvent.ACTION_BUTTON_PRESS){// 按下
                    MainActivity.DoingRegister = true;
                    info_textview.setText("靠近锁!");
                }else{
                    MainActivity.DoingRegister = false;
                    info_textview.setText("");
                }
                return true;
            }
        });
        return inflater.inflate(R.layout.fragment_master_home_related_to_lock, container, false);
    }

}
