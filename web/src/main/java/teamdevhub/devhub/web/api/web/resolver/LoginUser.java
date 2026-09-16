package teamdevhub.devhub.web.api.web.resolver;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {

    boolean required() default true;

}