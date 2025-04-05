package top.kloping.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.kloping.entity.Card;
import top.kloping.entity.User;
import top.kloping.mapper.UserMapper;
import top.kloping.service.ICardService;
import top.kloping.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User findByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).one();
    }

    @Autowired
    ICardService cardService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean saveUser(User user) {
        // 检查用户名是否已存在
        if (findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        user.setRole("USER");
        if (save(user)) {
            Card card = new Card();
            card.setUserId(user.getId());
            card.setBookId(0);
            if (cardService.save(card)) {
                user.setCardId(card.getId());
                if (updateById(user)) {
                    return true;
                } else throw new RuntimeException("借阅证 更新失败");
            } else throw new RuntimeException("借阅证 创建失败");
        } else throw new RuntimeException("用户信息保存异常");
    }
}