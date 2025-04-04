package top.kloping.service;

import top.kloping.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kloping
 * @since 2025-03-28
 */
public interface IUserService extends IService<User> {
    User findByUsername(String username);
    boolean saveUser(User user);
}