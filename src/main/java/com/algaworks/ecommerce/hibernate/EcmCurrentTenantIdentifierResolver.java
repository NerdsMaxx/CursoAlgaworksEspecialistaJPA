package com.algaworks.ecommerce.hibernate;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class EcmCurrentTenantIdentifierResolver implements CurrentTenantIdentifierResolver {
    
    private static ThreadLocal<String> t1 = new ThreadLocal<>();
    
    public static void setTenantIdentifier(String tenantId) {
        t1.set(tenantId);
    }
    
    @Override
    public String resolveCurrentTenantIdentifier() {
        return t1.get();
    }
    
    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}