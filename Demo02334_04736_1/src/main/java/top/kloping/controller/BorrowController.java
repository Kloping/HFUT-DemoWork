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
import java.util.List;

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
            return ResponseEntity.badRequest().body("请先归还图书再进行借阅");
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

    @PostMapping("/return/{bookId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<Object> returnBook(@PathVariable Integer bookId, @AuthenticationPrincipal UserDetails details) {
        User user = userService.findByUsername(details.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        BorrowRecord borrowRecord = borrowRecordService.getByUserIdAndBookId(user.getId(), bookId);
        if (borrowRecord == null) {
            return ResponseEntity.notFound().build();
        }

        // 计算超期罚款
        long currentTime = Instant.now().getEpochSecond();
        if (currentTime > borrowRecord.getDueDate()) {
            long overdueDays = (currentTime - borrowRecord.getDueDate()) / (24 * 60 * 60);
            double fineAmount = overdueDays * 1.0; // 假设每天罚款1元
            borrowRecord.setFineAmount(fineAmount);
        } else {
            borrowRecord.setFineAmount(0.0);
        }

        borrowRecord.setReturnDate(currentTime);
        borrowRecordService.updateById(borrowRecord);

        // 更新图书状态为可借阅
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        book.setStatus(0); // 0 表示可借阅
        bookService.updateBook(book);

        // 解锁借阅证
        Card card = cardService.getById(user.getCardId());
        card.setBookId(0);
        cardService.updateById(card);

        return ResponseEntity.ok(borrowRecord);
    }

    @GetMapping("/records")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<List<BorrowRecord>> getBorrowRecords(@AuthenticationPrincipal UserDetails details) {
        User user = userService.findByUsername(details.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<BorrowRecord> borrowRecords = borrowRecordService.lambdaQuery()
                .eq(BorrowRecord::getUserId, user.getId())
                .list();

        return ResponseEntity.ok(borrowRecords);
    }

    @PutMapping("/renew/{bookId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<String> renewBook(@PathVariable Integer bookId, @AuthenticationPrincipal UserDetails details) {
        User user = userService.findByUsername(details.getUsername());
        BorrowRecord borrowRecord = borrowRecordService.lambdaQuery()
                .eq(BorrowRecord::getUserId, user.getId())
                .eq(BorrowRecord::getBookId, bookId)
                .eq(BorrowRecord::getReturnDate, -1L)
                .one();

        if (borrowRecord != null) {
            // 更新借阅记录的到期时间，续借3天
            borrowRecord.setDueDate(Instant.now().plusSeconds(3 * 24 * 60 * 60).getEpochSecond());
            boolean result = borrowRecordService.updateById(borrowRecord);
            return ResponseEntity.ok("借阅成功");
        } else {
            return ResponseEntity.badRequest().body("不是当前用户借阅无法归还");
        }
    }
}