package com.example.userinterfacetry.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.userinterfacetry.bean.UserLog;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineModel {

    private String mMessage;
    private String mDate;
    private UserLog.PassType mStatus;
    private UserLog.UserType userType;

    public TimeLineModel() {
    }

    public TimeLineModel(String mMessage, String mDate, UserLog.PassType mStatus, UserLog.UserType type) {
        this.mMessage = mMessage;
        this.mDate = mDate;
        this.mStatus = mStatus;
        this.userType = type;
    }

    public UserLog.UserType getUserType() {
        return userType;
    }

    public void setUserType(UserLog.UserType userType) {
        this.userType = userType;
    }

    public String getMessage() {
        return mMessage;
    }

    public void semMessage(String message) {
        this.mMessage = message;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public UserLog.PassType getStatus() {
        return mStatus;
    }

    public void setStatus(UserLog.PassType mStatus) {
        this.mStatus = mStatus;
    }

}
