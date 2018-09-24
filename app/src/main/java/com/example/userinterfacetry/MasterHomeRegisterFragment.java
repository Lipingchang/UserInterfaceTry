package com.example.userinterfacetry;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class MasterHomeRegisterFragment extends Fragment {


    private Button register_btn;
    private EditText mastername_edittext,masterpwd_edittext,masterrepwd_edittext;



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

        return v;
    }

}
