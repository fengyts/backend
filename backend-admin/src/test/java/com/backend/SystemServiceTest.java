package com.backend;

import com.backend.system.dto.SysMenuDto;
import com.backend.system.service.ISysMenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SystemServiceTest {

    @Autowired
    private ISysMenuService sysMenuService;

    @Test
    public void test() {
        List<SysMenuDto> allMenus = sysMenuService.getAllMenus(1L);
        System.out.println(allMenus.size());
    }

}
