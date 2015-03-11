package org.evilkitten.gitboard.application.security;

import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.regex.Pattern;

import org.evilkitten.gitboard.application.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class UserAuthenticatedFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(UserAuthenticatedFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        StringBuilder sb = new StringBuilder(request.getServletPath());
        if (request.getPathInfo() != null) {
            sb.append(request.getPathInfo());
        }
        String path = sb.toString();

        Pattern re = Pattern.compile("^/(api/user|api/login|public|signin).*");
        if (!re.matcher(path).matches()) {
            //LOG.info("Checking to see if user is authenticated for {}", path);
            Object sessionUser = session.getAttribute("session.user");
            if (sessionUser == null || !(sessionUser instanceof User)) {
                String redirectPath = request.getContextPath() + "/signin";
                response.sendRedirect(redirectPath);
                return;
            }
            User user = (User) sessionUser;
            //LOG.info("User is logged in as {}", user.getEmail());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
