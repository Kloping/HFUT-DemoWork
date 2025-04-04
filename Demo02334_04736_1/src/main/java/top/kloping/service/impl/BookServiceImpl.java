package top.kloping.service.impl;

import top.kloping.entity.Book;
import top.kloping.mapper.BookMapper;
import top.kloping.service.IBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kloping
 * @since 2025-03-28
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

    @Override
    public Book getBookById(Integer id) {
        return getById(id);
    }

    @Override
    public boolean saveBook(Book book) {
        return save(book);
    }

    @Override
    public boolean updateBook(Book book) {
        return updateById(book);
    }

    @Override
    public boolean deleteBook(Integer id) {
        return removeById(id);
    }
}