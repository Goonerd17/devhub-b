package teamdevhub.devhub.member.outbound.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.outbound.user.adapter.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUserGuid(String userGuid);
    boolean existsByUserRole(UserRole userRole);

    @Modifying
    @Query("UPDATE UserEntity u SET u.lastLoginDateTime = :lastLoginDateTime WHERE u.userGuid = :userGuid")
    void updateLastLoginDateTime(@Param("userGuid") String userGuid, @Param("lastLoginDateTime") LocalDateTime lastLoginDateTime);

    @Query("select u.userGuid, u.username from UserEntity u where u.userGuid IN (:userGuids)")
    List<Object[]> findNamesByUserGuid(@Param("userGuids") List<String> userGuids);
}