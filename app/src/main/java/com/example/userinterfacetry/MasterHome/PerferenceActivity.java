package com.example.userinterfacetry.MasterHome;

import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.Toast;

import com.example.userinterfacetry.R;
import com.example.userinterfacetry.bean.MasterCard;

public class PerferenceActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener,Preference.OnPreferenceClickListener
{

    static final String TAG="PreferenceActivity";

    SharedPreferences preference;
    EditTextPreference oldpasswordcheck_et;
    EditTextPreference newmasterpwd_et;
    EditTextPreference newmastername_et;
    SwitchPreference logpassdata_s;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.perferences);

        preference = PreferenceManager.getDefaultSharedPreferences(this);
        oldpasswordcheck_et = (EditTextPreference) findPreference(getString(R.string.settings_old_pwd_check));
        newmasterpwd_et = (EditTextPreference) findPreference(getString(R.string.settings_new_master_pwd));
        newmastername_et = (EditTextPreference) findPreference(getString(R.string.settings_new_master_name));
        logpassdata_s = (SwitchPreference) findPreference(getString(R.string.settings_log_pass_data));

        oldpasswordcheck_et.setOnPreferenceChangeListener(this);
        logpassdata_s.setOnPreferenceChangeListener(this);
        newmasterpwd_et.setOnPreferenceChangeListener(this);
        newmastername_et.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if( key.equals(getString(R.string.settings_log_pass_data)) ){
            Toast.makeText(this,getString(R.string.change_settings_success),Toast.LENGTH_SHORT);
            return true;
        }

        if( key.equals(getString(R.string.settings_old_pwd_check)) ){

            String pwd = oldpasswordcheck_et.getEditText().getText().toString();
            oldpasswordcheck_et.getEditor().clear().apply();
            if( pwd.equals(MasterCard.getMasterCardInstance().getMasterPwd()) ) {
                newmastername_et.setEnabled(true);
                newmasterpwd_et.setEnabled(true);
                PreferenceScreen screen = getPreferenceScreen();
                PreferenceGroup group = (PreferenceGroup) screen.findPreference(getString(R.string.settings_card_data_settings));
                group.removePreference(oldpasswordcheck_et);
                return false;
            }else{
                Toast.makeText(this,getString(R.string.change_settings_fail),Toast.LENGTH_SHORT).show();
                return false;
            }

        }

        try {
            if (key.equals(getString(R.string.settings_new_master_name))) {
                String name = newmastername_et.getEditText().getText().toString();

                MasterCard.getMasterCardInstance().setUnRelate();
                MasterCard.getMasterCardInstance().setName(name);
                Toast.makeText(this,getString(R.string.change_settings_success), Toast.LENGTH_SHORT).show();
                return true;
            }
            if (key.equals(getString(R.string.settings_new_master_pwd))) {
                String pwd = newmasterpwd_et.getEditText().getText().toString();

                MasterCard.getMasterCardInstance().setUnRelate();
                MasterCard.getMasterCardInstance().setPwd(pwd);
                Toast.makeText(this,getString(R.string.change_settings_success), Toast.LENGTH_SHORT).show();
                return true;
            }
        }catch (Exception e){
            Toast.makeText(this,getString(R.string.change_settings_fail), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }
}
