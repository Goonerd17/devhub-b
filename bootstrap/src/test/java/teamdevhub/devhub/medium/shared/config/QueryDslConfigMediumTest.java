package teamdevhub.devhub.medium.shared.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
class QueryDslConfigMediumTest {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    @DisplayName("jpaQueryFactory_가_생성된다")
    void createJPAQueryFactory() {
        // given, when, then
        assertThat(jpaQueryFactory).isNotNull();
    }
}
