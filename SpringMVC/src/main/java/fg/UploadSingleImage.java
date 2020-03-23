package fg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;



/**
 * Servlet implementation class UploadSingleImage
 */
@WebServlet("/UploadSingleImage")
public class UploadSingleImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadSingleImage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletContext servletContext = this.getServletConfig().getServletContext();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items;
		String username="";
		String type = "";
		PrintWriter writer = response.getWriter();
		try {
			items = upload.parseRequest(request);
			for (FileItem item : items) {
				if (item.isFormField()) {
					String temp = item.getFieldName();
					if (temp.equals("type")) {
						type = item.getString();
					}
					else {
						 username = item.getString();
					}
			       
			      
			    } else {
			    	String path = "D:/Apache/Apache24/htdocs";
					//name = 照片名
					String name = item.getName();
					//fieldname = filename
					String fieldName = item.getFieldName();
					System.out.println(2+name+fieldName);
					File file = new File(path+"/"+username+"/"+name);
					if(!file.exists()) {
						item.write(file);
						
					}
					String temp = "";
					if (name.endsWith(".jpg")) {
						temp = ".jpg";
					}
					else if (name.endsWith("jpeg")) {
						temp = ".jpeg";
					}
					else if (name.endsWith("png")) {
						temp = ".png";
					}
					else if (name.endsWith("gif")) {
						temp = ".gif";
					}
					String aString = "";
					if (type.equals("icon")) {
						aString = "usericon";
					}
					else if (type.equals("background")) {
						aString = "background";
					}
					File file2 = new File(path+"/"+username+"/"+aString+".jpg");
					if (!file2.exists()) {
						file2.createNewFile();
					}
					FileInputStream inputStream = new FileInputStream(file);
					FileOutputStream outputStream = new FileOutputStream(file2, false);
					int a;
					while((a = inputStream.read()) != -1) {
						outputStream.write(a);
					}
					writer.print("success");
					writer.flush();
					outputStream.close();
					inputStream.close();
			    }
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
			writer.print("fail");
			writer.flush();
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			writer.print("fail");
			writer.flush();
		}finally {
			
			writer.close();
		}
	}

}
