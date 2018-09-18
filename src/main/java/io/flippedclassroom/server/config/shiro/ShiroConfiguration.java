package io.flippedclassroom.server.config.shiro;

import io.flippedclassroom.server.config.shiro.realm.PasswordShiroRealm;
import io.flippedclassroom.server.config.shiro.realm.TokenShiroRealm;
import io.flippedclassroom.server.config.shiro.matcher.TokenCredentialsMatcher;
import io.flippedclassroom.server.filter.TokenFilter;
import io.flippedclassroom.server.util.Const.Const;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 配置
 * 参考 https://xlui.me/t/spring-boot-shiro/
 */
@Configuration
public class ShiroConfiguration {
    /**
     * URL 认证授权规则,以auth开头的请求需要认证
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new TokenFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/auth/**", "jwt");
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 为基于用户名密码的验证注入加密方式
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName(Const.algorithm);
        credentialsMatcher.setHashIterations(Const.iteration);
        return credentialsMatcher;
    }

    /**
     * 为基于 Token 的验证注入验证方式
     */
    @Bean
    public TokenCredentialsMatcher tokenCredentialsMatcher() {
        return new TokenCredentialsMatcher();
    }

    /**
     * 基于 Token 的 Realm
     * 注入了自定义的验证类
     */
    @Bean
    public TokenShiroRealm tokenShiroRealm() {
        TokenShiroRealm tokenShiroRealm = new TokenShiroRealm();
        tokenShiroRealm.setCredentialsMatcher(tokenCredentialsMatcher());
        return tokenShiroRealm;
    }

    /**
     * 基于 用户名密码 的 Realm
     * 注入了加密方式
     */
    @Bean
    public PasswordShiroRealm passwordShiroRealm() {
        PasswordShiroRealm passwordShiroRealm = new PasswordShiroRealm();
        passwordShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return passwordShiroRealm;
    }

    /**
     * 多 Realm 情况下配置仅通过一个 Realm 的验证即可
     */
    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator() {
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    /**
     * 开启 Shiro Aop 注解支持 `@RequiresPermissions`
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Shiro 配置的核心
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // ModularRealmAuthenticator 的位置要放在 Realms 前，否则会报找不到 Realm 异常
        securityManager.setAuthenticator(modularRealmAuthenticator());
        securityManager.setRealms(Arrays.asList(tokenShiroRealm(), passwordShiroRealm()));

        // REST 模式下关闭 Shiro 自带的 Session
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        defaultSubjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(defaultSubjectDAO);

        return securityManager;
    }
}
