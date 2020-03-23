package dao;

import java.util.List;
import java.util.Map;

import model.Msg;

public interface MsgMapper {
    int insert(Msg record);

    List<Msg> selectAll();
    List<Msg> select1(Map<String, String> map);
}