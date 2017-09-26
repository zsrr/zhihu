package com.stephen.zhihu.base;

import javax.servlet.*;
import java.io.IOException;

public class AccessCounterFilter implements Filter {

    private RedisAccessCounter counter;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String host = filterConfig.getInitParameter("jedis.host");
        String port = filterConfig.getInitParameter("jedis.port");
        counter = new RedisAccessCounter(host, Integer.parseInt(port));
        counter.clean();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        counter.updateCounter();
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        counter.close();
    }
}
