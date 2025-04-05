package top.kloping.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kloping.entity.Card;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 借阅证 Mapper 接口
 * </p>
 *
 * @author kloping
 * @since 2025-04-04
 */
@Mapper
public interface CardMapper extends BaseMapper<Card> {

}
