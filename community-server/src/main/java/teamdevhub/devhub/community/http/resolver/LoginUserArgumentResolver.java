package teamdevhub.devhub.community.http.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    public boolean supportsParameter(MethodParameter p) { return p.hasParameterAnnotation(LoginUser.class) && p.getParameterType().equals(String.class); }
    public Object resolveArgument(MethodParameter p, ModelAndViewContainer m, NativeWebRequest r, WebDataBinderFactory b) {
        Authentication a=SecurityContextHolder.getContext().getAuthentication();
        if (a==null || a.getPrincipal()==null) return null;
        Object principal=a.getPrincipal();
        if (principal instanceof String s) return s;
        try { return principal.getClass().getMethod("getSubject").invoke(principal); } catch (ReflectiveOperationException e) { return principal.toString(); }
    }
}
