package teamdevhub.devhub.community.http.resolver;
import java.lang.annotation.*;
@Target(ElementType.PARAMETER) @Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser { boolean required() default true; }
