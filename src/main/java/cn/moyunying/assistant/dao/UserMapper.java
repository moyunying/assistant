package cn.moyunying.assistant.dao;

import cn.moyunying.assistant.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User selectByName(String username);

    int insertUser(User user);

    int updateType(int id, int type);
}
