package phonestore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phonestore.entity.User;
import phonestore.service.exception.*;
import phonestore.utils.PasswordEncryptedUtils;
import phonestore.service.exception.*;
import phonestore.mapper.UserMapper;
import phonestore.service.IUserService;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class IUserServiceImpl implements IUserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    /**
     * 处理用户注册
     *  user 注册的用户信息
     * @return void
     **/
    @Override
    public void userRegister(User user) {
        //需要先判断用户名是否在数据库中重复
        User queryUser = userMapper.queryUserByUsername(user.getUsername());

        //重复的情况下，抛出用户名重复异常
        if (queryUser != null){
            throw new UsernameDuplicateException("用户名已被注册");
        }

        //密码不能以明文方式存入数据库，需要进行加密操作
        //密码加密的实现： 盐值 + password + 盐值 ---> md5算法进行加密，连续加载三次 ---> 得到最终存入数据库的结果
        //盐值就是一个随机的字符串
        //记录旧密码
        String oldPassword = user.getPassword();
        //使用UUID获取时间戳创建盐值
        String salt = UUID.randomUUID().toString().toUpperCase();

        //记录此刻的盐值，用于以后做用户登录判断
        user.setSalt(salt);

        //进行加密操作
        String md5Password = PasswordEncryptedUtils.getPasswordByMD5(oldPassword, salt);

        //将加密后的密码设置为用户设置的密码
        user.setPassword(md5Password);

        //在执行插入操作之前对一些表字段进行补全
        //is_delete字段设置为0，表示未删除用户
        user.setIsDelete(0);
        //0为非管理员用户
        user.setIsAdmin(0);

        //补全其余四个日志信息的字段
        Date currentTime = new Date();
        user.setCreatedUser(user.getUsername());
        user.setCreatedTime(currentTime);
        user.setModifiedUser(user.getUsername());
        user.setModifiedTime(currentTime);

        //不重复，调用插入方法,处理业务
        int result = userMapper.addUser(user);

        if (result == 0){ //判断服务器或数据库执行是否出现异常
            throw new InsertException("处理用户注册过程中，服务器或数据库执行出现异常");
        }
    }

    /**
     * 处理用户登录
     *  user 登录的用户信息
     * @return phonestore.entity.User
     **/
    public User userLogin(User user) {
        //用户名
        String username = user.getUsername();
        //用户输入的密码
        String userPassword = user.getPassword();

        //查询登录用户是否在数据库中存在
        User loginUser = userMapper.queryUserByUsername(username);

        if (loginUser == null){ //为空代表用户名不存在
            throw new UserNotExistException("该用户名账户不存在");
        }

        //取得数据库查询返回用户的盐值和密码以及删除状态
        String salt = loginUser.getSalt();
        String databasePwd = loginUser.getPassword();
        Integer deleteStatus = loginUser.getIsDelete();

        //对用户输入的密码进行加密
        String md5PasswordBy = PasswordEncryptedUtils.getPasswordByMD5(userPassword, salt);

        //将加密后的字符和数据库查询的MD5进行校验
        if (!databasePwd.equals(md5PasswordBy)){
            throw new WrongPasswordException("该账户密码不正确");
        }

        //判断登录的用户账户是否已注销
        if (deleteStatus == 1){
            throw new UserNotExistException("该用户名账户不存在");
        }

        //密码正确返回查询的用户信息
        return loginUser;
    }


    /**
     * 处理用户修改密码
     *  originalPassword 用户的原密码
     *  newPassword 用户要修改的新密码
     *  session 项目启动springboot提供的session对象，用于获取用户的uid
     * @return void
     **/
    @Override
    public void userResetPwd(String originalPassword,String newPassword, HttpSession session) {

        //从session中获取用户名
        String username = session.getAttribute("username").toString();

        //从数据库查询对应的信息
        User user = userMapper.queryUserByUsername(username);

        //取得数据库查询返回用户的盐值和密码
        String salt = user.getSalt();
        String databasePwd = user.getPassword();

        //对用户输入的密码进行加密
        String md5Password = PasswordEncryptedUtils.getPasswordByMD5(originalPassword, salt);

        //先判断原密码是否正确，将加密后的密码和数据库查询的MD5进行校验
        if (!databasePwd.equals(md5Password)){
            throw new OriginalPasswordNotMatchException("原密码不正确");
        }

        //将新密码进行加密
        String newMD5Pwd = PasswordEncryptedUtils.getPasswordByMD5(newPassword, salt);

        //先获取现在的时间
        Date currentTime = new Date();

        //更新密码
        int result = userMapper.updatePassword(newMD5Pwd,user.getUsername(),currentTime,username);

        if (result == 0){
            throw new InsertException("数据库或服务器故障，密码修改失败");
        }
    }

    /**
     * 处理用户更新个人资料
     *  phone 电话
     *  email 邮箱
     *  gender 性别
     *  username 用户名
     *  uid 用户id
     * @return int
     **/
    @Override
    public void userUpdateInfo(String phone, String email, Integer gender,String username,Integer uid) {

        //获取用户id并判断用户是否存在
        User user = userMapper.queryUserByUid(uid);

        if (user == null | user.getIsDelete() == 1){
            throw new UserNotExistException("用户数据不存在");
        }

        //修改用户信息
        int result = userMapper.UpdateUserInfo(phone, email, gender, username, new Date(), uid);

        if (result == 0){
            throw new InsertException("数据库或服务器异常，个人资料修改失败");
        }

    }

    /**
     * 根据用户id查询用户信息
     *  uid 用户id
     * @return phonestore.entity.User
     **/
    @Override
    public User queryUserByUid(Integer uid) {
        //根据uid查询用户信息返回给前端
        return userMapper.queryUserByUid(uid);
    }

    /**
     * 处理用户上传图片
     *  imgAddress 图片地址
     *  uid 用户id
     * @return void
     **/
    @Override
    public void userUploadImg(String imgAddress, Integer uid) {
        User user = userMapper.queryUserByUid(uid);
        if (user == null || user.getIsDelete() == 1){
            throw new UserNotExistException("用户数据不存在");
        }
        //插入图片
       int result  = userMapper.updateUserAvatar(imgAddress, user.getUsername(),new Date(),uid);

       if (result == 0){
           throw new InsertException("图片在服务器或数据库更新过程中出现错误，上传失败！");
       }

    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public void addUserByAdmin(User user) {
        // 检查用户名是否重复
        User existingUser = userMapper.queryUserByUsername(user.getUsername());
        if (existingUser != null) {
            throw new UsernameDuplicateException("用户名已被注册");
        }

        // 密码加密
        String oldPassword = user.getPassword();
        String salt = UUID.randomUUID().toString().toUpperCase();
        user.setSalt(salt);
        String md5Password = PasswordEncryptedUtils.getPasswordByMD5(oldPassword, salt);
        user.setPassword(md5Password);

        // 设置默认字段
        user.setIsDelete(0);
        Date currentTime = new Date();
        user.setCreatedUser("admin");
        user.setCreatedTime(currentTime);
        user.setModifiedUser("admin");
        user.setModifiedTime(currentTime);

        // 插入用户
        int result = userMapper.addUser(user);
        if (result == 0) {
            throw new InsertException("添加用户失败，服务器或数据库异常");
        }
    }

    @Override
    public void updateUserByAdmin(Integer uid, String phone, String email, Integer gender,
                                  Integer isAdmin, Integer isDelete) {
        // 检查用户是否存在
        User user = userMapper.queryUserByUid(uid);
        if (user == null) {
            throw new UserNotExistException("用户不存在");
        }

        // 更新用户信息
        int result = userMapper.updateUserByAdmin(phone, email, gender, isAdmin, isDelete,
                "admin", new Date(), uid);
        if (result == 0) {
            throw new InsertException("更新用户失败，服务器或数据库异常");
        }
    }

    @Override
    public void deleteUserByAdmin(Integer uid) {
        // 检查用户是否存在
        User user = userMapper.queryUserByUid(uid);
        if (user == null) {
            throw new UserNotExistException("用户不存在");
        }

        // 逻辑删除
        int result = userMapper.updateUserByAdmin(null, null, null, null, 1,
                "admin", new Date(), uid);
        if (result == 0) {
            throw new InsertException("删除用户失败，服务器或数据库异常");
        }
    }

}
