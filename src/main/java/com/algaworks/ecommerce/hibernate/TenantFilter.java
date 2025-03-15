package com.algaworks.ecommerce.hibernate;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

public class TenantFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }
    
    @Override
    public void doFilter(ServletRequest httpServletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) httpServletRequest;
        
//        String serverName = request.getServerName();
//        String subdomain = serverName.substring(0, serverName.indexOf("."));
//
//        EcmCurrentTenantIdentifierResolver.setTenantIdentifier(subdomain + "_ecommerce");
        
        System.out.println("Map = " + request.getParameterMap());
        String tenant = Objects.requireNonNullElse(request.getParameter("tenant"), "algaworks");
        
        EcmCurrentTenantIdentifierResolver.setTenantIdentifier(tenant + "_ecommerce");
        
        filterChain.doFilter(httpServletRequest, servletResponse);
    }
    
    @Override
    public void destroy() {}
}