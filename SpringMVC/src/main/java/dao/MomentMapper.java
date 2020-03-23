package dao;

import java.util.List;
import model.Moment;

public interface MomentMapper {
    int insert(Moment record);
    
    List<Moment> selectAll();
    List<Moment> selectType(String type);
    List<Moment> selectPerson(String username);
    List<Moment> search(String text);
    int uploadComment(String text);
}