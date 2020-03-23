package dao;

import java.util.List;
import java.util.Map;

import model.User;

public interface UserMapper {
    int deleteByPrimaryKey(String username);

    int insert(User record);

    User selectByPrimaryKey(String username);

    List<User> selectAll();

    int updateByPrimaryKey(User record);
    int update1(User user);
    int update2(Map<String, String> map);
    User login(User record);

	User queryContact(String phoneNumber);
	User queryUser(String username);
	
}