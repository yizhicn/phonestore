package phonestore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phonestore.entity.Discount;
import phonestore.mapper.DiscountMapper;
import phonestore.service.IDiscountService;
import phonestore.service.exception.InsertException;
import phonestore.service.exception.UpdateException;
import phonestore.service.exception.DiscountNotExistsException;
import phonestore.service.exception.DiscountInvalidException;

import java.util.Date;
import java.util.List;

@Service
public class IDiscountServiceImpl implements IDiscountService {
    @Autowired(required = false)
    private DiscountMapper discountMapper; // 自动注入DiscountMapper，用于数据库操作

    // 插入新的优惠码
    @Override
    public void insertDiscount(Discount discount, String username) {
        // 检查优惠码名称是否已存在
        if (discountMapper.queryDiscountByName(discount.getName()) != null) {
            throw new InsertException("优惠码名称已存在");
        }
        validateDiscount(discount); // 验证优惠码的有效性
        Date now = new Date();
        discount.setCreatedUser(username); // 设置创建者
        discount.setCreatedTime(now); // 设置创建时间
        discount.setModifiedUser(username); // 设置修改者
        discount.setModifiedTime(now); // 设置修改时间
        int result = discountMapper.insertDiscount(discount); // 执行插入操作
        if (result == 0) {
            throw new InsertException("创建优惠码失败"); // 插入失败抛出异常
        }
    }

    // 更新优惠码
    @Override
    public void updateDiscount(Discount discount, String username) {
        Discount existing = discountMapper.queryDiscountById(discount.getId()); // 查询已存在的优惠码
        if (existing == null) {
            throw new DiscountNotExistsException("优惠码不存在"); // 如果不存在，抛出异常
        }
        // 检查优惠码名称是否已存在
        Discount byName = discountMapper.queryDiscountByName(discount.getName());
        if (byName != null && !byName.getId().equals(discount.getId())) {
            throw new InsertException("优惠码名称已存在");
        }
        validateDiscount(discount); // 验证优惠码的有效性
        discount.setModifiedUser(username); // 设置修改者
        discount.setModifiedTime(new Date()); // 设置修改时间
        int result = discountMapper.updateDiscount(discount); // 执行更新操作
        if (result == 0) {
            throw new UpdateException("更新优惠码失败"); // 更新失败抛出异常
        }
    }

    // 删除指定ID的优惠码
    @Override
    public void deleteDiscountById(Integer id) {
        if (discountMapper.queryDiscountById(id) == null) {
            throw new DiscountNotExistsException("优惠码不存在"); // 如果优惠码不存在，抛出异常
        }
        int result = discountMapper.deleteDiscountById(id); // 执行删除操作
        if (result == 0) {
            throw new UpdateException("删除优惠码失败"); // 删除失败抛出异常
        }
    }

    // 查询指定ID的优惠码
    @Override
    public Discount queryDiscountById(Integer id) {
        Discount discount = discountMapper.queryDiscountById(id); // 查询优惠码
        if (discount == null) {
            throw new DiscountNotExistsException("优惠码不存在"); // 如果优惠码不存在，抛出异常
        }
        return discount;
    }

    // 根据优惠码名称查询优惠码
    @Override
    public Discount queryDiscountByName(String name) {
        Discount discount = discountMapper.queryDiscountByName(name); // 根据名称查询优惠码
        if (discount == null) {
            throw new DiscountNotExistsException("优惠码不存在"); // 如果优惠码不存在，抛出异常
        }
        return discount;
    }

    // 查询所有优惠码
    @Override
    public List<Discount> queryAllDiscounts() {
        List<Discount> discounts = discountMapper.queryAllDiscounts(); // 查询所有优惠码
        if (discounts.isEmpty()) {
            throw new DiscountNotExistsException("暂无优惠码"); // 如果没有优惠码，抛出异常
        }
        return discounts;
    }

    // 验证优惠码的有效性
    @Override
    public void verifyDiscount(String name, Long orderAmount) {
        Discount discount = discountMapper.queryDiscountByName(name); // 根据名称查询优惠码
        if (discount == null) {
            throw new DiscountNotExistsException("优惠码不存在"); // 如果优惠码不存在，抛出异常
        }
        if (discount.getQuantity() <= 0) {
            throw new DiscountInvalidException("优惠码已用完"); // 如果优惠码数量小于等于0，抛出异常
        }
        if (discount.getExpiryDate().before(new Date())) {
            throw new DiscountInvalidException("优惠码已过期"); // 如果优惠码已过期，抛出异常
        }
        if (discount.getMode() == 0 && orderAmount < discount.getMinAmount()) {
            throw new DiscountInvalidException("订单金额未达到最低要求"); // 如果订单金额小于最低要求，抛出异常
        }
        // TODO: Decrease quantity and apply discount logic (减少优惠码数量并应用折扣逻辑)
        int result = discountMapper.decreaseQuantity(discount.getId()); // 减少优惠码数量
        if (result == 0) {
            throw new UpdateException("优惠码使用失败"); // 使用优惠码失败抛出异常
        }
        // TODO: Apply discount to order (e.g., update order totalPrice) (在订单中应用折扣)
    }

    // 验证优惠码是否有效
    private void validateDiscount(Discount discount) {
        if (discount.getName() == null || discount.getName().isEmpty()) {
            throw new InsertException("优惠码名称不能为空"); // 优惠码名称不能为空
        }
        if (discount.getQuantity() == null || discount.getQuantity() < 0) {
            throw new InsertException("优惠码数量无效"); // 优惠码数量无效
        }
        if (discount.getExpiryDate() == null) {
            throw new InsertException("到期时间不能为空"); // 到期时间不能为空
        }
        if (discount.getMode() == null || (discount.getMode() != 0 && discount.getMode() != 1)) {
            throw new InsertException("优惠模式无效"); // 优惠模式无效
        }
        if (discount.getMode() == 0) {
            if (discount.getMinAmount() == null || discount.getMinAmount() < 0) {
                throw new InsertException("最低金额无效"); // 最低金额无效
            }
            if (discount.getDiscountAmount() == null || discount.getDiscountAmount() <= 0) {
                throw new InsertException("折扣金额无效"); // 折扣金额无效
            }
        } else {
            if (discount.getDiscountPercentage() == null || discount.getDiscountPercentage() <= 0 || discount.getDiscountPercentage() > 100) {
                throw new InsertException("折扣百分比无效"); // 折扣百分比无效
            }
        }
    }
}
