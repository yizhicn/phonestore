package phonestore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phonestore.entity.Comment;
import phonestore.entity.OrderItem;
import phonestore.mapper.CommentMapper;
import phonestore.service.ICommentService;
import phonestore.service.exception.InsertException;
import phonestore.service.IOrderService;
import java.util.Date;
import java.util.List;

@Service
public class ICommentServiceImpl implements ICommentService {

    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired
    private IOrderService orderService;

    @Override
    public void addComment(Integer uid, String username, Comment comment) {
        // 设置默认值
        comment.setCreatedTime(new Date());
        comment.setModifiedTime(new Date());
        comment.setUid(uid);
        comment.setCreatedUser(username);
        comment.setModifiedUser(username);

        // 插入评价
        int rows = commentMapper.insertComment(comment);
        if (rows != 1) {
            throw new InsertException("添加评价失败");
        }
        // 检查订单状态并更新
        checkAndUpdateOrderStatus(comment.getOid());
    }

    @Override
    public void checkAndUpdateOrderStatus(Integer oid) {
        // 获取订单中的所有商品
        List<OrderItem> orderItems = orderService.queryOrderItemByOid(oid);
        // 获取该订单的所有评价
        List<Comment> comments = commentMapper.queryCommentsByOid(oid);

        // 检查每个商品是否都有评价
        boolean allCommented = true;
        for (OrderItem item : orderItems) {
            boolean hasComment = false;
            for (Comment comment : comments) {
                if (comment.getPid().equals(item.getPid())) {
                    hasComment = true;
                    break;
                }
            }
            if (!hasComment) {
                allCommented = false;
                break;
            }
        }

        // 如果所有商品都已评价，更新订单状态为4
        if (allCommented) {
            orderService.updateOrderStatusByOid(oid, null, 4);
        }
    }

    @Override
    public List<Comment> getCommentsByPid(Integer pid) {
        return commentMapper.queryCommentsByPid(pid);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentMapper.queryAllComments();
    }

    @Override
    public void deleteComment(Integer id) {
        int rows = commentMapper.deleteComment(id);
        if (rows != 1) {
            throw new InsertException("删除评论失败");
        }
    }
}