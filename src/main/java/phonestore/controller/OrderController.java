package phonestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import phonestore.entity.Order;
import phonestore.utils.JsonResult;
import phonestore.vo.OrderVo;
import phonestore.service.IOrderService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {
    @Autowired
    private IOrderService orderService;

    @PostMapping("/createOrder")
    public JsonResult<Order> createOrder(Integer aid, Long totalPrice, HttpSession session) {
        Integer uid = getUserIdFromSession(session);
        String username = getUsernameFromSession(session);
        Order order = orderService.insertOrder(aid, totalPrice, uid, username);
        return new JsonResult<>(OK, order);
    }

    @PostMapping("/createOrderItem")
    public JsonResult<Void> createOrderItem(Integer oid, Integer cid, Integer pid, Integer num, HttpSession session) {
        String username = getUsernameFromSession(session);
        if (pid == null) {
            orderService.insertOrderItem(oid, cid, num, username);
        } else {
            orderService.insertOrderItemFromProductHtml(oid, pid, num, username);
        }
        return new JsonResult<>(OK);
    }

    @GetMapping("/queryOrder")
    public JsonResult<Order> queryOrderByOid(Integer oid) {
        Order order = orderService.queryOrderByOid(oid);
        return new JsonResult<>(OK, order);
    }

    @PostMapping("/updateStatus")
    public JsonResult<Void> updateStatusByOid(Integer oid, HttpSession session, Integer status) {
        Integer uid = getUserIdFromSession(session);
        orderService.updateOrderStatusByOid(oid, uid, status);
        return new JsonResult<>(OK);
    }

    @PostMapping("/updatePrice")
    public JsonResult<Void> updatePriceByOid(Integer oid, Integer price) {
        orderService.updateOrderPriceByOid(oid, price);
        return new JsonResult<>(OK);
    }

    @GetMapping("/queryOrderVo")
    public JsonResult<List<OrderVo>> queryOrderVo(Integer oid) {
        List<OrderVo> orderVos = orderService.queryOrderVoByOid(oid);
        return new JsonResult<>(OK, orderVos);
    }

    @GetMapping("/uidOrderVo")
    public JsonResult<List<OrderVo>> queryOrderVoByUid(HttpSession session, Integer status) {
        Integer uid = getUserIdFromSession(session);
        List<OrderVo> orderVos = orderService.queryOrderVoByUid(uid, status);
        return new JsonResult<>(OK, orderVos);
    }

    @GetMapping("/lastSevenDaysTotal")
    public JsonResult<List<Map<String, Object>>> queryLastSevenDaysOrderTotal() {
        List<Map<String, Object>> data = orderService.queryLastSevenDaysOrderTotal();
        return new JsonResult<>(OK, data);
    }

    @GetMapping("/list")
    public JsonResult<List<Order>> queryOrderList(Integer status) {
        List<Order> orders = orderService.queryOrderList(status);
        return new JsonResult<>(OK, orders);
    }
}