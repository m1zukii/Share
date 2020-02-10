package fg;



import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class erke
 */
@WebServlet("/erke")
public class erke extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public erke() {
        super();
        // TODO Auto-generated constructor stub
    }
    public static Connection connection;
	public static PreparedStatement insert;
	private static String sqlName = "com.mysql.cj.jdbc.Driver",
   			url = "jdbc:mysql://localhost:3306/share?serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false",
   			userName = "ljrtest",
   			password = "asdfzxcv123",
			insertStr = "insert into msg (content,time,fromUser,toUser) values ( ? , ? , ? , ? )";
			
	static {
		connection = getDBConnection(sqlName, url, userName, password);
		
	}
	public static Connection getDBConnection(String sqlName,String url,String user,String password) {
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
	public boolean isEmpty(String from,String to,String content,String time) {
   		if(     (from!=null)&&(from.length()>0)
   				&&(to!=null)&&(to.length()>0)
   				&&(content!=null)&&(content.length()>0)
   				&&(time!=null)&&(time.length()>0)) {
   			return false;
   		}
   		return true;
   	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String fromUser = request.getParameter("fromUser");
		String toUser = request.getParameter("toUser");
		String content = request.getParameter("content");
		String timeStr = request.getParameter("time");
		
		if(!isEmpty(fromUser, toUser, content,timeStr)) {
			long time = Long.valueOf(timeStr);
			try {
				boolean flag = false;
				
				insert = connection.prepareStatement(insertStr);
				insert.setString(1,content);
				insert.setLong(2,time);
				insert.setString(3,fromUser);
				insert.setString(4,toUser);
				
				int res = insert.executeUpdate();
				if(res == 1)
					flag = true;
				DataOutputStream outputStream = new DataOutputStream(response.getOutputStream());
				outputStream.writeBoolean(flag);
				outputStream.flush();
				outputStream.close();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
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
