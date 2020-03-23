package controller;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import dao.MomentDao;
import dao.UserDao;
import fg.Comment;
import model.Moment;
@RestController
public class MomentController {
	@Resource(name = "momentdao")
	private MomentDao momentDao;
	@GetMapping("/template1")
	public String function1() {
		return "";
	}
	@GetMapping("/QueryMoment")
	public List<Moment> queryMoment(String type) {
		return momentDao.selectType(type);
	}
	@GetMapping("/QueryPersonMoment")
	public List<Moment> queryPersonMoment(String username) {
		return momentDao.selectType(username);
	}
	@GetMapping("/Search")
	public List<Moment> search(String text) {
		return momentDao.search(text);
	}
	
	@GetMapping("/UploadComment")
	public String  uploadComment(String text) {
		int i =  momentDao.uploadComment(text);
		if (i==1) {
			return "success";
		}
		return "fail";
	}
	
	@GetMapping("/UploadMoment")
	public String  uploadMoment(Moment moment) {
		int i =  momentDao.insert(moment);
		if (i==1) {
			return "success";
		}
		return "fail";
	}
}
