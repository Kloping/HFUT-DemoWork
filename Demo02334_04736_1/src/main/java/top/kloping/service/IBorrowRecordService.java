package top.kloping.service;

import top.kloping.entity.BorrowRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IBorrowRecordService extends IService<BorrowRecord> {
    BorrowRecord getByUserIdAndBookId(Integer userId, Integer bookId);
}