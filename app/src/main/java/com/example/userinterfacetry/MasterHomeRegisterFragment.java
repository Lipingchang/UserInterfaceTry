package com.example.userinterfacetry;


import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class MasterHomeRegisterFragment extends Fragment {
    static private String TAG = "MasterHomeRegisterFragment";


    private Button register_btn;
    private EditText mastername_edittext,masterpwd_edittext,masterrepwd_edittext;
    private registerInfo info;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_master_home_register,container,false);
        register_btn = v.findViewById(R.id.id_master_home_register_register_btn);
        mastername_edittext = v.findViewById(R.id.id_master_home_register_name);
        masterpwd_edittext  = v.findViewById(R.id.id_master_home_register_pwd);
        masterrepwd_edittext= v.findViewById(R.id.id_master_home_register_repwd);

        info = new registerInfo();
        info.okToSubmit(register_btn);

        mastername_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( (!s.equals("")) && s!=null)
                    info.name = s.toString();
                else
                    info.name = null;
                info.okToSubmit(register_btn);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        masterpwd_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( UtilTools.MasterPwdCheck(s.toString())){
                    // set icon to green;
                    masterpwd_edittext.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_green_accept_25dp,0);
                    info.pwd = s.toString();
                }else{
                    // set icon to red:
                    masterpwd_edittext.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_red_reject_25dp,0);
                    info.pwd = null;
                }
                info.okToSubmit(register_btn);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        masterrepwd_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwd = masterpwd_edittext.getText().toString();
                String repwd = s.toString();
                if( pwd.equals(repwd)) {
                    masterrepwd_edittext.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_green_accept_25dp, 0);
                    info.repwd = s.toString();
                }else{
                    masterrepwd_edittext.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_red_reject_25dp,0);
                    info.repwd = null;
                }
                info.okToSubmit(register_btn);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity.master.setMaster(info.name,info.pwd);
                }catch (Exception e){

                }
            }
        });

        return v;
    }
    public class registerInfo{
        String name,pwd,repwd;
        void okToSubmit(Button b){
            if( name!=null && pwd!=null && repwd!=null && pwd.equals(repwd)){
                b.setClickable(true);
                b.setBackground(getResources().getDrawable(R.drawable.enabled_btn));

            }else {
                b.setClickable(false);
                b.setBackgroundColor(0x00ffffff);
                b.setBackground(getResources().getDrawable(R.drawable.disabled_btn));

            }
        }
    }

}
