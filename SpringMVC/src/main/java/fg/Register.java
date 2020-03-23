package fg;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
/**
 * Servlet implementation class r
 */
@WebServlet("/r")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }
    private static Connection connection;
	private static String sqlName = "com.mysql.cj.jdbc.Driver",
			url = "jdbc:mysql://localhost:3306/share?serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false",
			userName = "root",
			password = "930516",
			queryUsername = "select username from user ",
			insert = "insert into user(username,password) values ( ? , ?)";
	private static PreparedStatement queryUN,insertUN;
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
	public static byte[] EncryptionStrBytes(String str, String algorithm) {
        // ����֮�������ֽ�����
        byte[] bytes = null;
        try {
            // ��ȡMD5�㷨ʵ�� �õ�һ��md5����ϢժҪ
            MessageDigest md = MessageDigest.getInstance(algorithm);
            //���Ҫ���м���ժҪ����Ϣ
            md.update(str.getBytes());
            //�õ���ժҪ
            bytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("�����㷨: "+ algorithm +" ������: ");
        }
        return null==bytes?null:bytes;
    }
    /**
     * ���ֽ�����ת�����ַ�������
     * @param bytes
     * @return
     */
    public static String BytesConvertToHexString(byte [] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte aByte : bytes) {
          String s=Integer.toHexString(0xff & aByte);
            if(s.length()==1){
                sb.append("0"+s);
            }else{
                sb.append(s);
            }
        }
        return sb.toString();
    }
    /**
     * ���ü����㷨�����ַ�������
     * @param str   ��Ҫ���ܵ�����
     * @param algorithm ���õļ����㷨
     * @return �ֽ�����
     */
    public static String EncryptionStr(String str, String algorithm) {
        // ����֮�������ֽ�����
        byte[] bytes = EncryptionStrBytes(str,algorithm);
        return BytesConvertToHexString(bytes);
    }
    public boolean insertUser(PreparedStatement insertUN, String username, String password) {
		// TODO �Զ����ɵķ�����	
    	int row;
    	try {
			
			insertUN.setString(1, username);
			insertUN.setString(2, password);
			row = insertUN.executeUpdate();
			int i=0;
			while(i<10) {
				if(row == 1)
					return true;
				row = insertUN.executeUpdate();
				i++;
			}
			return false;
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
    	return false;
	}
    public static boolean isEmpty(String username,String password) {
    	if( (username!=null) && (password!=null)) {
    		if( (username.length()>0) && (password.length()>0))
    			return false;
    	}
    	return true;
    }
    public void createDir(String username) {
    	String path = "E:/xampp/htdocs/share/";
    	File file = new File(path+username);
    	if (!file.exists()) {
			file.mkdir();
		}
    	
    	File f1 = new File(path+username+"/usericon.jpg");
    	File f2 = new File(path+username+"/background.jpg");
    	FileInputStream inputStream;
    	FileOutputStream outputStream;
    	int l;
    	byte[] bs = new byte[1024];
		try {
			inputStream = new FileInputStream(path+"default/default_usericon.png");
			outputStream = new FileOutputStream(f1);
	    	
	    	while( (l = inputStream.read(bs)) != -1) {
	    		outputStream.write(bs);
	    	}
	    	outputStream.flush();
	    	outputStream.close();
	    	
	    	inputStream = new FileInputStream(path+"default/default_userbg.png");
	    	outputStream = new FileOutputStream(f2);
	    	while( (l = inputStream.read(bs)) != -1) {
	    		outputStream.write(bs);
	    	}
	    	outputStream.flush();
	    	outputStream.close();
	    	
		} catch (FileNotFoundException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
    	
    	
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(!isEmpty( username, password)) {
			System.out.println(username+password);
			ResultSet resultSet;
			String temp;
			int index;
			boolean flag ;
			String result;
			PrintWriter writer = response.getWriter();
			ArrayList<String> names = new ArrayList<String>();
			try {
				queryUN = connection.prepareStatement(queryUsername);
				resultSet = queryUN.executeQuery();
				while(resultSet.next()) {
					temp = resultSet.getString("username");
					names.add(temp);
				}
				index = names.indexOf(username);
				if(index == -1) {
					insertUN = connection.prepareStatement(insert);
					password = EncryptionStr(password, "MD5");
					flag = insertUser(insertUN,username,password);
					if(flag) {
						createDir(username);
						result = "success";
					}
					else {
						result = "fail";
					}
				}
				else {
					result = "exist";
				}
				writer.println(result);
				writer.flush();
				writer.close();
			} catch (SQLException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
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
