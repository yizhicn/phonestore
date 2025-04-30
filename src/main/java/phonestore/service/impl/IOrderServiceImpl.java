package phonestore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phonestore.entity.*;
import phonestore.mapper.OrderMapper;
import phonestore.service.exception.InsertException;
import phonestore.service.exception.UpdateException;
import phonestore.vo.OrderVo;
import phonestore.service.IAddressService;
import phonestore.service.ICartService;
import phonestore.service.IOrderService;
import phonestore.service.IProductService;
import phonestore.service.exception.OrderNotExistsException;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class IOrderServiceImpl implements IOrderService {
    @Autowired(required = false)
    private OrderMapper orderMapper;
    @Autowired(required = false)
    private IAddressService addressService;
    @Autowired(required = false)
    private ICartService cartService;
    @Autowired(required = false)
    private IProductService productService;

    @Override
    public Order insertOrder(Integer aid, Long totalPrice, Integer uid, String username) {
        Address address = addressService.queryAddressByAid(aid);
        Order order = new Order();
        order.setUid(uid);
        order.setAid(aid);
        order.setRecvName(address.getName());
        order.setRecvPhone(address.getPhone());
        order.setRecvProvince(address.getProvinceName());
        order.setRecvCity(address.getCityName());
        order.setRecvArea(address.getAreaName());
        order.setRecvAddress(address.getAddress());
        order.setTotalPrice(totalPrice);
        order.setStatus(0);
        Date createdTime = new Date();
        order.setOrderTime(createdTime);
        order.setPayTime(null);
        order.setCreatedUser(username);
        order.setModifiedUser(username);
        order.setCreatedTime(createdTime);
        order.setModifiedTime(createdTime);

        int result = orderMapper.insertOneOrder(order);
        if (result == 0) {
            throw new InsertException("服务器出现错误，创建订单失败");
        }
        return orderMapper.queryOrderByOid(order.getOid());
    }

    @Override
    public int insertOrderItem(Integer oid, Integer cid, Integer num, String username) {
        Cart cart = cartService.queryCartByCid(cid);
        Integer pid = cart.getPid();
        Product product = productService.queryProductById(pid);
        OrderItem orderItem = new OrderItem();
        orderItem.setOid(oid);
        orderItem.setPid(pid);
        orderItem.setTitle(product.getTitle());
        orderItem.setImage(product.getImage());
        orderItem.setPrice(product.getPrice());
        orderItem.setNum(num);
        Date createdTime = new Date();
        orderItem.setCreatedUser(username);
        orderItem.setCreatedTime(createdTime);
        orderItem.setModifiedUser(username);
        orderItem.setModifiedTime(createdTime);

        int result = orderMapper.insertOneOrderItem(orderItem);
        if (result == 0) {
            throw new InsertException("服务器出现错误，创建订单失败");
        }
        return result;
    }

    @Override
    public int insertOrderItemFromProductHtml(Integer oid, Integer pid, Integer num, String username) {
        Product product = productService.queryProductById(pid);
        OrderItem orderItem = new OrderItem();
        orderItem.setOid(oid);
        orderItem.setPid(pid);
        orderItem.setTitle(product.getTitle());
        orderItem.setImage(product.getImage());
        orderItem.setPrice(product.getPrice());
        orderItem.setNum(num);
        Date createdTime = new Date();
        orderItem.setCreatedUser(username);
        orderItem.setCreatedTime(createdTime);
        orderItem.setModifiedUser(username);
        orderItem.setModifiedTime(createdTime);

        int result = orderMapper.insertOneOrderItem(orderItem);
        if (result == 0) {
            throw new InsertException("服务器出现错误，创建订单失败");
        }
        return result;
    }

    @Override
    public Order queryOrderByOid(Integer oid) {
        Order order = orderMapper.queryOrderByOid(oid);
        if (order == null) {
            throw new OrderNotExistsException("订单不存在！！！");
        }
        return order;
    }

    @Override
    public int updateOrderStatusByOid(Integer oid, Integer uid, Integer status) {
        Order order = orderMapper.queryOrderByOid(oid);
        if (order == null) {
            throw new OrderNotExistsException("无订单信息！！！");
        }

        int result = 0;
        if (order.getStatus() == 0) {
            Date payTime = new Date();
            result = orderMapper.updateStatusByOidInt(oid, status, payTime);
            List<OrderItem> orderItems = orderMapper.queryOrderItemByOid(oid);
            for (OrderItem o : orderItems) {
                Integer pid = o.getPid();
                cartService.deleteCartByUidAndPid(uid, pid);
            }
        } else {
            result = orderMapper.updateStatusByOidInt(oid, status, order.getPayTime());
        }

        if (result == 0) {
            throw new UpdateException("服务器异常，修改订单状态失败");
        }

        return result;
    }

    @Override
    public int updateOrderPriceByOid(Integer oid, Integer price) {
        Order order = orderMapper.queryOrderByOid(oid);
        if (order == null) {
            throw new OrderNotExistsException("订单不存在！！！");
        }
        int result = orderMapper.updatePriceByOidInt(oid, price);
        if (result == 0) {
            throw new UpdateException("服务器异常，修改订单价格失败");
        }
        return result;
    }


    @Override
    public List<OrderItem> queryOrderItemByOid(Integer oid) {
        List<OrderItem> orderItems = orderMapper.queryOrderItemByOid(oid);
        if (orderItems.size() == 0) {
            throw new OrderNotExistsException("订单不存在！！！");
        }
        return orderItems;
    }

    @Override
    public List<OrderVo> queryOrderVoByOid(Integer oid) {
        List<OrderVo> orderVos = orderMapper.queryOrderVoByOid(oid);
        for (OrderVo vo : orderVos) {
            Address address = addressService.queryAddressByAid(vo.getAid());
            vo.setZip(address.getZip());
            vo.setPhone(address.getPhone());
            vo.setProvinceName(address.getProvinceName());
            vo.setCityName(address.getCityName());
            vo.setAreaName(address.getAreaName());
            vo.setAddress(address.getAddress());
        }
        return orderVos;
    }

    @Override
    public List<OrderVo> queryOrderVoByUid(Integer uid, Integer status) {
        List<OrderVo> orderVos = orderMapper.queryOrderVoByUid(uid, status);
        if (orderVos.size() == 0) {
            throw new OrderNotExistsException("查询订单为空");
        }
        return orderVos;
    }

    @Override
    public List<Map<String, Object>> queryLastSevenDaysOrderTotal() {
        List<Map<String, Object>> result = orderMapper.queryLastSevenDaysOrderTotal();
        return result;
    }

    @Override
    public List<Order> queryOrderList(Integer status) {
        List<Order> orders = orderMapper.queryOrderList(status);
        if (orders == null || orders.isEmpty()) {
            throw new OrderNotExistsException("订单列表为空");
        }
        return orders;
    }
}