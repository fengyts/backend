package com.backend.mock;

import com.backend.model.entity.SysUserEntity;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 模拟系统用户
 */
@Data
public class SysUserMock implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<SysUserEntity> sysUsers;

    private static final String PASSWORD = "123456";

    {
        initData();
    }

    private void initData() {
        sysUsers = Lists.newArrayList();

        SysUserEntity boss = new SysUserEntity();
        boss.setId("8");
        boss.setLoginName("boss");
        boss.setRealName("霸道总裁");
        boss.setSex(1);
        boss.setAge(33);
        boss.setPosition("老板");
        boss.setPasswd(PASSWORD);
        boss.setDeptId("0");
        boss.setGroupId("manager");
        boss.setEmail("boss@163.com");
        sysUsers.add(boss);

        SysUserEntity admin = new SysUserEntity();
        admin.setId("7");
        admin.setLoginName("admin");
        admin.setRealName("管理员");
        admin.setSex(1);
        admin.setAge(33);
        admin.setPosition("管理员");
        admin.setPasswd(PASSWORD);
        admin.setDeptId("IT研发部");
        admin.setGroupId("manager");
        admin.setEmail("admin@163.com");
        sysUsers.add(admin);

        SysUserEntity suAdmin = new SysUserEntity();
        suAdmin.setId("6");
        suAdmin.setLoginName("superAdmin");
        suAdmin.setRealName("超级管理员");
        suAdmin.setAge(35);
        suAdmin.setSex(0);
        suAdmin.setPosition("超级管理员");
        suAdmin.setPasswd(PASSWORD);
        suAdmin.setDeptId("IT研发部");
        suAdmin.setGroupId("manager");
        suAdmin.setEmail("superadmin@163.com");
        sysUsers.add(suAdmin);

        SysUserEntity diaochan = new SysUserEntity();
        diaochan.setId("5");
        diaochan.setLoginName("diaochan");
        diaochan.setRealName("貂蝉");
        diaochan.setAge(20);
        diaochan.setSex(0);
        diaochan.setPosition("普通员工");
        diaochan.setPasswd(PASSWORD);
        diaochan.setDeptId("IT研发部");
        diaochan.setGroupId("订单组");
        diaochan.setEmail("diaochan@163.com");
        sysUsers.add(diaochan);

        SysUserEntity zhangsan = new SysUserEntity();
        zhangsan.setId("1");
        zhangsan.setLoginName("zhangsan");
        zhangsan.setRealName("张三");
        zhangsan.setAge(22);
        zhangsan.setSex(1);
        zhangsan.setPosition("普通员工");
        zhangsan.setPasswd(PASSWORD);
        zhangsan.setDeptId("IT研发部");
        zhangsan.setGroupId("订单组");
        zhangsan.setEmail("zhangsan@163.com");
        sysUsers.add(zhangsan);

        SysUserEntity lisi = new SysUserEntity();
        lisi.setId("2");
        lisi.setLoginName("lisi");
        lisi.setRealName("李四");
        lisi.setAge(23);
        lisi.setSex(1);
        lisi.setPosition("普通员工");
        lisi.setPasswd(PASSWORD);
        lisi.setDeptId("IT研发部");
        lisi.setGroupId("商品组");
        lisi.setEmail("lisi@163.com");
        sysUsers.add(lisi);

        SysUserEntity wangwu = new SysUserEntity();
        wangwu.setId("3");
        wangwu.setLoginName("wangwu");
        wangwu.setRealName("王五");
        wangwu.setAge(27);
        wangwu.setSex(1);
        wangwu.setPosition("技术组长");
        wangwu.setPasswd(PASSWORD);
        wangwu.setDeptId("IT研发部");
        wangwu.setGroupId("商品组");
        wangwu.setEmail("wangwu@163.com");
        sysUsers.add(wangwu);

        SysUserEntity zhaoliu = new SysUserEntity();
        zhaoliu.setId("4");
        zhaoliu.setLoginName("zhaoliu");
        zhaoliu.setRealName("赵六");
        zhaoliu.setAge(30);
        zhaoliu.setSex(1);
        zhaoliu.setPosition("技术总监");
        zhaoliu.setPasswd(PASSWORD);
        zhaoliu.setDeptId("IT研发部");
        zhaoliu.setGroupId("manager");
        zhaoliu.setEmail("zhaoliu@163.com");
        sysUsers.add(zhaoliu);
    }

}
