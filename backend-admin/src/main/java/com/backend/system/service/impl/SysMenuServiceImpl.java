package com.backend.system.service.impl;

import com.backend.common.ResultData;
import com.backend.enums.SysMenuTypeEnum;
import com.backend.system.converter.SysMenuConverter;
import com.backend.system.dto.SysMenuDto;
import com.backend.system.entity.SysMenu;
import com.backend.system.mapper.SysMenuMapper;
import com.backend.system.service.ISysMenuService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author fengyts
 * @since 2020-08-01
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysMenuConverter converter;

    @Override
    public List<SysMenuDto> getAllMenusByTier(Long userId) {
//        List<SysMenuDto> menuList = generateStaticTestMenus();
        List<SysMenuDto> menuList = baseMapper.selectAllMenusByTier(userId);
        return menuList;
    }

    @Override
    public List<SysMenuDto> getAllMenus(Long userId) {
        List<SysMenuDto> menuList = baseMapper.selectAllMenus(userId);
        return menuList;
    }

    @Override
    public SysMenuDto getMenuById(Long id) {
        SysMenu sysMenu = baseMapper.selectById(id);
        SysMenuDto sysMenuDto = converter.toEntityDto(sysMenu);
        return sysMenuDto;
    }

    @Override
    public ResultData saveOrUpdate(SysMenuDto dto) {
        Long id = dto.getId();
        String name = dto.getMenuName();
        Long parentId = dto.getParentId();
        int type = dto.getType();

        LambdaQueryWrapper<SysMenu> lqw = Wrappers.lambdaQuery();
        lqw.eq(SysMenu::getParentId, parentId).eq(SysMenu::getName, name);
        List<SysMenu> sysMenus = baseMapper.selectList(lqw);
        if (null == id) { // 新增
            if (CollectionUtils.isNotEmpty(sysMenus)) {
                return ResultData.err("存在同名菜单");
            }
            Integer level = getLevel(type, parentId);
            dto.setLevel(level);
        } else { // 修改
            SysMenu sysMenuDb = baseMapper.selectById(id);
            String nameDb = sysMenuDb.getName();
            if (!name.equals(nameDb)) { // 菜单名称有修改, 判断重复
                if (CollectionUtils.isNotEmpty(sysMenus)) {
                    return ResultData.err("存在同名菜单");
                }
            }
            if (type != sysMenuDb.getType()){ //菜单类型有修改
                Integer level = getLevel(type, parentId);
                dto.setLevel(level);
            }
        }
        SysMenu entity = converter.toEntity(dto);
        this.saveOrUpdate(entity);
        return ResultData.ok();
    }

    private int getLevel(int type, Long id){
        Integer level;
        if(SysMenuTypeEnum.DIRECTORY.getCode() == type){
            level = 0;
        } else if (SysMenuTypeEnum.BUTTON.getCode() == type) {
            level = 9;
        } else {
            // 获取父菜单
            SysMenu parentMenu = baseMapper.selectById(id);
            level = parentMenu.getLevel() + 1;
        }
        return level;
    }

    @Override
    public boolean updateSysMenu(SysMenuDto dto) {
        SysMenu sysMenu = converter.toEntity(dto);
        return this.updateById(sysMenu);
    }

    private List<SysMenuDto> generateStaticTestMenus() {
        List<SysMenuDto> topMenus = new ArrayList<>(); // topMenus
        final int LEVEL1 = 1; // top
        final int LEVEL2 = 2; // leftTop
        final int LEVEL3 = 3; // lefSub

        // -top-id -> [1~10], left-top-id -> [11~50], subs-id -> [51~]

        SysMenuDto top1 = generateMenu(0L, LEVEL1, 1L, "控制台", 0, null);
        SysMenuDto top2 = generateMenu(0L, LEVEL1, 2L, "商品管理", 1, null);
        SysMenuDto top3 = generateMenu(0L, LEVEL1, 3L, "系统管理", 2, null);

        // start =====> top1
        // top1 subs
        List<SysMenuDto> top1Subs = new ArrayList<>(); // top1子菜单
        // ++++top1_left_top++++
        // top1_left_top1
        SysMenuDto top1Left1 = generateMenu(top1.getParentId(), LEVEL2, 11L, "k导航菜单一", 0, null);
        List<SysMenuDto> top1Left1Subs = new ArrayList<>(); // left_top_subs
        SysMenuDto top1Left1Sub1 = generateMenu(top1Left1.getId(), LEVEL3, 51L, "k子菜单11", 0, "/test/view1");
        SysMenuDto top1Left1Sub2 = generateMenu(top1Left1.getId(), LEVEL3, 52L, "k子菜单长度长度长度12", 1, "/test/view2");
        top1Left1Subs.add(top1Left1Sub1);
        top1Left1Subs.add(top1Left1Sub2);
        top1Left1.setSubMenus(top1Left1Subs); // 设置top1Left1Subs
        // top1_left_top2
        SysMenuDto top1Left2 = generateMenu(top1.getParentId(), LEVEL2, 12L, "k导航菜单二", 1, null);
        List<SysMenuDto> top1Left2Subs = new ArrayList<>(); // left_top_subs
        SysMenuDto top1Left2Sub1 = generateMenu(top1Left2.getId(), LEVEL3, 53L, "k21", 0, "/top1left2sub1");
        SysMenuDto top1Left2Sub2 = generateMenu(top1Left2.getId(), LEVEL3, 54L, "k子22", 1, "/top1left2sub2");
        top1Left2Subs.add(top1Left2Sub1);
        top1Left2Subs.add(top1Left2Sub2);
        top1Left2.setSubMenus(top1Left2Subs); // 设置top1Left2Subs

        SysMenuDto top1Left3 = generateMenu(top1.getParentId(), LEVEL2, 13L, "k导航菜单三", 2, null);

        top1Subs.add(top1Left1);
        top1Subs.add(top1Left2);
        top1Subs.add(top1Left3);

        top1.setSubMenus(top1Subs);
        // end =====> top1

        // start =====> top2
        List<SysMenuDto> top2Subs = new ArrayList<>(); // top2子菜单
        // ++++top2_left_top++++
        // top2_left_top1
        SysMenuDto top2Left1 = generateMenu(top2.getParentId(), LEVEL2, 14L, "S导航菜单一", 0, null);
        List<SysMenuDto> top2Left1Subs = new ArrayList<>();// left_top_subs
        SysMenuDto top2Left1Sub1 = generateMenu(top2Left1.getId(), LEVEL3, 55L, "S子菜单11", 0, "/");
        SysMenuDto top2Left1Sub2 = generateMenu(top2Left1.getId(), LEVEL3, 56L, "S子菜单12", 1, "");
        top2Left1Subs.add(top2Left1Sub1);
        top2Left1Subs.add(top2Left1Sub2);
        top2Left1.setSubMenus(top2Left1Subs);// 设置top1Left1Subs
        // top1_left_top2
        SysMenuDto top2Left2 = generateMenu(top2.getParentId(), LEVEL2, 15L, "S导航菜单二", 0, null);
        List<SysMenuDto> top2Left2Subs = new ArrayList<>(); // left_top_subs
        SysMenuDto top2Left2Sub1 = generateMenu(top2Left2.getId(), LEVEL3, 57L, "S子21", 0, "/");
        SysMenuDto top2Left2Sub2 = generateMenu(top2Left2.getId(), LEVEL3, 58L, "S子22", 1, "/test/view1");
        top2Left2Subs.add(top2Left2Sub1);
        top2Left2Subs.add(top2Left2Sub2);
        top2Left2.setSubMenus(top2Left2Subs); // 设置top1Left2Subs

        top2Subs.add(top2Left1);
        top2Subs.add(top2Left2);

        top2.setSubMenus(top2Subs);
        // start =====> top2

        topMenus.add(top1);
        topMenus.add(top2);
        topMenus.add(top3);
        return topMenus;
    }

    private SysMenuDto generateMenu(Long parentId, int level, Long id, String menuName, int sort, String url) {
        SysMenuDto menuDto = new SysMenuDto();
        menuDto.setId(id);
        menuDto.setLevel(level);
        menuDto.setParentId(parentId);
        menuDto.setMenuName(menuName);
        menuDto.setSort(sort);
        menuDto.setUrl(url);
        return menuDto;
    }


}
