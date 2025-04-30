package phonestore.mapper;

import phonestore.entity.Discount;

import java.util.List;

public interface DiscountMapper {
    // 插入折扣信息
    int insertDiscount(Discount discount);

    // 更新折扣信息
    int updateDiscount(Discount discount);

    // 根据ID删除折扣信息
    int deleteDiscountById(Integer id);

    // 根据ID查询折扣信息
    Discount queryDiscountById(Integer id);

    // 根据名称查询折扣信息
    Discount queryDiscountByName(String name);

    // 查询所有折扣信息
    List<Discount> queryAllDiscounts();

    // 减少折扣数量
    int decreaseQuantity(Integer id);
}
