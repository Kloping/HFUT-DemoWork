package top.kloping.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kloping.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kloping
 * @since 2025-03-28
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {

}
