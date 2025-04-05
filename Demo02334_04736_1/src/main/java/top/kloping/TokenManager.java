package top.kloping;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenManager {

    private final Map<String, UserDetails> tokenMap = new ConcurrentHashMap<>();

    /**
     * 将token与用户信息关联
     *
     * @param token  token
     * @param userDetails 用户信息
     */
    public void set(String token, UserDetails userDetails) {
        tokenMap.put(token, userDetails);
    }

    /**
     * 根据token获取用户信息
     *
     * @param token token
     * @return 用户信息
     */
    public UserDetails get(String token) {
        return tokenMap.get(token);
    }

    /**
     * 移除token
     *
     * @param token token
     */
    public void remove(String token) {
        tokenMap.remove(token);
    }

    /**
     * 检查token是否存在
     *
     * @param token token
     * @return 是否存在
     */
    public boolean contains(String token) {
        return tokenMap.containsKey(token);
    }
}