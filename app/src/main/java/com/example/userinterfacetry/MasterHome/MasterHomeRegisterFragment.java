package com.example.userinterfacetry.MasterHome;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.userinterfacetry.MainActivity;
import com.example.userinterfacetry.R;
import com.example.userinterfacetry.utils.UtilTools;
import com.example.userinterfacetry.bean.MasterCard;


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
    public void onStart() {
        super.onStart();
        // 在 绘图完成之后, 设置btn为不能点击状态
        info.okToSubmit(register_btn);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_master_home_register,container,false);
        register_btn = v.findViewById(R.id.id_master_home_register_register_btn);
        mastername_edittext = v.findViewById(R.id.id_master_home_register_name);
        masterpwd_edittext  = v.findViewById(R.id.id_master_home_register_pwd);
        masterrepwd_edittext= v.findViewById(R.id.id_master_home_register_repwd);

        info = new registerInfo();

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
                    MasterCard masterCard = MasterCard.getMasterCardInstance();
                    masterCard.setMaster(info.name,info.pwd);
                    Toast.makeText(MainActivity.mainContext,"register masterCard success",Toast.LENGTH_LONG).show();
                    MasterRegisterOKListener.MasterRegisterOK();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.mainContext,"register master fail",Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }
    public class registerInfo{
        String name,pwd,repwd;
        void okToSubmit(Button b){
            if( name!=null && !name.equals("")
                    && pwd!=null && !pwd.equals("")
                    && repwd!=null && !repwd.equals("")
                    && pwd.equals(repwd)){
                b.setClickable(true);
                b.setBackground(getResources().getDrawable(R.drawable.enabled_btn));

            }else {
                b.setClickable(false);
                b.setBackgroundColor(0x00ffffff);
                b.setBackground(getResources().getDrawable(R.drawable.disabled_btn));

            }
        }
    }


    // 用户注册完成 回调 父Activity中的方法
    public interface RegisterOverListener {
        void MasterRegisterOK();
    }

    RegisterOverListener MasterRegisterOKListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            MasterRegisterOKListener = (RegisterOverListener) context;
        } catch (Exception e) {
            // TODO: handle exception
            throw new ClassCastException(context.toString() + " must implement OnButton2ClickListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        MasterRegisterOKListener = null;
    }
}
