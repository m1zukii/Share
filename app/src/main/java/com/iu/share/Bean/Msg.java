package com.iu.share.Bean;

//消息实体类
public class Msg {
    private String fromUser, toUser, content;
    private long time;

    public Msg(String fromUser, String toUser, String content, long time) {
        super();
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
        this.time = time;
    }

    public Msg() {
        super();
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String to) {
        this.toUser = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    @Override
    public String toString() {
        return "Msg [fromUser=" + fromUser + ", toUser=" + toUser + ", content=" + content + ", time=" + time + "]";
    }


}
