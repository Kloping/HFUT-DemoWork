package top.kloping.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/book")
    public String showBookPage() {
        logger.info("Returning view name: book");
        return "book";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
}