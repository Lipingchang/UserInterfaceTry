package com.example.userinterfacetry.MasterHome;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.userinterfacetry.MainActivity;
import com.example.userinterfacetry.MasterHome.SendSecondaryCard.range.RangeActivity;
import com.example.userinterfacetry.MasterHome.loglist.UserLogListFragment;
import com.example.userinterfacetry.MasterHome.setting.PerferenceActivity;
import com.example.userinterfacetry.R;
import com.example.userinterfacetry.bean.MasterCard;
import com.example.userinterfacetry.bean.GuestCardManager;

import java.util.Date;



public class MasterHomeFragment extends Fragment implements View.OnClickListener{

    private Button bt_history,bt_send_card,bt_change_pwd;

    private OnFragmentInteractionListener mListener;
    public MasterHomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_master_home, container, false);
        bt_history = (Button)v.findViewById(R.id.id_master_home_history_btn);
        bt_send_card = (Button)v.findViewById(R.id.id_master_home_send_card_btn);
        bt_change_pwd = (Button)v.findViewById(R.id.id_master_home_changepwd_btn);
        bt_change_pwd.setOnClickListener(this);
        bt_send_card.setOnClickListener(this);
        bt_history.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_master_home_history_btn:
                mListener.addAFragment(new UserLogListFragment());
                break;
            case R.id.id_master_home_changepwd_btn:
                startActivity( PerferenceActivity.class );
                break;
            case R.id.id_master_home_send_card_btn:
                RangeActivity.show(MainActivity.mainContext);
                try {
                    System.out.println( GuestCardManager.generateCryptoCard("lipc",new Date(1),new Date(1000)) );
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    private void startActivity( Class<?> cls){
        Intent startMain = new Intent(getContext(),cls);
        startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mListener.MasterHomeRegisterSetTitle( "Hi!  "+ MasterCard.getMasterCardInstance().getMasterName());
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        <T extends Fragment> void addAFragment(T f);
        void MasterHomeRegisterSetTitle(String s);

    }
}
