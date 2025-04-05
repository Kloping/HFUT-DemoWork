package top.kloping.service.impl;

import top.kloping.entity.Card;
import top.kloping.mapper.CardMapper;
import top.kloping.service.ICardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 借阅证 服务实现类
 * </p>
 *
 * @author kloping
 * @since 2025-04-04
 */
@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements ICardService {

}
