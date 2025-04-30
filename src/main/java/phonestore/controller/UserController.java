package phonestore.controller;

import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import phonestore.controller.exception.*;
import phonestore.controller.BaseController;
import phonestore.entity.User;
import phonestore.utils.JsonResult;
import phonestore.service.IUserService;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private IUserService userService;

    //用户注册
    @PostMapping
    public JsonResult<Void> userRegister(User user, HttpSession session, String code) {
        //从session取出验证码
        String validCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        //判断验证码是否一致
        if (!validCode.equals(code)){
            throw new ValidCodeNotMatchException("验证码错误,请重试！");
        }
        //执行插入操作
        userService.userRegister(user);
        return new JsonResult<>(OK);
    }

    //用户登录
    @GetMapping
    public JsonResult<User> userLogin(User user, HttpSession session,String code){
        //将存储在session的kaptcha所生成的验证码取出
        String validCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        //判断验证码是否一致
        if (!validCode.equals(code)){
            throw new ValidCodeNotMatchException("验证码错误,请重试！");
        }
        //执行登录操作
        User loginUser = userService.userLogin(user);
        //分别将用户的session保存到服务端
        session.setAttribute("uid",loginUser.getUid());
        session.setAttribute("username",loginUser.getUsername());
        session.setAttribute("isAdmin", loginUser.getIsAdmin());
        //优化一下传回前端的user数据，有些字段是不需要的。
        User newUser = new User();
        newUser.setUsername(loginUser.getUsername());
        newUser.setUid(loginUser.getUid());
        newUser.setGender(loginUser.getGender());
        newUser.setPhone(loginUser.getPhone());
        newUser.setEmail(loginUser.getEmail());
        newUser.setAvatar(loginUser.getAvatar());

        return new JsonResult<>(OK,newUser);
    }

    //管理员登录方法
    @PostMapping("/adminLogin")
    public JsonResult<User> adminLogin(User user, HttpSession session, String code) {
        String validCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (!validCode.equals(code)) {
            throw new ValidCodeNotMatchException("验证码错误,请重试！");
        }
        User loginUser = userService.userLogin(user);
        if (loginUser.getIsAdmin() == null || loginUser.getIsAdmin() != 1) {
            throw new AccessDeniedException("非管理员账户，无权限登录");
        }
        session.setAttribute("uid", loginUser.getUid());
        session.setAttribute("username", loginUser.getUsername());
        session.setAttribute("isAdmin", loginUser.getIsAdmin());
        User newUser = new User();
        newUser.setUsername(loginUser.getUsername());
        newUser.setUid(loginUser.getUid());
        newUser.setGender(loginUser.getGender());
        newUser.setPhone(loginUser.getPhone());
        newUser.setEmail(loginUser.getEmail());
        newUser.setAvatar(loginUser.getAvatar());
        return new JsonResult<>(OK, newUser);
    }

    //用户重置密码
    @PostMapping("/resetPassword")
    public JsonResult<Void> userResetPwd(@RequestParam("oldPassword") String oldPwd,
                                         @RequestParam("newPassword") String newPwd,
                                         HttpSession session){
        userService.userResetPwd(oldPwd, newPwd, session);

        //在用户修改密码之后清除session中保存的密码
        session.setAttribute("uid",null);
        return new JsonResult<>(OK);
    }

    @GetMapping("/queryUser")
    public JsonResult<User> queryUserByUid(HttpSession session){
        Integer uid = getUserIdFromSession(session);

        User user = userService.queryUserByUid(uid);

        //将用户名、id、电话、邮箱、性别进行回传
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setUid(user.getUid());
        newUser.setGender(user.getGender());
        newUser.setPhone(user.getPhone());
        newUser.setEmail(user.getEmail());
        newUser.setAvatar(user.getAvatar());

        return new JsonResult<>(OK,newUser);
    }


    //用户个人信息更新
    @PostMapping("/updateInfo")
    public JsonResult<User> userInfoUpdate(String phone,String email,Integer gender,HttpSession session){
        //从session中取出用户名和uid
        String username = getUsernameFromSession(session);
        Integer uid = getUserIdFromSession(session);

        //更新数据
        userService.userUpdateInfo(phone, email, gender, username, uid);

        User user = userService.queryUserByUid(uid);

        //将用户名、id、电话、邮箱、性别进行回传
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setUid(user.getUid());
        newUser.setGender(user.getGender());
        newUser.setPhone(user.getPhone());
        newUser.setEmail(user.getEmail());
        newUser.setAvatar(user.getAvatar());

        return new JsonResult<>(OK,newUser);
    }

    //处理用户退出登录的请求
    @GetMapping("/exit")
    public JsonResult<Void> exitUserLoginStatus(HttpSession session){
        session.removeAttribute("username");
        session.removeAttribute("uid");
        return new JsonResult<>(OK);
    }
    //设置上传的头像的最大值为10mb
    public static final int AVATAR_MAX_SIZE = 10 * 1024 * 1024;

    //限制上传文件的类型
    public static final List<String> AVATAR_TYPE = new ArrayList<>();

    static {
        AVATAR_TYPE.add("image/jpeg");
        AVATAR_TYPE.add("image/png");
        AVATAR_TYPE.add("image/bmp");
        AVATAR_TYPE.add("image/gif");
        AVATAR_TYPE.add("image/jpg");
    }


    @RequestMapping("/updateAvatarByUid")
    public JsonResult updateAvatarByUid(HttpSession session, MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileEmptyException("文件为空");
        } else if (file.getSize() > AVATAR_MAX_SIZE) {
            throw new FileSizeException("文件大小超出限制");
        } else if (!AVATAR_TYPE.contains(file.getContentType())) {
            throw new FileTypeNotMatchException("文件类型异常");
        }
        //将头像文件放在本项目部署目录的upload目录下，parent是upload的真实路径
        String parent = session.getServletContext().getRealPath("upload");
        //File对象指向这个路径
        File dir = new File(parent);
        //判断这个路径是否存在
        if (!dir.exists()) {
            dir.mkdirs();  //创建当前目录
        }

        //获取到这个文件名称，UUID工具来生成一个新的字符串作为文件名
        //获取文件的名称
        String originalFilename = file.getOriginalFilename();
        //获取文件名称中"."的下表索引
        int index = originalFilename.lastIndexOf(".");
        //从"."开始截取到最后，获取这个文件的后缀
        String substring = originalFilename.substring(index);
        //用UUID生成一串字符串作为文件的名字
        String filename = UUID.randomUUID().toString().toUpperCase() + substring;

        //创建一个和上面的文件同名的空文件
        File dest = new File(dir, filename);
        //transferTo将参数file中的数据传入dest中
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new FileEmptyException("文件读写时发生未知异常");
        }

        Integer uid = getUserIdFromSession(session);
        String username = getUsernameFromSession(session);
        //头像的路径
        String imgAddress = "/upload/" + filename;
        userService.userUploadImg(imgAddress,  uid);
        //将头像的路径返回给前端，将来用于头像展示使用
        return new JsonResult(OK,imgAddress);
    }
    // 查询所有用户
    @GetMapping("/allUsers")
    public JsonResult<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new JsonResult<>(OK, users);
    }

    // 管理员新增用户
    @PostMapping("/addAdmin")
    public JsonResult<Void> addUserByAdmin(@RequestBody User user) {
        userService.addUserByAdmin(user);
        return new JsonResult<>(OK);
    }

    // 管理员更新用户信息
    @PostMapping("/updateAdmin")
    public JsonResult<Void> updateUserByAdmin(@RequestBody User user) {
        userService.updateUserByAdmin(user.getUid(), user.getPhone(), user.getEmail(),
                user.getGender(), user.getIsAdmin(), user.getIsDelete());
        return new JsonResult<>(OK);
    }

    // 管理员删除用户
    @PostMapping("/deleteAdmin")
    public JsonResult<Void> deleteUserByAdmin(@RequestParam("uid") Integer uid) {
        userService.deleteUserByAdmin(uid);
        return new JsonResult<>(OK);
    }
}
