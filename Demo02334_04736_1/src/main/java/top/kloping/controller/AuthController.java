package top.kloping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.kloping.entity.User;
import top.kloping.service.IUserService;

@Controller
public class AuthController {

    private final IUserService userService;

    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        User user = userService.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return "redirect:/book";
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
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole("USER");
            userService.saveUser(user);
            return "redirect:/login?success";
        } catch (RuntimeException e) {
            return "redirect:/register?error";
        }
    }
}