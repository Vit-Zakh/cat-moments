package com.sab.catsmoments.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class User implements Parcelable {
    private String userId;
    private String catName;

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public User(String userId, String catName) {
        this.userId = userId;
        this.catName = catName;
    }

    protected User(Parcel in) {
        userId = in.readString();
        catName = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(catName);
    }
}
