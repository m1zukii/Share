package controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import dao.UserDao;
import model.User;
@RestController
public class UserController {
	@Resource(name="userdao")
	private UserDao userDao;
	@GetMapping("/template1")
	public String function1() {
		return "";
	}
	@GetMapping("/ChangeNumber")
	public String changeNumber(String username,String phoneNumber) {
		User user = new User();
		user.setUsername(username);
		user.setPhonenumber(phoneNumber);
		int count = userDao.update1(user);
		if (count==1) {
			return "success";
		}
		return "fail";
	}
	
	@GetMapping("/ChangeUsername")
	public String changeUsername(String before,String after) {
		HashMap<String, String>	 map = new HashMap<String, String>();
		map.put("username1", after);
		map.put("username2", before);
		int count = userDao.update2(map);
		if (count==1) {
			return "success";
		}
		return "fail";
	}
	@GetMapping("/b")
	public String b(String username,String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		User tempUser = userDao.login(user);
		if (tempUser!=null) {
			return "success";
		}
		return "fail";
	}
	
	@GetMapping("/QueryContact")
	public User queryContact(String phonenumber) {
		
		return userDao.queryContact(phonenumber);
	}
	@GetMapping("/QueryUser")
	public User queryUser(String username) {
		
		return userDao.queryUser(username);
	}
	
	@GetMapping("/r")
	public String register(String username,String password) {
		User user = userDao.queryUser(username);
		if (user!=null) {
			User user2 = new User();
			user2.setUsername(username);
			user2.setPassword(password);
			int count = userDao.insert(user);
			if (count == 1) {
				return "success";
			}
			return "fail";
		}
		return "fail";
		
	}
	@PostMapping("/bgbg")
	public String uploadImage(HttpServletRequest request, HttpServletResponse response) {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletContext servletContext = request.getServletContext();
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
					//name = ��Ƭ��
					String name = item.getName();
					//fieldname = filename
					String fieldName = item.getFieldName();
					System.out.println(2+name+fieldName);
					File file = new File(path+"/"+username+"/"+name);
					if(!file.exists()) {
						item.write(file);
						return "success";
					}
			    }
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}finally{
			return "fail";
		}
		
	}
	
	@PostMapping("/UploadSingleImage")
	public void uploadSingleImage(HttpServletRequest request, HttpServletResponse response) {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletContext servletContext = request.getServletContext();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> items;
		String username="";
		String type = "";
		PrintWriter writer=null;
		try {
			writer = response.getWriter();
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
					//name = ��Ƭ��
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			writer.print("fail");
			writer.flush();
		}finally {
			
			writer.close();
			
		}
		
	}


}
