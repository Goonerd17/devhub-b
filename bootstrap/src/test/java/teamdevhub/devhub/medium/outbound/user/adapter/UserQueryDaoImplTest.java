package teamdevhub.devhub.medium.outbound.user.adapter;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.member.outbound.user.adapter.entity.UserEntity;
import teamdevhub.devhub.member.outbound.user.persistence.UserQueryDao;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
@Transactional
class UserQueryDaoImplTest {

    @Autowired
    private UserQueryDao userQueryDao;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void cleanUp() {
        entityManager.createQuery("DELETE FROM UserEntity").executeUpdate();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("차단된_사용자_검색조건이_적용되면_차단된_사용자만_조회된다")
    void listUserWithBlockedTrueFilterReturnsCorrectResults() {
        // given
        String randomGuid1 = UUID.randomUUID().toString().replace("-", "");
        String randomGuid2 = UUID.randomUUID().toString().replace("-", "");
        UserEntity userEntity1 = UserEntity.builder()
                .userGuid(randomGuid1)
                .username(TEST_USERNAME_1)
                .userRole(UserRole.USER)
                .mannerDegree(50.0)
                .blocked(false)
                .deleted(false)
                .introduction(TEST_INTRO_1)
                .build();

        UserEntity userEntity2 = UserEntity.builder()
                .userGuid(randomGuid2)
                .username(TEST_USERNAME_2)
                .userRole(UserRole.USER)
                .mannerDegree(50.0)
                .blocked(true)
                .deleted(false)
                .introduction(TEST_INTRO_2)
                .build();

        entityManager.persist(userEntity1);
        entityManager.persist(userEntity2);
        entityManager.flush();

        SearchUserCommand searchUserCommand = new SearchUserCommand(true, null, null, null);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<UserEntity> result = userQueryDao.listUser(searchUserCommand, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getUsername()).isEqualTo(TEST_USERNAME_2);
    }

    @Test
    @DisplayName("키워드_검색조건이_적용되면_해당_사용자만_조회된다")
    void listUserWithKeywordFilterReturnsCorrectResults() {
        // given
        String randomGuid1 = UUID.randomUUID().toString().replace("-", "");
        String randomGuid2 = UUID.randomUUID().toString().replace("-", "");
        UserEntity userEntity1 = UserEntity.builder()
                .userGuid(randomGuid1)
                .username(TEST_USERNAME_1)
                .userRole(UserRole.USER)
                .mannerDegree(50.0)
                .blocked(false)
                .deleted(false)
                .introduction(TEST_INTRO_1)
                .build();

        UserEntity userEntity2 = UserEntity.builder()
                .userGuid(randomGuid2)
                .username(TEST_USERNAME_2)
                .userRole(UserRole.USER)
                .mannerDegree(50.0)
                .blocked(true)
                .deleted(false)
                .introduction(TEST_INTRO_2)
                .build();

        entityManager.persist(userEntity1);
        entityManager.persist(userEntity2);
        entityManager.flush();

        SearchUserCommand searchUserCommand = new SearchUserCommand(null, null, null, TEST_USERNAME_1);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<UserEntity> result = userQueryDao.listUser(searchUserCommand, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getUsername()).isEqualTo(TEST_USERNAME_1);
    }

    @Test
    @DisplayName("모든_조건이_조합되면_해당_사용자만_조회된다")
    void listUserWithAllFiltersReturnsCorrectResults() {
        // given
        String randomGuid1 = UUID.randomUUID().toString().replace("-", "");
        String randomGuid2 = UUID.randomUUID().toString().replace("-", "");
        UserEntity userEntity1 = UserEntity.builder()
                .userGuid(randomGuid1)
                .username(TEST_USERNAME_1)
                .userRole(UserRole.USER)
                .mannerDegree(50.0)
                .blocked(false)
                .deleted(false)
                .introduction(TEST_INTRO_1)
                .build();

        UserEntity userEntity2 = UserEntity.builder()
                .userGuid(randomGuid2)
                .username(TEST_USERNAME_2)
                .userRole(UserRole.USER)
                .mannerDegree(50.0)
                .blocked(true)
                .deleted(false)
                .introduction(TEST_INTRO_2)
                .build();

        entityManager.persist(userEntity1);
        entityManager.persist(userEntity2);
        entityManager.flush();

        LocalDateTime joinedFrom = LocalDateTime.now().minusDays(3);
        LocalDateTime joinedTo = LocalDateTime.now();
        SearchUserCommand searchUserCommand = new SearchUserCommand(true, joinedFrom, joinedTo, TEST_USERNAME_2);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<UserEntity> result = userQueryDao.listUser(searchUserCommand, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getUsername()).isEqualTo(TEST_USERNAME_2);
    }

    @Test
    @DisplayName("조건이_없으면_모든_사용자를_조회한다")
    void listUserWithNoFiltersReturnsAllResults() {
        // given
        String randomGuid1 = UUID.randomUUID().toString().replace("-", "");
        String randomGuid2 = UUID.randomUUID().toString().replace("-", "");
        UserEntity userEntity1 = UserEntity.builder()
                .userGuid(randomGuid1)
                .username(TEST_USERNAME_1)
                .userRole(UserRole.USER)
                .mannerDegree(50.0)
                .blocked(false)
                .deleted(false)
                .introduction(TEST_INTRO_1)
                .build();

        UserEntity userEntity2 = UserEntity.builder()
                .userGuid(randomGuid2)
                .username(TEST_USERNAME_2)
                .userRole(UserRole.USER)
                .mannerDegree(50.0)
                .blocked(true)
                .deleted(false)
                .introduction(TEST_INTRO_2)
                .build();

        entityManager.persist(userEntity1);
        entityManager.persist(userEntity2);
        entityManager.flush();

        SearchUserCommand searchUserCommand = new SearchUserCommand(null, null, null, null);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<UserEntity> result = userQueryDao.listUser(searchUserCommand, pageable);

        // then
        assertThat(result.getContent()).hasSize(2);
    }
}
