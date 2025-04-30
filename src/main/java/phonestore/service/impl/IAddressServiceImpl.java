package phonestore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phonestore.entity.Address;
import phonestore.entity.User;
import phonestore.service.exception.*;
import phonestore.mapper.AddressMapper;
import phonestore.service.IAddressService;
import phonestore.service.IDistrictService;
import phonestore.service.IUserService;
import phonestore.service.exception.*;

import java.util.Date;
import java.util.List;


@Service
public class IAddressServiceImpl implements IAddressService {

    @Autowired(required = false)
    private AddressMapper addressMapper;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDistrictService districtService;


    /**
     * 添加地址业务具体逻辑
     *  address 地址类对象
     **/
    @Override
    public void addAddress(Address address, String username, Integer uid) {

        //先判断用户在数据库中的地址记录数
        int count = addressMapper.userAddressCount(uid);

        //查询是否超过记录限制
        if (count >= 20){
            throw new AddressCountLimitException("地址数量已达上限，请先删除部分地址！");
        }

        //当查询记录为0，则将此地址设置为用户默认的地址
        if (count == 0){
            address.setIsDefault(1);
        }else{ //否则设置为0，表示不是默认地址
            address.setIsDefault(0);
        }

        //获取并设置其他三个地址为null的字段
        String provinceName = districtService.getNameByCode(address.getProvinceCode());
        String cityName = districtService.getNameByCode(address.getCityCode());
        String areaName = districtService.getNameByCode(address.getAreaCode());

        address.setProvinceName(provinceName);
        address.setCityName(cityName);
        address.setAreaName(areaName);

        //补全表单中没有的其他字段
        //将取出的uid设为当前地址对象的的uid
        address.setUid(uid);
        address.setCreatedUser(username);
        address.setCreatedTime(new Date());
        address.setModifiedUser(username);
        address.setModifiedTime(new Date());

        //插入数据
        int result = addressMapper.addAddress(address);

        if (result == 0){
            throw new InsertException("新增地址失败，服务器或数据库异常！");
        }
    }

    /**
     * 地址查询的具体逻辑
     *  uid 用户id
     **/
    @Override
    public List<Address> queryUserAddress(Integer uid) {
        //先判断用户信息是否存在
        User user = userService.queryUserByUid(uid);

        if (user == null | user.getIsDelete() == 1){
            throw new UserNotExistException("用户信息不存在");
        }
        //返回查询的用户地址信息
        return addressMapper.queryUserAddress(uid);
    }

    /**
     * Description :根据aid查询地址的具体逻辑
     *  aid 地址aid
     **/
    @Override
    public Address queryAddressByAid(Integer aid) {
        return addressMapper.queryUserAddressByAid(aid);
    }

    /**
     * 设置用户所有地址为非默认的具体逻辑
     *  uid 用户uid
     **/
    @Override
    public int setNotDefaultAddress(Integer uid) {
      return addressMapper.setAllAddressNotDefault(uid);
    }

    /**
     * 设置某个地址为默认地址的具体逻辑
     *  aid 地址aid
     *  modifiedTime 修改时间
     *  modifiedUser 修改者
     **/
    @Override
    public int setOneAddressDefault(Integer aid,String modifiedUser, Date modifiedTime) {
        return addressMapper.setOneAddressDefault(aid,modifiedUser,modifiedTime);
    }

    /**
     * 删除某个地址的具体逻辑
     *  aid 地址aid
     *  modifiedUser 修改者
     *  modifiedTime 修改时间
     **/
    @Override
    public int deleteOneAddress(Integer aid, String modifiedUser, Date modifiedTime) {
        Address address = addressMapper.queryUserAddressByAid(aid);
        if (address == null){
            throw new AddressNotExistsException("地址不存在，删除失败");
        }
        int result = addressMapper.deleteAddressByAid(aid, modifiedUser, modifiedTime);

        if (result == 0){
            throw new DeleteException("服务器或数据出现异常，删除失败");
        }

        return result;
    }

    /**
     * 修改指定地址的具体逻辑
     *  address 实体类对象
     **/
    @Override
    public int updateOneAddress(Address address,String modifiedUser) {

        //获取并设置其他三个地址为null的字段
        String provinceName = districtService.getNameByCode(address.getProvinceCode());
        String cityName = districtService.getNameByCode(address.getCityCode());
        String areaName = districtService.getNameByCode(address.getAreaCode());

        address.setProvinceName(provinceName);
        address.setCityName(cityName);
        address.setAreaName(areaName);

        //补全表单中没有的其他字段
        address.setModifiedUser(modifiedUser);
        address.setModifiedTime(new Date());
        int result = addressMapper.updateUserAddressByAid(address);

        return result;
    }

}
