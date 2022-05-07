package cn.shh.project.reggie.common.filter;

import cn.shh.project.reggie.util.BaseContext;
import cn.shh.project.reggie.util.R;
import cn.shh.project.reggie.util.ReggieConstant;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 放行的请求
        String[] uris = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };

        // 1、拿到请求uri
        String requestURI = request.getRequestURI();
        // 2、判断请求是否要处理
        boolean check = checkURI(uris, requestURI);
        // 3、不需要处理，则放行
        if (check){
            filterChain.doFilter(request, response);
            return;
        }
        // 4.1、已登录，放行
        if (request.getSession().getAttribute("employee") != null){
            filterChain.doFilter(request, response);
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            return;
        }
        // 4.2、已登录，放行
        if (request.getSession().getAttribute("user") != null){
            filterChain.doFilter(request, response);
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            return;
        }
        // 5、未登录，跳到登录页面
        response.getWriter().write(JSON.toJSONString(R.error(ReggieConstant.NOT_LOGIN)));
        return;
    }

    private boolean checkURI(String[] uris, String requestURI){
        for (String uri : uris) {
            boolean b = PATH_MATCHER.match(uri, requestURI);
            if (b) {
                return true;
            }
        }
        return false;
    }
}
