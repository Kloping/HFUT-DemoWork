package top.kloping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.kloping.filter.CookieAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired TokenManager tokenManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // 禁用 CSRF 保护
                .authorizeRequests()
                .anyRequest().permitAll() // 其他请求需要认证
                .and()
                .addFilterBefore(new CookieAuthenticationFilter(tokenManager),
                        UsernamePasswordAuthenticationFilter.class); // 添加Token认证过滤器
    }
}