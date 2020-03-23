package dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import model.Moment;

@Repository("momentdao")
public class MomentDao implements MomentMapper {
	@Resource(name = "sqlSession")
	private SqlSession session;

	@Override
	public int insert(Moment record) {
		// TODO Auto-generated method stub
		return session.insert("dao.MomentMapper.insert",record);
	}

	@Override
	public List<Moment> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Moment> selectType(String type) {
		// TODO Auto-generated method stub
		return session.selectList("dao.MomentMapper.selectType", type);
	}

	@Override
	public List<Moment> selectPerson(String username) {
		// TODO Auto-generated method stub
		return session.selectList("dao.MomentMapper.selectPerson", username);
	}

	@Override
	public List<Moment> search(String text) {
		// TODO Auto-generated method stub
		return session.selectList("dao.MomentMapper.search", text);
	}

	@Override
	public int uploadComment(String text) {
		// TODO Auto-generated method stub
		return session.update("dao.MomentMapper.uploadComment", text);
	}
}
