package com.example.userinterfacetry;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userinterfacetry.MasterHome.MasterHomeFragment;
import com.example.userinterfacetry.MasterHome.MasterHomeRegisterFragment;
import com.example.userinterfacetry.MasterHome.MasterHomeRelatedToLock;
import com.example.userinterfacetry.MasterHome.UserLogListFragment;
import com.example.userinterfacetry.bean.MasterCard;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MasterHomeFragment.OnFragmentInteractionListener,MasterHomeRegisterFragment.RegisterOverListener{
    public static String LogFileName = "logfile.txt";
    public final static String TAG  = "MainActiviy";
    @Override
    public void MasterRegisterOK(){
        // 刷新内存中的 master
        MasterCard.freshInstance();
        Fragment display = null;
        display = new MasterHomeRelatedToLock();
        tv_title.setText(mainContext.getResources().getText(R.string.related_lock_title));
        currentFragment = display;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.id_framelayout_mainactivity, currentFragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    // 返回关联结果,修改页面:
    public void relateResult(boolean success){
        Fragment ff = getVisiableFragment();
        if(ff.getClass().equals(  MasterHomeRelatedToLock.class)  ){
            if( success ){
                ((MasterHomeRelatedToLock)ff).setRelated();
                currentFragment = new MasterHomeFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.id_framelayout_mainactivity, currentFragment);
                transaction.commit();
                tv_title.setText( "Hi"+MasterCard.getMasterCardInstance().getMasterName() );

            }else
                ((MasterHomeRelatedToLock)ff).setReRelated();
        }else{
            Log.e(TAG,"当前页面不是 关联锁的Fragment!!");
        }
    }

    public Fragment getVisiableFragment() {
        List<Fragment> fragments = fragmentManager.getFragments();
        for(int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if(fragment!=null && fragment.isAdded()&&fragment.isVisible() ) {
               Log.e(TAG,"related fragment:"+fragment.getClass()+" ");
                return fragment;
            }
        }
        return null;
    }



    static public MainActivity mainContext;
    static public boolean DoingRegister = false; // 当 MasterHomeRelatedToLock 中的按钮按下的时候,改成true,这样的话,可以让apduService知道可以把秘钥信息发送出去.

    private TextView tv_title;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    System.out.println( navigation.getSelectedItemId() );

                    Fragment display = null; // 将要展示的fragment:
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            // 检查有没有注册过
                            MasterCard masterCard = MasterCard.getMasterCardInstance();

                            if( ! masterCard.isRegister() ){ // 没有注册过信息
                                tv_title.setText(mainContext.getResources().getText(R.string.register_title));
                                display = new MasterHomeRegisterFragment();
                                Toast.makeText(MainActivity.mainContext,"你还没有初始化你的门卡!",Toast.LENGTH_SHORT).show();
                            }else if( ! masterCard.isHaveLock() ) { // 没有关联门锁
                                tv_title.setText(mainContext.getResources().getText(R.string.related_lock_title));
                                display = new MasterHomeRelatedToLock();
                                Toast.makeText(MainActivity.mainContext,"还没有关联门锁!",Toast.LENGTH_SHORT).show();
                            }else{
                                tv_title.setText( "Hi"+MasterCard.getMasterCardInstance().getMasterName() );
                                display = new MasterHomeFragment();
                            }
                            break;
                        case R.id.navigation_dashboard:
                            tv_title.setText(mainContext.getResources().getText(R.string.guest_card_title));
                            display = new GuestCardListFragment();
                            break;
//                        case R.id.navigation_notifications:
//                            mTextMessage.setText(R.string.title_notifications);
//                            break;
                    }

                    if( display != null ) {
                        display = new UserLogListFragment();
                        currentFragment = display;
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.id_framelayout_mainactivity, currentFragment);
                        transaction.commit();
                    }else {

                    }
                    return true;
                }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        mainContext = this;

        this.tv_title = (TextView)findViewById(R.id.tv_title);
        this.navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

}
