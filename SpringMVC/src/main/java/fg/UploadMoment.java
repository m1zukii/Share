package fg;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
 * Servlet implementation class UploadMoment
 */
@WebServlet("/UploadMoment")
public class UploadMoment extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadMoment() {
        super();
        // TODO Auto-generated constructor stub
    }
    public static ArrayList<String> parseArray(String array){
    	JSONArray array2 = new JSONArray(array);
    	ArrayList<String> result = new ArrayList<String>();
    	for (int i = 0; i < array2.length(); i++) {
			result.add(array2.getString(i));
		}
    	return result;
    }
    private static Connection connection;
   	private static String sqlName = "com.mysql.cj.jdbc.Driver",
   			url = "jdbc:mysql://localhost:3306/share?serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false",
   			userName = "root",
   			password = "930516",
   			insertStr = "insert into moment(username,sharetext,publishtime,imglist,videolist,type,commentlist,good) values (?,?,?,?,?,?,?,?)";
   	private static PreparedStatement insert;
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
   	public static String createComment(){
   		ArrayList<Comment> comments = new ArrayList<>();
   		JSONArray array = new JSONArray(comments);
   		return array.toString();
   	}
   	public static boolean insertMoment(PreparedStatement insert,String username,String publishtime,String imglist,
   			String videolist,String sharetext,String type) {
   		int row;
   		try {
			insert.setString(1, username);
			insert.setString(2, sharetext);
			insert.setString(3, publishtime);
			insert.setString(4, imglist);
			insert.setString(5, videolist);
			insert.setString(6, type);
			insert.setString(7, createComment());
			insert.setLong(8, 0);
			row = insert.executeUpdate();
			for (int i = 0; i < 10; i++) {
				if (row == 1) {
					return true;
				}
				row = insert.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
   		return false;
   	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String username = request.getParameter("username");
		String publishtime = request.getParameter("publishtime");
		String type = request.getParameter("type");
		String imglist = request.getParameter("imglists");
		String videolist = request.getParameter("videolists");
		String sharetext = request.getParameter("sharetext");
		PrintWriter writer = new PrintWriter(response.getWriter());
		try {
			insert = connection.prepareStatement(insertStr);
			boolean flag = insertMoment(insert, username, publishtime, imglist, videolist, sharetext,type);
			if (flag) {
				writer.println("success");
			} else {
				writer.println("fail");
			}
			writer.flush();
			writer.close();
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
