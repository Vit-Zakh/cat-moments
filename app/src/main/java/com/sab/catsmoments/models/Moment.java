package com.sab.catsmoments.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Moment implements Parcelable {
    private String title;
    private String description;
    private String imageUrl;
    private String userId;
    private String catName;
    private Timestamp timeAdded;

    public Moment() {
    }

    public Moment(String title, String description, String imageUrl, String userId, String catName,
                  Timestamp timeAdded) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.catName = catName;
        this.timeAdded = timeAdded;
    }

    protected Moment(Parcel in) {
        title = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        userId = in.readString();
        catName = in.readString();
        timeAdded = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<Moment> CREATOR = new Creator<Moment>() {
        @Override
        public Moment createFromParcel(Parcel in) {
            return new Moment(in);
        }

        @Override
        public Moment[] newArray(int size) {
            return new Moment[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeString(userId);
        dest.writeString(catName);
        dest.writeParcelable(timeAdded, flags);
    }
}
