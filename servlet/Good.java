package fg;
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
 * Servlet implementation class Good
 */
@WebServlet("/Good")
public class Good extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Good() {
		super();
		// TODO Auto-generated constructor stub
	}
	private static Connection connection;
	private static String sqlName = "com.mysql.cj.jdbc.Driver",
			url = "jdbc:mysql://localhost:3306/share?serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false",
			userName = "ljrtest",
			password = "asdfzxcv123",
			addStr = "update moment set good = (good + 1) where (username = ? and publishtime = ?)",
			minusStr = "update moment set good = (good - 1) where (username = ? and publishtime = ?)";
	private static PreparedStatement statement;
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
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String username = request.getParameter("username");
		String publishtime = request.getParameter("publishtime");
		String flag = request.getParameter("flag");
		boolean a = false;
		if (flag.equals("true")) {
			a = handleGood(addStr,username,publishtime);
		}
		else if (flag.equals("false")) {
			a = handleGood(minusStr,username,publishtime);
		}
		PrintWriter writer = response.getWriter();
		if (a) {
			writer.print("success");
		}
		else {
			writer.print("false");
		}
		writer.flush();
		writer.close();
	}
	private boolean handleGood(String str,String username,String publishtime) {
		try {
			statement = connection.prepareStatement(str);
			statement.setString(1, username);
			statement.setLong(2, Long.parseLong(publishtime));
			int count = statement.executeUpdate();
			int time = 0;
			while(true) {
				if (count == 1)
					break;
				time++;
				if (time == 10)
					break;
				count = statement.executeUpdate();
			}
			if (count == 1) 
				return true;
			else 
				return false;
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
