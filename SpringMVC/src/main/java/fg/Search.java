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
 * Servlet implementation class Search
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Search() {
		super();
		// TODO Auto-generated constructor stub
	}
	private static Connection connection;
	private static String sqlName = "com.mysql.cj.jdbc.Driver",
			url = "jdbc:mysql://localhost:3306/share?serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false",
			userName = "root",
			password = "930516",
			query1 = "select * from moment where sharetext like \"%",
			query2 = "%\"";

	private static PreparedStatement queryUN;
	static {
		connection = getDBConnection(sqlName, url, userName, password);
	}
	public static Connection getDBConnection(String sqlName,String url,String user,String password) {
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
		String search = request.getParameter("search");
		PrintWriter writer = response.getWriter();
		ResultSet resultSet;
		ArrayList<Moment> moments = new ArrayList<>();
		JSONArray result ;
		try {
			queryUN = connection.prepareStatement(query1+search+query2);
			resultSet = queryUN.executeQuery();
			while (resultSet.next()) {
				String username = resultSet.getString("username");
				String sharetext = resultSet.getString("sharetext");
				ArrayList<String> imglist = parseStringToArray(resultSet.getString("imglist"));
				ArrayList<String> videolist = parseStringToArray(resultSet.getString("videolist"));
				long publishtime = Long.parseLong(resultSet.getString("publishtime"));
				String commentStr = resultSet.getString("commentlist");
				int good = Integer.parseInt(resultSet.getString("good"));
				ArrayList<Comment> comments = parseComments(commentStr);
				String type = resultSet.getString("type");
				Moment moment = new Moment(username, sharetext, publishtime, videolist, imglist, comments, type,good);
				moments.add(moment);
			}
			result = new JSONArray(moments);
			writer.print(result.toString());
			writer.flush();
			
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		finally {
			writer.close();
		}
	}
	private ArrayList<Comment> parseComments(String commentStr) {
		// TODO �Զ����ɵķ������
		JSONArray array = new JSONArray(commentStr);
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
	public ArrayList<String> parseStringToArray(String source){
		JSONArray array = new JSONArray(source);
		ArrayList<String> res = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			String temp = array.getString(i);
			System.out.println(temp);
			res.add(temp);
		}
		return res;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
