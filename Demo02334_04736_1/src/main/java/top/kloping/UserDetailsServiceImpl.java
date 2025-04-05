package top.kloping;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.kloping.entity.User;
import top.kloping.mapper.UserMapper;
import top.kloping.service.IUserService;

import java.util.*;

/**
 * @author github-kloping
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this);
        provider.setPasswordEncoder(passwordEncoder);
        ProviderManager providerManager = new ProviderManager(provider);
        return providerManager;
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    IUserService userService;

    @Value("${spring.security.token.exp}")
    Long exp;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User temp = userService.findByUsername(username);
        if (temp == null) return null;
        List<GrantedAuthority> authorities = new ArrayList<>();
        List.of(temp.getRole()).forEach(e -> authorities.add(() -> e));
        UserDetails user = new org.springframework.security.core.userdetails.User(
                temp.getUsername(), passwordEncoder.encode(temp.getPassword()), authorities);
        return user;
    }
}