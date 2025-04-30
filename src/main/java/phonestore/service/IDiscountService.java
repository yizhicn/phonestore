package phonestore.service;

import phonestore.entity.Discount;
import java.util.List;

public interface IDiscountService {
    // 插入折扣信息，记录操作的用户名
    void insertDiscount(Discount discount, String username);

    // 更新折扣信息，记录操作的用户名
    void updateDiscount(Discount discount, String username);

    // 根据ID删除折扣信息
    void deleteDiscountById(Integer id);

    // 根据ID查询折扣信息
    Discount queryDiscountById(Integer id);

    // 根据名称查询折扣信息
    Discount queryDiscountByName(String name);

    // 查询所有折扣信息
    List<Discount> queryAllDiscounts();

    // 验证折扣是否有效，根据订单金额进行验证（占位方法）
    void verifyDiscount(String name, Long orderAmount);
}
