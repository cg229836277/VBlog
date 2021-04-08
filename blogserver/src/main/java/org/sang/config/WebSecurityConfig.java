package org.sang.config;

import lombok.extern.slf4j.Slf4j;
import org.sang.config.filter.TokenAuthenticationFilter;
import org.sang.config.filter.TokenLoginFilter;
import org.sang.config.filter.UnauthorizedEntryPoint;
import org.sang.config.impl.TokenLogoutHandler;
import org.sang.config.impl.UserDetailsServiceImpl;
import org.sang.config.utils.TokenUtils;
import org.sang.mapper.RoleMapper;
import org.sang.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * Created by sang on 2017/12/17.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private TokenUtils tokenUtils;

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private UnauthorizedEntryPoint unauthorizedEntryPoint;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        // 使用 BCryptPasswordEncoder 验证密码
        auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        // 配置 CSRF 关闭,允许跨域访问
        httpSecurity.csrf().disable();
        // 指定错误未授权访问的处理类
        httpSecurity.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint);
        // 关闭 Session
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 允许 登录 注册的 api 的无授权访问，其他需要授权访问
        httpSecurity.authorizeRequests()
                .antMatchers("/api/user/login", "/api/user/register", "/login")
                .permitAll().anyRequest().authenticated().and()
                .formLogin().loginProcessingUrl("/login").permitAll().and()
                .logout().logoutUrl("/logout") // 配置注销登录请求URL为"/logout"（默认也就是 /logout）
                .clearAuthentication(true).addLogoutHandler(new TokenLogoutHandler()); // 清除身份认证信息;
        httpSecurity.addFilter(new TokenLoginFilter(authenticationManagerBean(), tokenUtils, roleMapper, userMapper));
        httpSecurity.addFilter(new TokenAuthenticationFilter(authenticationManagerBean(), tokenUtils)).httpBasic();
        // 禁用缓存
        httpSecurity.headers().cacheControl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/index.html", "/static/**", "/templates/**");
    }

}