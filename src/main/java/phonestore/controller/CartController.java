package phonestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import phonestore.entity.Cart;
import phonestore.utils.JsonResult;
import phonestore.vo.CartVo;
import phonestore.service.ICartService;
import phonestore.service.exception.InsertException;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/cart")
public class CartController extends BaseController{
    @Autowired
    private ICartService cartService;

    /**
     * 处理添加购物车的请求
     *  cart cart实体类
     *  session 项目启动时自动生成的session对象
     **/
    @PostMapping("/addCart")
    public JsonResult<Void> addCart(Cart cart, HttpSession session){
        //从session中区域uid和用户名
        Integer uid = getUserIdFromSession(session);
        String username = getUsernameFromSession(session);
        cart.setUid(uid);
        Date date = new Date();
        int result = cartService.addCart(cart, username, date, username, date);

        if (result == 0){
            throw  new InsertException("服务器或数据库异常，加入购物车失败");
        }

        return new JsonResult<>(OK);
    }

    /**
     * 处理查询用户购物车信息的请求
     *  session 项目启动时自动生成的session对象
     **/
    @GetMapping("/showCarts")
    public JsonResult<List<CartVo>> showCarts(HttpSession session){
        Integer uid = getUserIdFromSession(session);
        List<CartVo> carts = cartService.queryCartByUid(uid);

        return new JsonResult<>(OK,carts);
    }

    /**
     * 处理更新购物车数量信息的请求
     *  num 更新的数量
     *  cid cart的cid信息
     *  session 项目启动时自动生成的session对象
     **/
    @PostMapping("/updateCart")
    public JsonResult<Void> updateCateByCid(Integer num,Integer cid,HttpSession session){
        String modifiedUser = getUsernameFromSession(session);
        Date modifiedTime = new Date();
        cartService.updateCartNumByCid(num,modifiedUser,modifiedTime,cid);
        return new JsonResult<>(OK);
    }

    /**
     * 处理cids数组的内容查询cart信息的请求
     *  cids 查询的cids数组
     **/
    @GetMapping("/queryCids")
    public JsonResult<List<CartVo>> queryCids(Integer[] cids){
        List<CartVo> list = cartService.queryCartByCids(cids);
        System.out.println(list.toString());
        if (list.size() == 0){
            return new JsonResult<>(9001);
        }
        return new JsonResult<>(OK,list);
    }

    /**
     * 处理根据cids内的指定cid删除cart的请求
     *  cids 存储要被删除的cart的cid
     **/
    @PostMapping("/deleteCart")
    public JsonResult<Void> deleteCartByCid(Integer[] cids){
        //遍历执行删除操作
        for (Integer cid: cids) {
            cartService.deleteCartByCid(cid);
        }

        return new JsonResult<>(OK);
    }

}
