package phonestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import phonestore.entity.Comment;
import phonestore.service.ICommentService;
import phonestore.utils.JsonResult;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Autowired
    private ICommentService commentService;

    // 添加评价
    @PostMapping("/add")
    public JsonResult<Void> addComment(HttpSession session, @RequestBody Comment comment) {
        Integer uid = getUserIdFromSession(session);
        String username = getUsernameFromSession(session);

        // 检查是否已存在该订单商品的评价
        List<Comment> existingComments = commentService.getCommentsByPid(comment.getPid());
        for (Comment existingComment : existingComments) {
            if (existingComment.getOid().equals(comment.getOid())) {
                return new JsonResult<>(400);
            }
        }

        commentService.addComment(uid,username,comment);
        return new JsonResult<>(OK);
    }

    // 获取商品的评价
    @GetMapping("/{pid}")
    public JsonResult<List<Comment>> getCommentsByPid(@PathVariable("pid") Integer pid) {
        List<Comment> comments = commentService.getCommentsByPid(pid);
        return new JsonResult<>(OK, comments);
    }

    // 获取所有评论
    @GetMapping("/all")
    public JsonResult<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        return new JsonResult<>(OK, comments);
    }

    // 删除评论
    @DeleteMapping("/delete/{id}")
    public JsonResult<Void> deleteComment(@PathVariable("id") Integer id) {
        commentService.deleteComment(id);
        return new JsonResult<>(OK);
    }
}