package phonestore.mapper;

import org.apache.ibatis.annotations.Param;
import phonestore.entity.User;

import java.util.Date;
import java.util.List;


public interface UserMapper {

    /**
     * 用户注册
     *  user 用户信息
     * @return int 插入的结果
     **/
    int addUser(User user);

    /**
     * 根据用户名查询用户信息
     *  username 用户名
     **/
    User queryUserByUsername(String username);

    /**
     * 用户重置密码
     *  password 要修改的密码
     *  modifiedUser 修改人
     *  modifiedTime 修改时间
     *  username 用户名
     **/
    int updatePassword(String password,
                       String modifiedUser,
                       Date modifiedTime,
                       String username);


    /**
     * 根据id查询用户信息
     *  uid 用户id
     **/
    User queryUserByUid(Integer uid);

    /**
     * 更新用户信息
     *  phone 电话
     *  email 邮箱
     *  gender 性别
     *  modifiedUser 修改人
     *  modifiedTime 修改时间
     *  uid 用户id
     **/
    int UpdateUserInfo(String phone,
                       String email,
                       Integer gender,
                       String modifiedUser,
                       Date modifiedTime,
                       Integer uid);
    /**
     * 处理用户上传头像
     *  ImgAddress 保存图片的地址
     *  modifiedUser 修改人
     *  modifiedTime 修改时间
     *  uid 用户uid
     **/
    int updateUserAvatar(@Param("file") String ImgAddress,
                         String modifiedUser,
                         Date modifiedTime,
                         Integer uid);



    // 查询所有用户
    List<User> getAllUsers();

    // 管理员更新用户信息
    int updateUserByAdmin(@Param("phone") String phone,
                          @Param("email") String email,
                          @Param("gender") Integer gender,
                          @Param("isAdmin") Integer isAdmin,
                          @Param("isDelete") Integer isDelete,
                          @Param("modifiedUser") String modifiedUser,
                          @Param("modifiedTime") Date modifiedTime,
                          @Param("uid") Integer uid);
}
