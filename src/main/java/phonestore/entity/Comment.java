package phonestore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {
    private Integer id;
    private Integer oid;       // 订单ID
    private Integer pid;       // 商品ID
    private Integer uid;       // 用户ID
    private String content;    // 评价内容
    private Integer rating;    // 评分
    private  String createdUser; //用户名
}