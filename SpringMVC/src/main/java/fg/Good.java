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

import org.apache.ibatis.annotations.Select;
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
			userName = "root",
			password = "930516",
			selectStr = "select good from moment where username = ? and publishtime = ?",
			addStr = "update moment set good = (good + 1) where (username = ? and publishtime = ? and good = ?)  ",
			minusStr = "update moment set good = (good - 1) where (username = ? and publishtime = ? and good = ?) ";
	private static PreparedStatement statement,statement2;
	static {
		connection = getDBConnection(sqlName, url, userName, password);
	}public static Connection getDBConnection(String sqlName,String url,String user,String password) {
		try {
			loadDriver(sqlName);
		} catch (ClassNotFoundException e) {
			// TODO �Զ����ɵ� catch ��
			System.out.println("��������ʧ��");
			return null;
		}
		Connection connection;
		try {
			connection = DriverManager.getConnection(url,user,password);
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			System.out.println("�������ݿ�ʧ��");
			return null;
		}
		System.out.println("�������ݿ�ɹ�");
		return connection;
	}
	public static void loadDriver(String sqlName) throws ClassNotFoundException {
		Class.forName(sqlName);
	}
	public static boolean closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			System.out.println("�ر�����ʧ��");
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
		int before = 0;
		try {
			
			statement2 = connection.prepareStatement(selectStr);
			statement2.setString(1, username);
			statement2.setLong(2, Long.parseLong(publishtime));
			ResultSet set = statement2.executeQuery();
			if (set.next()) {
				before = set.getInt("good");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean a = false;
		if (flag.equals("true")) {
			a = handleGood(addStr,username,publishtime,before);
		}
		else if (flag.equals("false")) {
			a = handleGood(minusStr,username,publishtime,before);
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
	private boolean handleGood(String str,String username,String publishtime, int before) {
		try {
			statement = connection.prepareStatement(str);
			statement.setString(1, username);
			statement.setLong(2, Long.parseLong(publishtime));
			statement.setInt(3, before);
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
			// TODO �Զ����ɵ� catch ��
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
