package com.backend;

import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import lombok.Data;

public class JavaTest {

    @Data
    public static class Ac {
        private Long id;
        private String name;
        private Date dure;
    }

    public static void main(String[] args) {
        List<Ac> lists = Lists.newArrayList();
        Ac ac1 = new Ac();
        ac1.setId(1L);
        ac1.setName("name1");
        ac1.setDure(new Date());
        lists.add(ac1);
        Ac ac2 = new Ac();
        ac2.setId(2L);
        ac2.setName("name1");
        ac2.setDure(new Date());
        lists.add(ac2);
        Ac ac3 = new Ac();
        ac3.setId(3L);
        ac3.setName("name1");
        lists.add(ac3);

    }
}
