package com.iu.share.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Moment implements Parcelable {
    private String username,
                   shareText,
                   type;
    private long publishTime;
    private int good;
    private ArrayList<String> videoLists = new ArrayList<String>();
    private ArrayList<String> imgLists = new ArrayList<String>();
    private ArrayList<Comment> comments = new ArrayList<Comment>();


    protected Moment(Parcel in) {
        username = in.readString();
        shareText = in.readString();
        type = in.readString();
        publishTime = in.readLong();
        good = in.readInt();
        videoLists = in.createStringArrayList();
        imgLists = in.createStringArrayList();
        comments = in.createTypedArrayList(Comment.CREATOR);
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

    @Override
    public String toString() {
        return "Moment{" +
                "username='" + username + '\'' +
                ", shareText='" + shareText + '\'' +
                ", type='" + type + '\'' +
                ", publishTime=" + publishTime +
                ", videoLists=" + videoLists +
                ", imgLists=" + imgLists +
                ", comments=" + comments +
                '}';
    }




    public Moment() {
    }

    public Moment(String username, String shareText, long publishTime, ArrayList<String> videoLists, ArrayList<String> imgLists, ArrayList<Comment> comments,String type,int good) {
        this.username = username;
        this.shareText = shareText;
        this.publishTime = publishTime;
        this.videoLists = videoLists;
        this.imgLists = imgLists;
        this.comments = comments;
        this.type = type;
        this.good = good;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getVideoLists() {
        return videoLists;
    }

    public void setVideoLists(ArrayList<String> videoLists) {
        this.videoLists = videoLists;
    }

    public ArrayList<String> getImgLists() {
        return imgLists;
    }

    public void setImgLists(ArrayList<String> imgLists) {
        this.imgLists = imgLists;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(shareText);
        dest.writeString(type);
        dest.writeLong(publishTime);
        dest.writeInt(good);
        dest.writeStringList(videoLists);
        dest.writeStringList(imgLists);
        dest.writeTypedList(comments);
    }
}
