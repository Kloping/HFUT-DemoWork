package top.kloping.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kloping.entity.User;
import top.kloping.entity.Card;
import top.kloping.service.IUserService;
import top.kloping.service.ICardService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICardService cardService;

    @GetMapping("/card")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<Card> getMyCardId(@AuthenticationPrincipal UserDetails details) {
        User currentUser = userService.findByUsername(details.getUsername());
        Card card = cardService.getById(currentUser.getCardId());
        return ResponseEntity.ok(card);
    }
}