package top.kloping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author kloping
 * @since 2025-04-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("borrow_record")
public class BorrowRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;
    @TableField(exist = false)
    private String userName; // 新增字段

    private Integer bookId;

    /**
     * 借出时间
     */
    private Long borrowDate;

    /**
     * 应还时间
     */
    private Long dueDate;

    /**
     * 还的时间
     */
    private Long returnDate;

    /**
     * 罚款金额
     */
    private Double fineAmount;
}
