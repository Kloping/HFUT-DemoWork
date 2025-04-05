package top.kloping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.kloping.TokenManager;
import top.kloping.entity.User;
import top.kloping.service.IUserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class AuthController {

    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @Autowired
    TokenManager tokenManager;

    @Autowired
    AuthenticationManager authenticationManager;

    @Value("${spring.security.token.exp}")
    Long exp;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request, HttpServletResponse response) {
        User user = userService.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            UsernamePasswordAuthenticationToken usernamePassword =
                    new UsernamePasswordAuthenticationToken(username, password);
            try {
                Authentication authenticate = authenticationManager.authenticate(usernamePassword);
                Object principal = authenticate.getPrincipal();
                UserDetails details = (UserDetails) principal;
                String token = UUID.randomUUID().toString();
                HttpSession httpSession = request.getSession(true);
                httpSession.setAttribute("token", token);
                httpSession.setMaxInactiveInterval(Math.toIntExact(exp));
                tokenManager.set(token, details);
                Cookie cookie = new Cookie("authorization", token);
                cookie.setMaxAge(Math.toIntExact(exp));
                cookie.setPath("/");
                response.addCookie(cookie);
                return "redirect:/book";
            } catch (AuthenticationException e) {
                response.setStatus(400);
                return "redirect:/login?error";
            }
        } else {
            return "redirect:/login?error";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        if (userService.saveUser(user)) {
            return "redirect:/login?success";
        } else {
            return "redirect:/register?error";
        }
    }
}