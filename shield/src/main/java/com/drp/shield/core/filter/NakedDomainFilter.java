package com.drp.shield.core.filter;

import com.drp.shield.config.EnvConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.drp.shield.utils.SpringContextUtils.getBean;
import static com.drp.shield.utils.SpringContextUtils.isDev;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month06day  00:40:41
 */
@Slf4j
public class NakedDomainFilter extends OncePerRequestFilter {

    private final ThreadLocal<EnvConfig> envLocal = new ThreadLocal<>();
    private static final String DEFAULT_SERVICE = "www";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        final EnvConfig env = initEnv();
        // if you're hitting naked domain - go to www
        // e.g. donrp.club/foo?true=1 should redirect to www.donrp.club/foo?true=1
        if (env.getServerName().equals(request.getServerName())) {
            log.info("hitting naked domain - redict to www");
            String scheme = "http";
            if (!isDev()) {
                scheme = "https";
            }
            try {
                URI redirectUrl = new URI(scheme,
                        null,
                        DEFAULT_SERVICE + "." + env.getServerName(),
                        request.getServerPort(),
                        request.getRequestURI(),
                        request.getQueryString(), null);
                response.sendRedirect(redirectUrl.toString());
            } catch (URISyntaxException e) {
                log.error("fail to build redirect url", e);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private EnvConfig initEnv() {
        if (envLocal.get() == null) {
            envLocal.set(getBean(EnvConfig.class));
        }
        return envLocal.get();
    }
}
