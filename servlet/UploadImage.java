package fg;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
 * Servlet implementation class bgbg
 */
@WebServlet("/bgbg")
public class UploadImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadImage() {
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
		// TODO Auto-generated method stub
		//doGet(request, response);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletContext servletContext = this.getServletConfig().getServletContext();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items;
		String username="";
		try {
			items = upload.parseRequest(request);
			for (FileItem item : items) {
				if (item.isFormField()) {
			        username = item.getString();
			        System.out.println("1"+username);
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
			    }
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
}
