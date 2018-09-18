package io.flippedclassroom.server.config.shiro.realm;

import io.flippedclassroom.server.entity.Permission;
import io.flippedclassroom.server.entity.Role;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.config.shiro.token.PasswordToken;
import io.flippedclassroom.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Shiro Realm 2: 用户名密码 登录验证
 */
@Slf4j
public class PasswordShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    /**
     * 通过重写 supports 方法来避免重复校验
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof PasswordToken;
    }

    /**
     * 重写 isPermitted 方法来检查进行授权的是否是 PasswordToken 类型，不是则退出。避免重复授权
     */
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        return principals.getPrimaryPrincipal() instanceof PasswordToken && super.isPermitted(principals, permission);
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("开始给用户赋予权限");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        PasswordToken passwordToken = (PasswordToken) principalCollection.getPrimaryPrincipal();
        User user = userService.findUserByUsername((String) passwordToken.getPrincipal());

        // 可以在这里进行缓存

        Role role = user.getRole();
        authorizationInfo.addRole(role.getRole());
//        for (Permission permission : role.getPermissionList()) {
//            authorizationInfo.addStringPermission(permission.getPermission());
//        }

        return authorizationInfo;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String username = (String) authenticationToken.getPrincipal();
        User user = userService.findUserByUsername(username);
        log.info("username{}",username);
        if (user == null) {
            log.error("用户{}不存在", username);
            throw new AuthenticationException();
        }

        return new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),
                getName()
        );

    }
}