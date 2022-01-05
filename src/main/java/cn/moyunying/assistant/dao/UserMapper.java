package cn.moyunying.assistant.dao;

import cn.moyunying.assistant.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User selectById(int id);

    User selectByName(String username);

    int insertUser(User user);

    int updateType(int id, int type);

    int updatePassword(int id, String password);

    int updateHeaderUrl(int id, String headerUrl);
}
