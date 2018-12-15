package com.example.userinterfacetry.MasterHome.SendSecondaryCard.range;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 基类
 * Created by huanghaibin on 2017/11/16.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected void initWindow() {

    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();


}
