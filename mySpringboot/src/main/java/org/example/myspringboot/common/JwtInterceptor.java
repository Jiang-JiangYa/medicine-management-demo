package org.example.myspringboot.common;
import org.example.myspringboot.entity.User;
import org.example.myspringboot.exception.ServiceException;
import org.example.myspringboot.utils.TokenUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class JwtInterceptor implements HandlerInterceptor {
    @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (path.equals("/user/register") || path.equals("/user/password")) {
            response.setStatus(403); throw new ServiceException("403", "演示环境已关闭此功能");
        }
        try {
            String token = request.getHeader("Token");
            if (token == null && request.getCookies() != null) {
                for (javax.servlet.http.Cookie cookie : request.getCookies())
                    if ("demo_session".equals(cookie.getName())) token = cookie.getValue();
            }
            User user = TokenUtils.verify(token);
            if (user == null) throw new IllegalArgumentException();
            request.setAttribute("currentUser", user);
            if (handler instanceof HandlerMethod && org.example.myspringboot.controller.UserController.class.isAssignableFrom(((HandlerMethod) handler).getBeanType())
                && !"admin".equalsIgnoreCase(user.getRole())) {
                response.setStatus(403); throw new ServiceException("403", "仅管理员可管理用户");
            }
            return true;
        } catch (ServiceException e) { throw e; }
        catch (Exception e) { response.setStatus(401); throw new ServiceException("401", "请重新登录账号"); }
    }
}
