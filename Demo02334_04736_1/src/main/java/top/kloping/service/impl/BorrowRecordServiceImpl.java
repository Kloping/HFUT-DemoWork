package top.kloping.service.impl;

import top.kloping.entity.BorrowRecord;
import top.kloping.mapper.BorrowRecordMapper;
import top.kloping.service.IBorrowRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kloping
 * @since 2025-04-04
 */
@Service
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements IBorrowRecordService {
}
