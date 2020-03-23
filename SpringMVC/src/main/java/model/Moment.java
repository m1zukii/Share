package model;

public class Moment {
    private String username;

    private String sharetext;

    private Long publishtime;

    private String type;

    private Integer good;

    private String imglist;

    private String videolist;

    private String commentlist;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getSharetext() {
        return sharetext;
    }

    public void setSharetext(String sharetext) {
        this.sharetext = sharetext == null ? null : sharetext.trim();
    }

    public Long getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(Long publishtime) {
        this.publishtime = publishtime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getGood() {
        return good;
    }

    public void setGood(Integer good) {
        this.good = good;
    }

    public String getImglist() {
        return imglist;
    }

    public void setImglist(String imglist) {
        this.imglist = imglist == null ? null : imglist.trim();
    }

    public String getVideolist() {
        return videolist;
    }

    public void setVideolist(String videolist) {
        this.videolist = videolist == null ? null : videolist.trim();
    }

    public String getCommentlist() {
        return commentlist;
    }

    public void setCommentlist(String commentlist) {
        this.commentlist = commentlist == null ? null : commentlist.trim();
    }
}