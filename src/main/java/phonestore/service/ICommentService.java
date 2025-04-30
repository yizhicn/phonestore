package phonestore.service;

import phonestore.entity.Comment;
import java.util.List;

public interface ICommentService {
    // 添加评价
    void addComment(Integer uid, String username, Comment comment);

    // 检查订单中所有商品是否都已评价，并更新订单状态
    void checkAndUpdateOrderStatus(Integer oid);

    // 根据商品ID获取评价列表
    List<Comment> getCommentsByPid(Integer pid);

    // 获取所有评论
    List<Comment> getAllComments();

    // 删除评论
    void deleteComment(Integer id);
}