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

/**
 * Servlet implementation class LoadMsg
 */
@WebServlet("/LoadMsg")
public class LoadMsg extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoadMsg() {
		super();
		// TODO Auto-generated constructor stub
	}
	private static Connection connection;
	private static String sqlName = "com.mysql.cj.jdbc.Driver",
			url = "jdbc:mysql://localhost:3306/share?serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false",
			userName = "root",
			password = "930516",
			queryStr = "select * from msg where  (fromUser = ? and toUser = ?) or (fromUser = ? and toUser = ?)";
	private static PreparedStatement query;
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
		String username1 = request.getParameter("username1");
		String username2 = request.getParameter("username2");

		try {
			query = connection.prepareStatement(queryStr);
			query.setString(1, username1);
			query.setString(2, username2);
			query.setString(3, username2);
			query.setString(4, username1);
			ResultSet set = query.executeQuery();
			ArrayList<Msg> msgs = new ArrayList<Msg>();
			Msg msg;
			String  tContent,tFrom,tTo;
			long tTime;
			PrintWriter writer = response.getWriter();
			while(set.next()) {
				tContent = set.getString("content");
				tFrom = set.getString("fromUser");
				tTo = set.getString("toUser");
				tTime = set.getLong("time");
				msg = new Msg(tFrom, tTo, tContent, tTime);
				msgs.add(msg);
			}
			if(msgs.size()>0) {
				String data = parseJSON(msgs);
				writer.print(data);
				writer.flush();
				writer.close();
			}
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	public static String parseJSON(ArrayList<Msg> msgs) {
		JSONArray array = new JSONArray(msgs);
		return array.toString();
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
