package com.drp.shield.core.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month07day  09:52:07
 */
public class HealthCheckFilter extends OncePerRequestFilter {
    private static final String HEALTH_CHECK_PATH = "/health";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if (HEALTH_CHECK_PATH.equals(request.getRequestURI())) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print("OK");
        } else {
            chain.doFilter(request, response);
        }
    }
}
