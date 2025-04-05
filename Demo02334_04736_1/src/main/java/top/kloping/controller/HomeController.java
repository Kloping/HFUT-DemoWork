package top.kloping.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/book")
    @PreAuthorize("isAuthenticated()")
    public String showBookPage() {
        return "book";
    }

    @GetMapping("/bookm")
    @PreAuthorize("isAuthenticated()")
    public String showBookmPage() {
        return "bookm";
    }

    @GetMapping("/borrow-records")
    @PreAuthorize("isAuthenticated()")
    public String showBorrowRecordsPage() {
        return "borrow-records";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
}