package dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import model.User;

@Repository("userdao")
public class UserDao implements UserMapper {
	@Resource(name="sqlSession")
	private SqlSession session;

	@Override
	public int deleteByPrimaryKey(String username) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(User record) {
		// TODO Auto-generated method stub
		return session.insert("dao.UserMapper.insert",record);
	}

	@Override
	public User selectByPrimaryKey(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKey(User record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update1(User user) {
		// TODO Auto-generated method stub
		return session.update("dao.UserMapper.update1",user);
	}

	@Override
	public int update2(Map<String, String> map) {
		// TODO Auto-generated method stub
		return session.update("dao.UserMapper.update2",map);
	}

	@Override
	public User login(User record) {
		// TODO Auto-generated method stub
		return session.selectOne("dao.UserMapper.login",record);
	}
	@Override
	public User queryContact(String phoneNumber) {
		// TODO Auto-generated method stub
		return session.selectOne("dao.UserMapper.queryContact",phoneNumber);
	}

	@Override
	public User queryUser(String username) {
		// TODO Auto-generated method stub
		return session.selectOne("dao.UserMapper.queryUser",username);
	}
}
