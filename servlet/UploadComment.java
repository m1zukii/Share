package fg;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;



/**
 * Servlet implementation class UploadComment
 */
@WebServlet("/UploadComment")
public class UploadComment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadComment() {
        super();
        // TODO Auto-generated constructor stub
    }
    private static Connection connection;
   	private static String sqlName = "com.mysql.cj.jdbc.Driver",
   			url = "jdbc:mysql://localhost:3306/share?serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false",
   			userName = "ljrtest",
   			password = "asdfzxcv123",
   			queryStr = "select commentlist from moment where ( username = ? and publishtime = ? )",
   			updateStr = "update moment set commentlist = ? where ( username = ? and publishtime = ? )";
   	private static PreparedStatement query,update;
   	static {
   		connection = getDBConnection(sqlName, url, userName, password);
   	}public static Connection getDBConnection(String sqlName,String url,String user,String password) {
   		try {
   			loadDriver(sqlName);
   		} catch (ClassNotFoundException e) {
   			// TODO 自动生成的 catch 块
   			System.out.println("加载驱动失败");
   			return null;
   		}
   		Connection connection;
   		try {
   			connection = DriverManager.getConnection(url,user,password);
   		} catch (SQLException e) {
   			// TODO 自动生成的 catch 块
   			e.printStackTrace();
   			System.out.println("连接数据库失败");
   			return null;
   		}
   		System.out.println("连接数据库成功");
   		return connection;
   	}
   	public static void loadDriver(String sqlName) throws ClassNotFoundException {
   		Class.forName(sqlName);
   	}
   	public static boolean closeConnection(Connection connection) {
   		try {
   			connection.close();
   		} catch (SQLException e) {
   			// TODO 自动生成的 catch 块
   			System.out.println("关闭连接失败");
   			return false;
   		}
   		return true;
   	}
   	public static ArrayList<Comment> parseStringtoArraylist(String source){
   		JSONArray array = new JSONArray(source);
   		Comment comment;
   		ArrayList<Comment> comments = new ArrayList<>();
   		for (int i = 0; i < array.length(); i++) {
   			JSONObject object = array.getJSONObject(i);
   			String username = object.getString("username");
   			String content = object.getString("content");
   			long publishtime = object.getLong("publishTime");
			comment = new Comment(username, content, publishtime);
			comments.add(comment);
		}
   		return comments;
   	}
   	public static String parseListToString(ArrayList<Comment> comments) {
   		JSONArray array = new JSONArray(comments);
   		return array.toString();
   	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String username1 = request.getParameter("username1");
		String username2= request.getParameter("username2");
		long publishtime1 = Long.parseLong(request.getParameter("publishtime1"));
		long publishtime2 = Long.parseLong(request.getParameter("publishtime2"));
		String content = request.getParameter("content");
		ResultSet set;
		PrintWriter writer = response.getWriter();
		try {
			String previous;
			query = connection.prepareStatement(queryStr);
			query.setString(1, username1);
			query.setLong(2, publishtime1);
			set = query.executeQuery();
			ArrayList<Comment> comments ;
			if (set.next()) {
				 previous = set.getString("commentlist");
				 comments = parseStringtoArraylist(previous);
				 Comment comment = new Comment(username2,content,publishtime2);
				 comments.add(comment);
				 String result = parseListToString(comments);
				 update = connection.prepareStatement(updateStr);
				 update.setString(1,result);
				 update.setString(2,username1);
				 update.setLong(3, publishtime1);
				 int count = update.executeUpdate();
				 int time = 0;
				 while(true) {
					 if (count == 1)
						 break;
					 time++;
					 if (time == 10)
						 break;
					 count = update.executeUpdate();
				 }
				 if (count == 1) {
					writer.print("success");
					
				 }
				 else {
					 writer.print("fail");
				}
				 
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			writer.print("fail");
		}finally {
			writer.flush();
			writer.close();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
