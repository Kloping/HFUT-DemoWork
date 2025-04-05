package top.kloping.service.impl;

import top.kloping.entity.BorrowRecord;
import top.kloping.mapper.BorrowRecordMapper;
import top.kloping.service.IBorrowRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements IBorrowRecordService {
    @Override
    public BorrowRecord getByUserIdAndBookId(Integer userId, Integer bookId) {
        return lambdaQuery()
                .eq(BorrowRecord::getUserId, userId)
                .eq(BorrowRecord::getBookId, bookId)
                .one();
    }
}