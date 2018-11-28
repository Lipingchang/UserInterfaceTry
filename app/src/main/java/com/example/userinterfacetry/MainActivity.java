package com.example.userinterfacetry;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userinterfacetry.bean.Master;

public class MainActivity extends AppCompatActivity implements MasterHomeFragment.OnFragmentInteractionListener,MasterHomeRegisterFragment.RegisterOverListener{
    @Override
    public void MasterRegisterOK(){
        // 刷新内存中的 master
        Master.freshInstance();
        Fragment display = null;
        // 把关联锁的页面替换上去
        display = new MasterHomeRelatedToLock();
        currentFragment = display;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.id_framelayout_mainactivity, currentFragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    static public Context mainContext;
    static public boolean DoingRegister = false; // 当 MasterHomeRelatedToLock 中的按钮按下的时候,改成true,这样的话,可以让apduService知道可以把秘钥信息发送出去.

    private TextView mTextMessage;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    mTextMessage.setText("");
                    Fragment display = null; // 将要展示的fragment:
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            // 检查有没有注册过
                            Master master = Master.getMasterInstance();

                            if( ! master.isRegister() ){ // 没有注册过信息
                                display = new MasterHomeRegisterFragment();
                                Toast.makeText(MainActivity.mainContext,"你还没有初始化你的门卡!",Toast.LENGTH_SHORT).show();
                            }else if( ! master.isHaveLock() ) { // 没有关联门锁
                                display = new MasterHomeRelatedToLock();
                                Toast.makeText(MainActivity.mainContext,"还没有关联门锁!",Toast.LENGTH_SHORT).show();
                            }else{
                                display = new MasterHomeFragment();
                            }
                            break;
                        case R.id.navigation_dashboard:
                            //mTextMessage.setText(R.string.title_dashboard);
                            display = new GuestCardListFragment();
                            break;
//                        case R.id.navigation_notifications:
//                            mTextMessage.setText(R.string.title_notifications);
//                            break;
                    }

                    if( display != null ) {
                        currentFragment = display;
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.id_framelayout_mainactivity, currentFragment);
                        transaction.commit();
                    }else {

                    }
                    return false;
                }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        mainContext = this.getApplicationContext();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

}
