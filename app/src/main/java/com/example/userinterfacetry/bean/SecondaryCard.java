package com.example.userinterfacetry.bean;

import android.graphics.drawable.Drawable;

import com.example.userinterfacetry.MainActivity;
import com.example.userinterfacetry.R;
import com.example.userinterfacetry.utils.UtilTools;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class SecondaryCard {
    private static Drawable passIconICON = MainActivity.mainContext.getDrawable(R.drawable.pass);
    private static Drawable forbiddenICON = MainActivity.mainContext.getDrawable(R.drawable.forbiden);
    private String name;
    private Drawable icon;
    private Date lastUseTime;
    private Date start,end;
    public SecondaryCard(String name,Date start,Date end, Date lastUseTime,boolean passState){
        this.start = start;
        this.end = end;
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
        if( this.lastUseTime.getTime() == 0 ){
            return "没用过";
        }
        return UtilTools.elapsedTime(this.lastUseTime);
    }
    public String getValidityRange(){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(start);
        date += " ~ " + simpleDateFormat.format(end);
        return date;
    }

    public void setLastUseTime(Date date){
        this.lastUseTime = date;
    }

    public void nowUsed(){
        this.lastUseTime = new Date(System.currentTimeMillis());
    }
}
