package fg;

import java.util.ArrayList;

public class Moment {
	private String username,
	shareText,
	type;
	private long publishTime;
	private int good;
	private ArrayList<String> videoLists = new ArrayList<String>();
	private ArrayList<String> imgLists = new ArrayList<String>();
	private ArrayList<Comment> comments = new ArrayList<Comment>();


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
}
