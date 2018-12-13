package com.example.userinterfacetry.bean;

import android.graphics.drawable.Drawable;

import com.example.userinterfacetry.MainActivity;
import com.example.userinterfacetry.R;
import com.example.userinterfacetry.utils.UtilTools;

import java.util.Date;

public class SecondaryCard {
    private static Drawable passIconICON = MainActivity.mainContext.getDrawable(R.drawable.pass);
    private static Drawable forbiddenICON = MainActivity.mainContext.getDrawable(R.drawable.forbiden);
    private String name;
    private Drawable icon;
    private Date lastUseTime;
    public SecondaryCard(String name,Date lastUseTime,boolean passState){
        this.name = name;
        this.lastUseTime = lastUseTime;
        this.icon = passState ? passIconICON : forbiddenICON;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getLastUseTime(){
        return UtilTools.elapsedTime(this.lastUseTime);
    }

    public void setLastUseTime(Date date){
        this.lastUseTime = date;
    }

    public void nowUsed(){
        this.lastUseTime = new Date(System.currentTimeMillis());
    }
}
