package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dao.MsgDao;
import dao.UserDao;
import model.Msg;

@RestController
public class MsgController {
	@GetMapping("/template1")
	public String function1() {
		return "";
	}
	@Resource(name="msgdao")
	private MsgDao msgDao;
	@GetMapping("/erke")
	public String function1(Msg msg) {
		int count = msgDao.insert(msg);
		if (count==1) {
			return "success";
		}
		return "fail";
		 
	}
	
	@GetMapping("/lizhien")
	public List<Msg> lizhien(String fromUser,String toUser) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("f1", fromUser);
		map.put("f2", toUser);
		List<Msg>  msgs = msgDao.select1(map );
		return msgs;
	}
}
