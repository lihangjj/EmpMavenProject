package util.filter;

import util.validate.ResourceUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "LoginFilter", urlPatterns = "/pages/*")
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;

        HttpSession session = request.getSession();
        if (session.getAttribute("mid") != null) {
            chain.doFilter(req, response);
        } else {
            req.setAttribute("msg", ResourceUtils.get("Messages", "login.check.failure.msg"));
            req.setAttribute("url", ResourceUtils.get("Pages", "login.page"));
            req.getRequestDispatcher(ResourceUtils.get("Pages", "forward.page")).forward(req, response);

        }
    }
    public void init(FilterConfig config) throws ServletException {
    }
}
