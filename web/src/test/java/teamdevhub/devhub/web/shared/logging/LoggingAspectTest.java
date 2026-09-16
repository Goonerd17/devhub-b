package teamdevhub.devhub.web.shared.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

class LoggingAspectTest {

    private final LoggingAspect loggingAspect = new LoggingAspect(new ObjectMapper());

    @Test
    @DisplayName("logAround_는_시작과_끝을_로그한다")
    void logStartAndEnd() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        DummyService dummyService = new DummyService();
        when(joinPoint.getTarget()).thenReturn(dummyService);
        when(joinPoint.getArgs()).thenReturn(new Object[]{"world"});

        // when
        MethodSignature methodSignature = mock(MethodSignature.class);
        when(methodSignature.getName()).thenReturn("hello");
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(joinPoint.proceed()).thenReturn("HELLO WORLD");
        Object result = loggingAspect.logAround(joinPoint);

        // then
        assertThat(result).isEqualTo("HELLO WORLD");
    }

    @Test
    @DisplayName("컬렉션이_SAMPLE_LIMIT_이하이면_전체를_로그한다")
    void logCollectionUnderSampleLimit() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature signature = mock(MethodSignature.class);

        DummyCollectionService dummyCollectionService = new DummyCollectionService();
        List<String> values = List.of("a", "b");

        when(joinPoint.getTarget()).thenReturn(dummyCollectionService);
        when(joinPoint.getArgs()).thenReturn(new Object[]{values});
        when(signature.getName()).thenReturn("echo");
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.proceed()).thenReturn("OK");

        Object result = loggingAspect.logAround(joinPoint);

        assertThat(result).isEqualTo("OK");
    }

    @Test
    @DisplayName("컬렉션이_SAMPLE_LIMIT_을_초과하면_샘플만_로그한다")
    void logCollectionOverSampleLimit() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature signature = mock(MethodSignature.class);

        DummyCollectionService dummyCollectionService = new DummyCollectionService();
        List<String> values = IntStream.range(0, 20)
                .mapToObj(String::valueOf)
                .toList();

        when(joinPoint.getTarget()).thenReturn(dummyCollectionService);
        when(joinPoint.getArgs()).thenReturn(new Object[]{values});
        when(signature.getName()).thenReturn("echo");
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.proceed()).thenReturn("OK");

        Object result = loggingAspect.logAround(joinPoint);

        assertThat(result).isEqualTo("OK");
    }

    static class DummyService {
        public String hello(String name) {
            return "HELLO " + name;
        }
    }

    static class DummyCollectionService {
        public String echo(Collection<String> values) {
            return "OK";
        }
    }
}
