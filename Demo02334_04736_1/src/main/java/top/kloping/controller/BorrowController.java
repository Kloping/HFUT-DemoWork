package top.kloping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import top.kloping.entity.Book;
import top.kloping.entity.BorrowRecord;
import top.kloping.entity.Card;
import top.kloping.entity.User;
import top.kloping.service.IBookService;
import top.kloping.service.IBorrowRecordService;
import top.kloping.service.ICardService;
import top.kloping.service.IUserService;

import java.time.Instant;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    @Autowired
    private IBorrowRecordService borrowRecordService;

    @Autowired
    private IUserService userService;

    @Autowired
    IBookService bookService;

    @Autowired
    ICardService cardService;

    @PostMapping("/{bookId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<Object> borrowBook(@PathVariable Integer bookId, @AuthenticationPrincipal UserDetails details) {
        User user = userService.findByUsername(details.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // 检查借阅证是否被锁定

        Card card = cardService.getById(user.getCardId());
        if (card.getBookId() > 0) {
            return ResponseEntity.badRequest().body("借阅证已使用,请归还图书后再次借阅");
        }

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setUserId(user.getId());
        borrowRecord.setBookId(bookId);
        borrowRecord.setBorrowDate(Instant.now().getEpochSecond());
        borrowRecord.setReturnDate(-1L);
        borrowRecord.setDueDate(Instant.now().plusSeconds(3 * 24 * 60 * 60).getEpochSecond());// 3天后的时间

        // 设置图书状态为已借阅
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        book.setStatus(1); // 1 表示已借阅
        bookService.updateBook(book);

        // 锁住借阅证
        card.setBookId(book.getId());
        cardService.updateById(card);

        boolean result = borrowRecordService.save(borrowRecord);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/return/{bookId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<Boolean> returnBook(@PathVariable Integer bookId, @AuthenticationPrincipal UserDetails details) {
        User user = userService.findByUsername(details.getUsername());
        BorrowRecord borrowRecord = borrowRecordService.lambdaQuery()
                .eq(BorrowRecord::getUserId, user.getId())
                .eq(BorrowRecord::getBookId, bookId)
                .eq(BorrowRecord::getReturnDate, -1L)
                .one();
        if (borrowRecord != null) {
            bookService.lambdaUpdate().eq(Book::getId, bookId).set(Book::getStatus, 0).update();
            cardService.lambdaUpdate().eq(Card::getUserId, user.getId()).set(Card::getBookId, 0).update();

            borrowRecord.setReturnDate(Instant.now().getEpochSecond());
            boolean result = borrowRecordService.updateById(borrowRecord);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}