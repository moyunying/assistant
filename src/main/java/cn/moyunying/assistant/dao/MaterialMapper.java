package cn.moyunying.assistant.dao;

import org.apache.ibatis.annotations.Mapper;
import cn.moyunying.assistant.entity.Material;

import java.util.List;

@Mapper
public interface MaterialMapper {
    int insertMaterial(Material material);

    List<Material> selectMaterialsByKeyword(String keyword, int offset, int limit);

    int selectTotalByKeyword(String keyword);
}
