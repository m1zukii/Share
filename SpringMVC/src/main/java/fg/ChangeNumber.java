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
 * Servlet implementation class ChangeNumber
 */
@WebServlet("/ChangeNumber")
public class ChangeNumber extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeNumber() {
        super();
        // TODO Auto-generated constructor stub
    }
    private static Connection connection;
   	private static String sqlName = "com.mysql.cj.jdbc.Driver",
   			url = "jdbc:mysql://localhost:3306/share?serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false",
   			userName = "root",
   			password = "930516",
   			updateStr = "update user set phoneNumber = ? where username = ?",
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
		String username = request.getParameter("username");
		String current= request.getParameter("current");
		PrintWriter writer = response.getWriter();
		try {
			
			update = connection.prepareStatement(updateStr);
			update.setString(1, current);
			update.setString(2, username);
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
				writer.print("success");
				
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
