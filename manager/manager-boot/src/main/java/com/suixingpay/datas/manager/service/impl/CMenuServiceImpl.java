/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.CMenu;
import com.suixingpay.datas.manager.core.mapper.CMenuMapper;
import com.suixingpay.datas.manager.service.CMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单目录表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class CMenuServiceImpl implements CMenuService {

    @Autowired
    private CMenuMapper cmenuMapper;

    @Override
    public CMenu menuTree(String fatherCode, String roleCode) {
        //查询该权限下所有的menu集合
        //List<CMenu> menuList = cmenuMapper.findByFatherCodeAndRoleCode(fatherCode, roleCode);
        List<CMenu> menulist = cmenuMapper.findAll();
        return parentMenu(menulist);
    }

    @Override
    public CMenu findAll() {
        List<CMenu> menulist = cmenuMapper.findAll();
        return parentMenu(menulist);
    }

    @Override
    public Integer insert(CMenu cMenu) {
        return cmenuMapper.insert(cMenu);
    }

    @Override
    public List<CMenu> findByFatherCode(String fatherCode) {
        return cmenuMapper.findByFatherCode(fatherCode);
    }

    @Override
    public Integer update(Long id, CMenu cMenu) {
        return cmenuMapper.update(id, cMenu);
    }

    @Override
    public CMenu findById(Long id) {
        return cmenuMapper.findById(id);
    }

    @Override
    public Integer delete(Long id) {
        return cmenuMapper.delete(id);
    }

    //菜单树
    private CMenu parentMenu(List<CMenu> menulist) {

        CMenu parentMenu = new CMenu(0);
        parentMenu.setMenus(menus("-1", listtomap(menulist)));

        return parentMenu;
    }

    //组装map
    private Map<String, List<CMenu>> listtomap(List<CMenu> menulist) {
        Map<String, List<CMenu>> menuMap = new HashMap<>();

        for (CMenu cMenu : menulist) {
            List<CMenu> menus = menuMap.get(cMenu.getFathercode());
            if (menus == null) {
                menus = new ArrayList<>();
            }
            menus.add(cMenu);
            menuMap.put(cMenu.getFathercode(), menus);
        }

        return menuMap;
    }

    //递归方法
    private List<CMenu> menus(String fatherCode, Map<String, List<CMenu>> menuMap) {
        List<CMenu> childlist = menuMap.get(fatherCode);
        if (childlist == null || childlist.size() == 0) {
            return null;
        }
        //根据父类id查询旗下子类
        for (CMenu cMenu : childlist) {
            if (cMenu == null || cMenu.getIsleaf() == null || cMenu.getIsleaf() == 1) {
                continue;
            }
            cMenu.setMenus(menus(cMenu.getCode(), menuMap));
        }
        return childlist;

    }

}
























