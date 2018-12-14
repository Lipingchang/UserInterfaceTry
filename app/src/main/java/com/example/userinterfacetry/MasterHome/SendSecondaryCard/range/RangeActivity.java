package com.example.userinterfacetry.MasterHome.SendSecondaryCard.range;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userinterfacetry.AesCBC;
import com.example.userinterfacetry.R;
import com.example.userinterfacetry.bean.GuestCardManager;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class RangeActivity extends AppCompatActivity implements
        CalendarView.OnCalendarRangeSelectListener,
        View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initData();
    }

    TextView mTextLeftDate;
    TextView mTextLeftWeek;
    EditText mEditUserName;
    TextView mTextRightDate;
    TextView mTextRightWeek;


    CalendarView mCalendarView;

    private int mCalendarHeight;

    public static void show(Context context) {
        context.startActivity(new Intent(context, RangeActivity.class));
    }


    protected int getLayoutId() {
        return R.layout.activity_range;
    }

    @SuppressLint("SetTextI18n")
    protected void initView() {
        mTextLeftDate = (TextView) findViewById(R.id.tv_left_date);
        mTextLeftWeek = (TextView) findViewById(R.id.tv_left_week);
        mTextRightDate = (TextView) findViewById(R.id.tv_right_date);
        mTextRightWeek = (TextView) findViewById(R.id.tv_right_week);
        mEditUserName = (EditText) findViewById(R.id.et_user_name);

        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setOnCalendarRangeSelectListener(this);

        findViewById(R.id.iv_clear).setOnClickListener(this);
        findViewById(R.id.iv_reduce).setOnClickListener(this);
        findViewById(R.id.iv_increase).setOnClickListener(this);
        findViewById(R.id.tv_commit).setOnClickListener(this);

        mCalendarHeight = dipToPx(this, 46);

        mCalendarView.setRange(mCalendarView.getCurYear(), mCalendarView.getCurMonth(), mCalendarView.getCurDay(),
                mCalendarView.getCurYear() + 2, 12, 31);
    }

    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear:
                mCalendarView.clearSelectRange();
                mTextLeftWeek.setText("开始日期");
                mTextRightWeek.setText("结束日期");
                mTextLeftDate.setText("");
                mTextRightDate.setText("");
                break;
            case R.id.iv_reduce:

                mCalendarHeight -= dipToPx(this, 8);
                if (mCalendarHeight <= dipToPx(this, 46)) {
                    mCalendarHeight = dipToPx(this, 46);
                }
                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                break;
            case R.id.iv_increase:
                mCalendarHeight += dipToPx(this, 8);
                if (mCalendarHeight >= dipToPx(this, 90)) {
                    mCalendarHeight = dipToPx(this, 90);
                }
                mCalendarView.setCalendarItemHeight(mCalendarHeight);
                break;
            case R.id.tv_commit:
                try {
                    List<Calendar> calendars = mCalendarView.getSelectCalendarRange();
                    if  (calendars == null || calendars.size() == 0){
                        throw new RuntimeException("时间不完整");
                    }

                    for (Calendar c : calendars) {
                        Date start = new Date(c.getTimeInMillis());
                        System.out.println("start: " + start.toString()); // 获取的时间 的日期是对的， 但是时分秒是根据 当前的时间点的
                    }

                    GregorianCalendar cal = new GregorianCalendar(Locale.CHINA);
                    Date s = new Date( calendars.get(0).getTimeInMillis());
                    cal.setTime(s);
                    cal.set(java.util.Calendar.HOUR_OF_DAY,0);
                    cal.set(java.util.Calendar.SECOND,0);
                    cal.set(java.util.Calendar.MINUTE,0);
                    System.out.println("start date:"+cal.getTime().toString());
                    s = cal.getTime();

                    Date e = new Date(calendars.get(calendars.size()-1).getTimeInMillis());
                    cal.setTime(e);
                    cal.set(java.util.Calendar.HOUR_OF_DAY,23);
                    cal.set(java.util.Calendar.SECOND,59);
                    cal.set(java.util.Calendar.MINUTE,59);
                    System.out.println("end date:"+cal.getTime().toString());
                    e = cal.getTime();

                    String username = mEditUserName.getEditableText().toString().trim();
                    System.out.println(username);

                    if( username ==  null || username.equals("") ){
                        throw new RuntimeException("没有填名字");
                    }

                    String sdata = GuestCardManager.generateCryptoCard(username, s, e);
                    ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText(null, sdata);
                    clipboard.setPrimaryClip(clipData);
                    Toast.makeText(this,"已经赋值到粘贴板！",Toast.LENGTH_LONG).show();
                    this.finish();
                }catch (Exception ee){
                    ee.printStackTrace();
                    Toast.makeText(this,"生成失败"+ee.getMessage(),Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onCalendarSelectOutOfRange(Calendar calendar) {
        // TODO: 2018/9/13 超出范围提示
    }

    @Override
    public void onSelectOutOfRange(Calendar calendar, boolean isOutOfMinRange) {
        Toast.makeText(this,
                calendar.toString() + (isOutOfMinRange ? "小于最小选择范围" : "超过最大选择范围"),
                Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarRangeSelect(Calendar calendar, boolean isEnd) {
        if (!isEnd) {
            mTextLeftDate.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
            mTextLeftWeek.setText(WEEK[calendar.getWeek()]);
            mTextRightWeek.setText("结束日期");
            mTextRightDate.setText("");
        } else {
            mTextRightDate.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
            mTextRightWeek.setText(WEEK[calendar.getWeek()]);
        }
    }

    private static final String[] WEEK = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
}
