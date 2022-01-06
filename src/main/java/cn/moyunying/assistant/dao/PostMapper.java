package cn.moyunying.assistant.dao;

import cn.moyunying.assistant.entity.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {

    int insertPost(Post post);
}
