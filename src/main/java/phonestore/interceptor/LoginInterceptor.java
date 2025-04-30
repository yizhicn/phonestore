package phonestore.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 检测全局session对象中是否由uid数据，有则放行，没有则拦截
     *  request 请求对象
     *  response 响应对象
     *  handler 处理器（url+Controller：映射）
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取项目工程的session
        HttpSession session = request.getSession();
        // 获取请求的URI
        String uri = request.getRequestURI();

        // 检查是否为admin.html或admin_p.html请求
        if (uri.endsWith("/web/admin.html") || uri.endsWith("/web/admin_p.html") || uri.endsWith("/web/admin_u.html") || uri.endsWith("/web/admin_o.html")) {
            // 检查是否登录
            if (session.getAttribute("uid") == null) {
                // 未登录，重定向到登录页面
                response.sendRedirect("/web/login.html");
                return false;
            }
            // 检查是否为管理员
            Integer isAdmin = (Integer) session.getAttribute("isAdmin");
            if (isAdmin == null || isAdmin != 1) {
                // 非管理员，重定向到首页
                response.sendRedirect("/web/index.html"); // 或者重定向到无权限页面，如 "/web/no-permission.html"
                return false;
            }
            // 是管理员，放行
            return true;
        }

        if (session.getAttribute("uid") != null){ //说明此时已登录
            return true;
        }else{ //说明未登录
            //重定向至登录页面
            response.sendRedirect("/web/login.html");
            return false;
        }



    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
