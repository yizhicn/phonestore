package phonestore.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import phonestore.entity.Favorites;
import phonestore.utils.JsonResult;
import phonestore.service.IFavoritesService;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/favorites")
public class FavoritesController extends BaseController{
    @Autowired
    private IFavoritesService favoritesService;

    /**
     * 处理查询收藏商品的请求
     *  session 项目启动自动生成session对象
     *  status 查询收藏商品的状态
     **/
    @GetMapping("/queryFavorites")
    public JsonResult<PageInfo<Favorites>> queryFavorites(HttpSession session, Integer pageNum, Integer pageSize, Integer status){
        Integer uid = getUserIdFromSession(session);
        PageInfo<Favorites> favorites = favoritesService.queryFavorites(uid, pageNum,pageSize,status);

        return new JsonResult<>(OK,favorites);

    }

    /**
     * 处理添加收藏商品的请求
     *  session 项目启动自动生成session对象
     *  pid 当前商品的pid
     **/
    @PostMapping("/addFavorites")
    public JsonResult<Integer> addFavorites(HttpSession session,Integer pid){
        //从session中取出uid
        Integer uid = getUserIdFromSession(session);
        //执行插入操作并返回fid
        int fid = favoritesService.addFavorites(uid, pid);
        return new JsonResult<>(OK,fid);
    }

    /**
     * 处理取消收藏的请求
     *  session session 项目启动自动生成session对象
     *  status 更新的状态
     *  fid 收藏品的id
     **/
    @PostMapping("/updateStatus")
    public JsonResult<Void> cancelFavorites(HttpSession session,Integer status,Integer fid){
        Integer uid = getUserIdFromSession(session);
        favoritesService.updateFavoritesStatus(status,fid,uid);
        return new JsonResult<>(OK);
    }
}
