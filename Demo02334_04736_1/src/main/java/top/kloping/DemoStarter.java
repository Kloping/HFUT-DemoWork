package top.kloping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ServletComponentScan
@SpringBootApplication(scanBasePackages = "top.kloping")
@CrossOrigin
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DemoStarter {
    public static void main(String[] args) {
        SpringApplication.run(DemoStarter.class, args);
    }
}