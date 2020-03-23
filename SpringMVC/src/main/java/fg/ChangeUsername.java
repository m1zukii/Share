package fg;

import java.io.File;
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
 * Servlet implementation class ChangeUsername
 */
@WebServlet("/ChangeUsername")
public class ChangeUsername extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangeUsername() {
		super();
		// TODO Auto-generated constructor stub
	}
	private static Connection connection;
	private static String sqlName = "com.mysql.cj.jdbc.Driver",
			url = "jdbc:mysql://localhost:3306/share?serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false",
			userName = "root",
			password = "930516",
			query = " select * from user where username = ?",
			updateStr = "update user set username = ? where username = ?",
			updateStr1 = "update moment set username = ? where username = ?";
	private static PreparedStatement update,update1;
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
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String last = request.getParameter("last");
		String current= request.getParameter("current");
		PrintWriter writer = response.getWriter();
		try {
			PreparedStatement query1 = connection.prepareStatement(query);
			query1.setString(1, current);
			ResultSet set = query1.executeQuery();
			if (set.next()) {
				writer.print("fail");
				writer.flush();
				writer.close();
				return ;
			}
			update = connection.prepareStatement(updateStr);
			update.setString(1, current);
			update.setString(2, last);
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
				change(last,current);
				writer.print("success");
				updateMoment(last,current);
			}
			else {
				writer.print("fail");
			}
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}finally {
			writer.flush();
			writer.close();
		}

	}
	public void change(String before,String after) {
		String base = "D:/Apache/Apache24/htdocs/";
		String s1 = base+before;
		File f1 = new File(s1);
		String s2 = base + after;
		File f2 = new File(s2);
		f1.renameTo(f2);
	}

	public  void updateMoment(String last, String current) {
		// TODO �Զ����ɵķ������
		try {
			update1 = connection.prepareStatement(updateStr1);
			update1.setString(1, current);
			update1.setString(2, last);
			update1.executeUpdate();
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
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
