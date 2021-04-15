package org.sang.config;

import lombok.extern.slf4j.Slf4j;
import org.sang.config.filter.TokenAuthenticationFilter;
import org.sang.config.filter.TokenLoginFilter;
import org.sang.config.filter.UnauthorizedEntryPoint;
import org.sang.config.impl.TokenLogoutHandler;
import org.sang.config.impl.UserDetailsServiceImpl;
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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 跨域配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // 允许跨域访问的 URL
        List<String> allowedOriginsUrl = new ArrayList<>();
        allowedOriginsUrl.add("http://localhost:8080");
        allowedOriginsUrl.add("http://127.0.0.1:8080");
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置允许跨域访问的 URL
        config.setAllowedOrigins(allowedOriginsUrl);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        //"/api/user/register",
        httpSecurity.authorizeRequests()
                .antMatchers("/category/add", "/category/delete", "/category/delete/ids", "/category/update", "/article/upload", "/article/update").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
                .and().formLogin().permitAll()
                .and().cors().and().csrf().disable();
        httpSecurity.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint);
        httpSecurity.logout().clearAuthentication(true).addLogoutHandler(new TokenLogoutHandler());
        httpSecurity.addFilter(new TokenLoginFilter(authenticationManagerBean(), roleMapper, userMapper));
        httpSecurity.addFilter(new TokenAuthenticationFilter(authenticationManagerBean()));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/blogimg/**", "/index.html", "/static/**");
        web.ignoring().antMatchers("/index.html", "/static/**", "/templates/**");
    }

}