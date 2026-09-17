package teamdevhub.devhub.member.outbound.user.adapter.mapper;

import teamdevhub.devhub.shared.core.common.audit.AuditInfo;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.outbound.user.adapter.entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .userGuid(user.getUserGuid())
                .userRole(user.getUserRole())
                .username(user.getUsername())
                .introduction(user.getIntroduction())
                .fileGuid(user.getFileGuid())
                .mannerDegree(user.getMannerDegree())
                .blocked(user.isBlocked())
                .blockEndDate(user.getBlockEndDate())
                .deleted(user.isDeleted())
                .lastLoginDateTime(user.getLastLoginDateTime())
                .build();
    }

    public static User toDomain(UserEntity userEntity) {
        return User.of(
                userEntity.getUserGuid(),
                userEntity.getUserRole(),
                userEntity.getUsername(),
                userEntity.getIntroduction(),
                userEntity.getFileGuid(),
                userEntity.getMannerDegree(),
                userEntity.isBlocked(),
                userEntity.getBlockEndDate(),
                userEntity.isDeleted(),
                userEntity.getLastLoginDateTime(),
                toAuditInfo(userEntity)
        );
    }

    private static AuditInfo toAuditInfo(UserEntity userEntity) {
        return AuditInfo.of(
                userEntity.getRegistrantGuid(),
                userEntity.getRegisteredDate(),
                userEntity.getModifierGuid(),
                userEntity.getModifiedDate()
        );
    }
}