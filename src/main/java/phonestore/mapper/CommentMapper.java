package phonestore.mapper;

import phonestore.entity.Comment;
import java.util.List;

public interface CommentMapper {
    // 插入一条评价
    int insertComment(Comment comment);

    // 根据订单ID查询评价列表
    List<Comment> queryCommentsByOid(Integer oid);

    // 根据商品ID查询评价列表
    List<Comment> queryCommentsByPid(Integer pid);

    // 查询所有评论
    List<Comment> queryAllComments();

    // 删除评论
    int deleteComment(Integer id);
}