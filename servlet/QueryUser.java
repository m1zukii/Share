package fg;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class QueryUser
 */
@WebServlet("/QueryUser")
public class QueryUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryUser() {
        super();
        // TODO Auto-generated constructor stub
    }
    private static Connection connection;
   	private static String sqlName = "com.mysql.cj.jdbc.Driver",
   			url = "jdbc:mysql://localhost:3306/share?serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false",
   			userName = "ljrtest",
   			password = "asdfzxcv123",
   			selectStr = "select * from user where username = ?";
   	private static PreparedStatement select;
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
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String username = request.getParameter("username");
		PrintWriter writer = response.getWriter();
		try {
			select = connection.prepareStatement(selectStr);
			select.setString(1, username);
			ResultSet set = select.executeQuery();
			String username1 ;
			String phoneNumber;
			if (set.next()) {
				username1 = set.getString("username");
				phoneNumber = set.getString("phoneNumber");
				writer.print("username="+username1+"&phoneNumber="+phoneNumber);
				writer.flush();
			}
			else {
				writer.print("no");
				writer.flush();
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
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
