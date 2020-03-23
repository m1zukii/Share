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
 * Servlet implementation class QueryPersonMoment
 */
@WebServlet("/QueryPersonMoment")
public class QueryPersonMoment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryPersonMoment() {
        super();
        // TODO Auto-generated constructor stub
    }
    private static Connection connection;
	private static String sqlName = "com.mysql.cj.jdbc.Driver",
			url = "jdbc:mysql://localhost:3306/share?serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false",
			userName = "root",
			password = "930516",
			queryStr = "select * from moment where username = ?";
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
		String username = request.getParameter("username");
		if (username!=null&&username.length()>0) {
			ResultSet resultSet;
			PrintWriter writer = response.getWriter();
			ArrayList<Moment> moments = new ArrayList<>();
			JSONArray result ;
			try {
				query = connection.prepareStatement(queryStr);
				query.setString(1, username);
				resultSet = query.executeQuery();
				while (resultSet.next()) {
					
					String sharetext = resultSet.getString("sharetext");
					ArrayList<String> imglist = parseStringToArray(resultSet.getString("imglist"));
					ArrayList<String> videolist = parseStringToArray(resultSet.getString("videolist"));
					long publishtime = Long.parseLong(resultSet.getString("publishtime"));
					String commentStr = resultSet.getString("commentlist");
					String type = resultSet.getString("type");
					int good = Integer.parseInt(resultSet.getString("good"));
					ArrayList<Comment> comments = parseComments(commentStr);
					Moment moment = new Moment(username, sharetext, publishtime, videolist, imglist, comments, type,good);
					moments.add(moment);
				}
				result = new JSONArray(moments);
				writer.print(result.toString());
				writer.flush();
				writer.close();
			} catch (SQLException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
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
	private ArrayList<String> parseStringToArray(String string) {
		// TODO �Զ����ɵķ������
		JSONArray array = new JSONArray(string);
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
