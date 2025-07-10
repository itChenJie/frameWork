package org.basis.framework.security;

import org.springframework.boot.actuate.endpoint.SecurityContext;

/**
 * @Description
 * @Author ChenJie
 **/
public class SecurityContextHolder {
    private static  ThreadLocal<SecurityContext> holder= new ThreadLocal<>();

    public static SecurityContext getContext(){
        SecurityContext securityContext = holder.get();
        if (securityContext == null) {
            return null;
        }
        return securityContext;
    }

    public static void setContext(SecurityContext context) {
        SecurityContext securityContext = holder.get();
        if (securityContext != null) {
            holder.remove();
        }
        holder.set(context);
    }

}
