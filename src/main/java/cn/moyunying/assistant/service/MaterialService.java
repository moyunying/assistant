package cn.moyunying.assistant.service;

import cn.moyunying.assistant.dao.MaterialMapper;
import cn.moyunying.assistant.dao.UserMapper;
import cn.moyunying.assistant.entity.LoginTicket;
import cn.moyunying.assistant.entity.Material;
import cn.moyunying.assistant.entity.User;
import cn.moyunying.assistant.util.AssistantUtil;
import cn.moyunying.assistant.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MaterialService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MaterialMapper materialMapper;

    public Map<String,Object> shareMaterial(String cookie, String title, String content, String format, String base64) {
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

        String key = AssistantUtil.generateUUID() + "." + format;
        String pictureUrl = UploadUtil.put64image(key, base64);
        if (pictureUrl == null) {
            map.put("code", 1);
            map.put("msg", "图片上传失败！");
            return map;
        }

        Material material = new Material();
        material.setTitle(title);
        material.setContent(content);
        material.setPictureUrl(pictureUrl);
        material.setCreateTime(new Date());
        materialMapper.insertMaterial(material);

        map.put("code", 0);
        map.put("msg", "分享景点成功！");
        return map;
    }

    public Map<String, Object> getMaterials() {
        Map<String, Object> map = new HashMap<>();

        List<Material> list = materialMapper.selectMaterials();

        if (list.isEmpty()) {
            map.put("code", 1);
            map.put("msg", "获取景点列表失败！");
            return map;
        }

        List<Map<String, Object>> materials = new ArrayList<>();
        for (Material material : list) {
            Map<String, Object> materialInfo = new HashMap<>();
            materialInfo.put("id", material.getId());
            materialInfo.put("title", material.getTitle());
            materialInfo.put("pictureUrl", material.getPictureUrl());
            materials.add(materialInfo);
        }
        map.put("materials", materials);

        map.put("code", 0);
        map.put("msg", "获取景点列表成功！");
        return map;
    }

    public Map<String, Object> getMaterialById(int id) {
        Map<String, Object> map = new HashMap<>();

        Material material = materialMapper.selectMaterialById(id);

        if (material == null) {
            map.put("code", 1);
            map.put("msg", "获取景点信息失败！");
            return map;
        }

        map.put("material", material);

        map.put("code", 0);
        map.put("msg", "获取景点信息成功！");
        return map;
    }
}
