package com.backend;

import com.backend.mybatis.entity.MulitiDb;
import com.backend.service.IMulitiDbService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicDataSourceTest {

    @Autowired
    private IMulitiDbService mulitiDbService;

    @Test
    public void test(){
        Long id = 1L;

        MulitiDb r1 = mulitiDbService.testRead(id);

        MulitiDb r2 = mulitiDbService.testWrite(id);

        MulitiDb r3 = mulitiDbService.testOther(id);

        System.out.println();

    }

}
