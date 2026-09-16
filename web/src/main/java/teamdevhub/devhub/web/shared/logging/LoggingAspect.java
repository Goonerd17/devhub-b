package teamdevhub.devhub.web.shared.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.support.AopUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private static final String EMPTY_JSON = "[]";
    private static final String NULL_JSON = "null";
    private static final String MASKED_VALUE = "******";

    private static final int SAMPLE_LIMIT = 5;

    private static final List<String> SENSITIVE_FIELDS = List.of(
            "password",
            "newPassword",
            "oldPassword",
            "confirmPassword",
            "encodedPassword",
            "token",
            "accessToken",
            "refreshToken",
            "tempToken",
            "secret",
            "authorization"
    );

    private final ObjectMapper objectMapper;

    @Around("execution(* teamdevhub.devhub.web.api..*(..)) || " +
            "execution(* teamdevhub.devhub..core..*(..)) || " +
            "execution(* teamdevhub.devhub..outbound..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Class<?> targetClass = AopUtils.getTargetClass(joinPoint.getTarget());
        String className = simplifyClassName(targetClass);

        if (className == null) {
            return joinPoint.proceed();
        }

        String methodName = joinPoint.getSignature().getName();
        String params = getParamsAsJson(joinPoint.getArgs());

        log.info("[START] {}.{}() with params: {}", className, methodName, params);

        Object result = joinPoint.proceed();

        long elapsed = System.currentTimeMillis() - start;
        String resultLog = summarizeResult(result);

        log.info("[END] {}.{}() in {} ms with result: {}", className, methodName, elapsed, resultLog);

        return result;
    }

    private String simplifyClassName(Class<?> clazz) {
        String simpleName = clazz.getSimpleName();

        if (simpleName.contains("BooleanToYNConverter")) {
            return null;
        }

        return simpleName;
    }

    private String getParamsAsJson(Object[] args) {
        if (args == null || args.length == 0) {
            return EMPTY_JSON;
        }

        Object[] maskedArgs = new Object[args.length];

        for (int i = 0; i < args.length; i++) {
            maskedArgs[i] = maskSensitiveFields(args[i]);
        }

        return toSafeJson(maskedArgs);
    }

    private String summarizeResult(Object result) {
        if (result == null) {
            return NULL_JSON;
        }

        if (result instanceof Page<?> page) {
            return "Page{contentSize=%d, page=%d, size=%d, totalElements=%d}"
                    .formatted(
                            page.getNumberOfElements(),
                            page.getNumber(),
                            page.getSize(),
                            page.getTotalElements()
                    );
        }

        if (result instanceof Collection<?> collection) {
            return summarizeCollection(collection);
        }

        return toSafeJson(maskSensitiveFields(result));
    }

    private String summarizeCollection(Collection<?> collection) {
        int size = collection.size();

        if (size <= SAMPLE_LIMIT) {
            return toSafeJson(maskSensitiveFields(collection));
        }

        List<?> sample = collection.stream()
                .limit(SAMPLE_LIMIT)
                .map(this::maskSensitiveFields)
                .toList();

        return "%s... (total %d items)".formatted(toSafeJson(sample), size);
    }

    private Object maskSensitiveFields(Object source) {
        if (source == null) {
            return null;
        }

        if (isSimpleType(source.getClass())) {
            return source;
        }

        if (source instanceof Collection<?> collection) {
            return collection.stream()
                    .map(this::maskSensitiveFields)
                    .toList();
        }

        Map<String, Object> result = new LinkedHashMap<>();

        Class<?> type = source.getClass();

        while (type != null && type != Object.class) {
            Field[] fields = type.getDeclaredFields();

            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(source);

                    if (isSensitiveField(field.getName())) {
                        result.put(field.getName(), MASKED_VALUE);
                    } else {
                        result.put(field.getName(), value);
                    }
                } catch (IllegalAccessException | InaccessibleObjectException ignored) {
                    result.put(field.getName(), "[unreadable]");
                }
            }

            type = type.getSuperclass();
        }

        return result;
    }

    private boolean isSensitiveField(String fieldName) {
        return SENSITIVE_FIELDS.stream()
                .anyMatch(sensitive ->
                        sensitive.equalsIgnoreCase(fieldName));
    }

    private boolean isSimpleType(Class<?> type) {
        return type.isPrimitive()
                || String.class.equals(type)
                || Number.class.isAssignableFrom(type)
                || Boolean.class.equals(type)
                || Enum.class.isAssignableFrom(type)
                || type.getPackageName().startsWith("java.time");
    }

    private String toSafeJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return Optional.ofNullable(object)
                    .map(Object::toString)
                    .orElse(NULL_JSON);
        }
    }
}
