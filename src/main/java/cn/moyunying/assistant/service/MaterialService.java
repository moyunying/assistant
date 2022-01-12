package cn.moyunying.assistant.service;

import cn.moyunying.assistant.dao.MaterialMapper;
import cn.moyunying.assistant.dao.PostMapper;
import cn.moyunying.assistant.dao.UserMapper;
import cn.moyunying.assistant.entity.LoginTicket;
import cn.moyunying.assistant.entity.Material;
import cn.moyunying.assistant.entity.Post;
import cn.moyunying.assistant.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MaterialService {
    @Autowired
    private UserService userService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MaterialMapper materialMapper;

    public Map<String, Object> getMaterial(String keyword, int page) {
        Map<String, Object> map = new HashMap<>();

        int limit = 10;
        int offset = (page - 1) * limit;
        List<Material> list = materialMapper.selectMaterialsByKeyword(keyword, offset, limit);

        map.put("keyword",keyword);
        if (list.isEmpty()) {
            map.put("code", 1);
            map.put("msg", "获取景点列表失败！");
            return map;
        }

        List<Map<String, Object>> materials = new ArrayList<>();
        for (Material material : list) {
            Map<String, Object> materialInfo = new HashMap<>();

            materialInfo.put("title", material.getTitle());
            materialInfo.put("content", material.getContent());
            materialInfo.put("createTime", material.getCreateTime());
            materials.add(materialInfo);
        }
        map.put("materials", materials);
        map.put("page", page);
        map.put("total", materialMapper.selectTotalByKeyword(keyword) / limit + 1);

        map.put("code", 0);
        map.put("msg", "获取景点列表成功！");
        return map;
    }

    public Map<String,Object> shareMaterial(String cookie, String title, String content, String keyword) {
        Map<String, Object> map = new HashMap<>();

        LoginTicket loginTicket = userService.isOnline(cookie);
        if (loginTicket == null) {
            map.put("code", 1);
            map.put("msg", "没有登录！");
            return map;
        }

        User user = userMapper.selectById(loginTicket.getUserId());
        if (user.getType() != 2)
        {
            map.put("code",1);
            map.put("msg", "没有权限！");
            return map;
        }

        Material material = new Material();
        material.setTitle(title);
        material.setContent(content);
        material.setCreateTime(new Date());
        material.setKeyword(keyword);
        materialMapper.insertMaterial(material);

        map.put("code", 0);
        map.put("msg", "分享景点成功！");
        return map;
    }
}

