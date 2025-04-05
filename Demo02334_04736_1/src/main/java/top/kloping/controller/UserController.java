package top.kloping.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kloping.entity.User;
import top.kloping.entity.Card;
import top.kloping.service.IUserService;
import top.kloping.service.ICardService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private ICardService cardService;

    @GetMapping("/permissions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<String>> getMyPermissions(@AuthenticationPrincipal UserDetails details) {
        User user = userService.findByUsername(details.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<String> permissions = new ArrayList<>();
        permissions.add(user.getRole());
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/isAdmin")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> isAdmin(@AuthenticationPrincipal UserDetails details) {
        User user = userService.findByUsername(details.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("ADMIN".equals(user.getRole()));
    }

    @GetMapping("/card")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<Card> getMyCardId(@AuthenticationPrincipal UserDetails details) {
        User currentUser = userService.findByUsername(details.getUsername());
        Card card = cardService.getById(currentUser.getCardId());
        return ResponseEntity.ok(card);
    }

    @GetMapping("/card/{bookId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<Card> getCardByBookId(@PathVariable Integer bookId) {
        Card card = cardService.lambdaQuery().eq(Card::getBookId, bookId).one();
        return ResponseEntity.ok(card);
    }

    @GetMapping("/nickname/{userId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<String> getNicknameByUserId(@PathVariable Integer userId) {
        User user = userService.getById(userId);
        if (user != null) {
            return ResponseEntity.ok(user.getUsername());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}