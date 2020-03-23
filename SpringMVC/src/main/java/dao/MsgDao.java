package dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import model.Msg;
@Repository("msgdao")
public class MsgDao implements MsgMapper{
	@Resource(name="sqlSession")
	private SqlSession session;
	@Override
	public int insert(Msg record) {
		// TODO Auto-generated method stub
		return session.insert("dao.MsgMapper.insert",record);
	}

	@Override
	public List<Msg> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Msg> select1(Map<String, String> map) {
		// TODO Auto-generated method stub
		return session.selectList("dao.MsgMapper.select1",map);
	}

}
