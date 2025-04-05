package top.kloping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.kloping.entity.Book;
import top.kloping.entity.User;
import top.kloping.service.IBookService;
import top.kloping.service.IUserService;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @author github kloping
 * @date 2025/4/4-15:40
 */
@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    private IBookService bookService;
    @Autowired
    private IUserService userService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.list();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Book>> getAllBooksForAdmin() {
        List<Book> books = bookService.list();
        for (Book book : books) {
            User publisher = userService.getById(book.getPublisherId());
            book.setPublisherName(publisher.getUsername());
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<Book>> getMyBooks(@AuthenticationPrincipal UserDetails details) {
        User user = userService.findByUsername(details.getUsername());
        List<Book> books = bookService.lambdaQuery().eq(Book::getPublisherId, user.getId()).list();
        return ResponseEntity.ok(books);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Boolean> saveBook(@AuthenticationPrincipal UserDetails details, @RequestBody Book book) {
        User user = userService.findByUsername(details.getUsername());
        book.setPublisherId(user.getId());
        boolean result = bookService.saveBook(book);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Boolean> updateBook(@PathVariable Integer id, @RequestBody Book book, @AuthenticationPrincipal UserDetails details) {
        User user = userService.findByUsername(details.getUsername());
        Book existingBook = bookService.getBookById(id);
        book.setId(id);
        if (existingBook != null && (user.getRole().equals("ADMIN") || existingBook.getPublisherId().equals(user.getId()))) {
            boolean result = bookService.updateBook(book);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Boolean> deleteBook(@PathVariable Integer id, @AuthenticationPrincipal UserDetails details) {
        User user = userService.findByUsername(details.getUsername());
        Book existingBook = bookService.getBookById(id);
        if (existingBook != null && (user.getRole().equals("ADMIN") || existingBook.getPublisherId().equals(user.getId()))) {
            boolean result = bookService.deleteBook(id);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}