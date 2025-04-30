package phonestore.mapper;

import phonestore.entity.Address;

import java.util.Date;
import java.util.List;


public interface AddressMapper {

    /**
     * 添加用户地址
     *  address 用户地址信息
     **/
    int addAddress(Address address);

    /**
     * 查询用户地址条目数
     *  uid 用户地址id
     **/
    int userAddressCount(Integer uid);

    /**
     * 查询用户id所有地址记录
     *  uid 用户id
     **/
    List<Address> queryUserAddress(Integer uid);

    /**
     * 根据地址aid查询某条数据
     *  aid 地址的aid
     **/
    Address queryUserAddressByAid(Integer aid);

    /**
     * 根据用户uid将其关联的地址设置为非默认值
     *  uid 用户uid
     **/
    int setAllAddressNotDefault(Integer uid);

    /**
     * 根据地址aid将某条地址设为默认值
     *  aid 地址aid
     *  modifiedUser 修改者
     *  modifiedTime 修改时间
     **/
    int setOneAddressDefault(Integer aid,String modifiedUser, Date modifiedTime);

    /**
     * 根据地址aid删除指定时间
     *  aid 地址aid
     *  modifiedUser 修改者
     *  modifiedTime 修改时间
     **/
    int deleteAddressByAid(Integer aid,String modifiedUser, Date modifiedTime);

    /**
     * 根据aid修改用户信息
     *  address 更新的用户地址信息
     **/
    int updateUserAddressByAid(Address address);

}
