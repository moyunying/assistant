package cn.moyunying.assistant.dao;

import cn.moyunying.assistant.entity.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {

    int insertPost(Post post);

    List<Post> selectPosts(int offset, int limit);

    int selectTotal(int userId);

    List<Post> selectPostByUserId(int userId,int offset, int limit);
}
