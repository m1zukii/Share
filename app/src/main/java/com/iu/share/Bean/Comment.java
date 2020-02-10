package com.iu.share.Bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
    private String username;
    private String content;
    private long publishTime;

    public Comment() {
    }

    protected Comment(Parcel in) {
        username = in.readString();
        content = in.readString();
        publishTime = in.readLong();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public Comment(String username, String content, long publishTime) {
        this.username = username;
        this.content = content;
        this.publishTime = publishTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(content);
        dest.writeLong(publishTime);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", publishTime=" + publishTime +
                '}';
    }
}
