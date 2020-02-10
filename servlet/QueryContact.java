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
 * Servlet implementation class QueryContact
 */
@WebServlet("/QueryContact")
public class QueryContact extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryContact() {
        super();
        // TODO Auto-generated constructor stub
    }
    private static Connection connection;
   	private static String sqlName = "com.mysql.cj.jdbc.Driver",
   			url = "jdbc:mysql://localhost:3306/share?serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false",
   			userName = "ljrtest",
   			password = "asdfzxcv123",
   			queryStr = "select username,phoneNumber from user where phoneNumber = ? ";
   	private static PreparedStatement query;
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
   	public ArrayList<String> parseResult(String source){
   		JSONArray array = new JSONArray(source);
   		ArrayList<String> temp = new ArrayList<>();
   		for (int i = 0; i < array.length(); i++) {
			String number = array.getString(i);
			temp.add(number);
		}
   		return temp;
   	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String phoneNumbers = request.getParameter("phoneNumber");
		ArrayList<String> numbers = parseResult(phoneNumbers);
		ArrayList<Contact> contacts = new ArrayList<>();
		String number;
		ResultSet set;
		Contact contact;
		PrintWriter writer = response.getWriter();
		try {
			query = connection.prepareStatement(queryStr);
			for (int i = 0; i < numbers.size(); i++) {
				number = numbers.get(i);
				query.setString(1, number);
				set = query.executeQuery();
				if (set.next()) {
					contact = new Contact("", number, set.getString("username"));
					contacts.add(contact);
				}
				
			}
			JSONArray array = new JSONArray(contacts);
			writer.print(array.toString());
			System.out.println(array.toString());
			writer.flush();
			writer.close();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
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
