package fg;

public class Comment {
	private String username;
    private String content;
    private long publishTime;

    public Comment() {
    }

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
}
