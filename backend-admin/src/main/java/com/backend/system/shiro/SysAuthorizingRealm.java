package com.backend.system.shiro;

import com.backend.system.entity.SysMenu;
import com.backend.system.entity.SysRole;
import com.backend.system.entity.SysUser;
import com.backend.system.service.ISysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class SysAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private ISysUserService sysUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("principals should not be null");
        }
        SysUser userInfo = (SysUser) SecurityUtils.getSubject().getPrincipals();
        if (Objects.isNull(userInfo)) {
            return null;
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 获取用户角色和权限
        List<SysRole> roleList = sysUserService.findRolesByUserName(userInfo.getUserName());
        roleList.forEach(role -> {
            Long roleId = role.getId();
            simpleAuthorizationInfo.addRole(role.getRoleName());
            List<SysMenu> menuList = sysUserService.selectMenusByRoleId(roleId);
            menuList.forEach(menu -> {
                simpleAuthorizationInfo.addStringPermission(menu.getName());
            });
        });

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = token.getPrincipal().toString();
        String credentials = new String((char[]) token.getCredentials());

        SysUser userInfo = sysUserService.selectByUserName(username);
        if (userInfo == null) {
            throw new UnknownAccountException("用户名或密码错误！");
        }
        String dbPasswd = userInfo.getPasswd();
//        if (!credentials.equals(dbPasswd)) {
//            throw new IncorrectCredentialsException("用户名或密码错误！");
//        }
        String salt = dbPasswd.substring(0, 16);
//        byte[] saltBytes = EncodeUtil.decodeHex(salt);
        String pwd = dbPasswd.substring(16);
        AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userInfo,
                pwd, ByteSource.Util.bytes(salt), getName());
        return authenticationInfo;
    }

}
