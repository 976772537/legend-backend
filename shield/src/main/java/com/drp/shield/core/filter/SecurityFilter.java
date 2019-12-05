package com.drp.shield.core.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static cn.hutool.core.util.StrUtil.isEmpty;
import static com.drp.shield.utils.SpringContextUtils.isDev;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month05day  21:31:37
 */
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        final String origin = request.getHeader(HttpHeaders.ORIGIN);
        if (!isEmpty(origin)) {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, PUT, DELETE");
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Accept, Content-Type, Content-Length, Cookie, Accept-Encoding, X-CSRF-Token, Authorization");
        }
        //stop options request at this here
        if ("OPTIONS".equals(request.getMethod())) {
            return;
        }
        //if env isn't dev
        if (!isDev()) {
            // Check if secure
            boolean isSecure = request.isSecure();
            if (!isSecure) {
                // Check if frontend proxy proxied it
                if ("https".equals(request.getHeader("X-Forwarded-Proto"))) {
                    isSecure = true;
                }
            }

            // If not secure, then redirect
            if (!isSecure) {
                log.info("Insecure quest in test & prod environment, redirect to https");
                try {
                    URI redirectUrl = new URI("https",
                            request.getServerName(),
                            request.getRequestURI(), null);
                    response.sendRedirect(redirectUrl.toString());
                } catch (URISyntaxException e) {
                    log.error("fail to build redirect url >> ", e);
                }
                return;
            }

            // HSTS - force SSL
            response.setHeader("Strict-Transport-Security", "max-age=315360000; includeSubDomains; preload");
            // only iframe by myself
            response.setHeader("X-Frame-Options", "SAMEORIGIN");
            // Cross-site scripting protection
            response.setHeader("X-XSS-Protection", "1; mode=block");
        }
        chain.doFilter(request, response);
    }
}
