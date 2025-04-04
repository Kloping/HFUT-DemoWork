package top.kloping.service;

import top.kloping.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IBookService extends IService<Book> {

    Book getBookById(Integer id);

    boolean saveBook(Book book);

    boolean updateBook(Book book);

    boolean deleteBook(Integer id);
}