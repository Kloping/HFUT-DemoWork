package top.kloping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.kloping.entity.Book;
import top.kloping.entity.User;
import top.kloping.service.IBookService;

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

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.list();
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Book>> getMyBooks(@AuthenticationPrincipal User user) {
        List<Book> books = bookService.lambdaQuery().eq(Book::getPublisherId, user.getId()).list();
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<Boolean> saveBook(@RequestBody Book book) {
        boolean result = bookService.saveBook(book);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Boolean> updateBook(@RequestBody Book book, @AuthenticationPrincipal User user) {
        Book existingBook = bookService.getBookById(book.getId());
        if (existingBook != null && (user.getRole().equals("ADMIN") || existingBook.getPublisherId().equals(user.getId()))) {
            boolean result = bookService.updateBook(book);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Boolean> deleteBook(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        Book existingBook = bookService.getBookById(id);
        if (existingBook != null && (user.getRole().equals("ADMIN") || existingBook.getPublisherId().equals(user.getId()))) {
            boolean result = bookService.deleteBook(id);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}