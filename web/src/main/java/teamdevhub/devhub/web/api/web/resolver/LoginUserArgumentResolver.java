package teamdevhub.devhub.web.api.web.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.identity.api.AuthenticatedUserCarrier;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.platform.outbound.common.exception.AuthRuleException;

import java.util.Optional;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginUser.class) && methodParameter.getParameterType().equals(AuthenticatedUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) {

        LoginUser loginUser = methodParameter.getParameterAnnotation(LoginUser.class);

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            if (loginUser.required()) {
                throw AuthRuleException.of(ErrorCode.USER_NOT_FOUND);
            }
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AuthenticatedUserCarrier carrier) {
            return carrier.authenticatedUser();
        }

        if (principal instanceof AuthenticatedUser authenticatedUser) {
            return authenticatedUser;
        }

        if (loginUser.required()) {
            throw AuthRuleException.of(ErrorCode.USER_NOT_FOUND);
        }

        return null;
    }
}
