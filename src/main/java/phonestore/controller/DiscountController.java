package phonestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import phonestore.entity.Discount;
import phonestore.utils.JsonResult;
import phonestore.service.IDiscountService;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/discount") // 定义该控制器的请求路径前缀为 "/discount"
public class DiscountController extends BaseController {
    @Autowired
    private IDiscountService discountService; // 自动注入优惠码服务

    // 创建新的优惠码
    @PostMapping("/create")
    public JsonResult<Void> createDiscount(@RequestBody Discount discount, HttpSession session) {
        String username = getUsernameFromSession(session); // 从会话中获取当前用户名
        discountService.insertDiscount(discount, username); // 调用服务层方法创建优惠码
        return new JsonResult<>(OK); // 返回成功的响应
    }

    // 更新已有的优惠码
    @PostMapping("/update")
    public JsonResult<Void> updateDiscount(@RequestBody Discount discount, HttpSession session) {
        String username = getUsernameFromSession(session); // 从会话中获取当前用户名
        discountService.updateDiscount(discount, username); // 调用服务层方法更新优惠码
        return new JsonResult<>(OK); // 返回成功的响应
    }

    // 删除指定ID的优惠码
    @PostMapping("/delete")
    public JsonResult<Void> deleteDiscount(@RequestParam Integer id) {
        discountService.deleteDiscountById(id); // 调用服务层方法删除优惠码
        return new JsonResult<>(OK); // 返回成功的响应
    }

    // 根据ID查询优惠码
    @GetMapping("/query")
    public JsonResult<Discount> queryDiscountById(@RequestParam Integer id) {
        Discount discount = discountService.queryDiscountById(id); // 调用服务层方法根据ID查询优惠码
        return new JsonResult<>(OK, discount); // 返回成功的响应和查询到的优惠码信息
    }

    // 根据name查询优惠码
    @GetMapping("/queryByName")
    public JsonResult<Discount> queryDiscountByName(@RequestParam String name) {
        Discount discount = discountService.queryDiscountByName(name);
        return new JsonResult<>(OK, discount);
    }

    // 查询所有优惠码
    @GetMapping("/list")
    public JsonResult<List<Discount>> queryAllDiscounts() {
        List<Discount> discounts = discountService.queryAllDiscounts(); // 调用服务层方法查询所有优惠码
        return new JsonResult<>(OK, discounts); // 返回成功的响应和优惠码列表
    }

    // 验证优惠码的有效性
    @PostMapping("/verify")
    public JsonResult<Void> verifyDiscount(@RequestParam String name, @RequestParam Long orderAmount) {
        discountService.verifyDiscount(name, orderAmount); // 调用服务层方法验证优惠码
        return new JsonResult<>(OK); // 返回成功的响应
    }
}
