package teamdevhub.devhub.member.core.user.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.platform.core.common.audit.AuditInfo;
import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.member.core.user.domain.vo.command.UpdateUserCommand;
import teamdevhub.devhub.member.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.member.core.user.domain.vo.position.UserPositionChangeResult;
import teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkillChangeResult;
import teamdevhub.devhub.member.core.user.port.in.command.UpdateProfileImageCommand;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@Getter
public class User {

    private final String userGuid;
    private final UserRole userRole;

    private String username;
    private String introduction;
    private String fileGuid;

    private Set<UserPosition> positions;
    private Set<UserSkill> skills;

    private double mannerDegree;

    private boolean blocked;
    private LocalDateTime blockEndDate;
    private boolean deleted;

    private LocalDateTime lastLoginDateTime;

    private final AuditInfo auditInfo;

    @Builder
    private User(
            String userGuid,
            UserRole userRole,
            String username,
            String introduction,
            String fileGuid,
            Set<UserPosition> positions,
            Set<UserSkill> skills,
            double mannerDegree,
            boolean blocked,
            LocalDateTime blockEndDate,
            boolean deleted,
            LocalDateTime lastLoginDateTime,
            AuditInfo auditInfo
    ) {
        this.userGuid = userGuid;
        this.userRole = userRole;

        this.username = username;
        this.introduction = introduction;
        this.fileGuid = fileGuid;

        this.positions = Objects.requireNonNullElseGet(positions, HashSet::new);
        this.skills = Objects.requireNonNullElseGet(skills, HashSet::new);

        this.mannerDegree = mannerDegree;

        this.blocked = blocked;
        this.blockEndDate = blockEndDate;
        this.deleted = deleted;

        this.lastLoginDateTime = lastLoginDateTime;

        this.auditInfo = auditInfo == null
                ? AuditInfo.empty()
                : auditInfo;
    }

    public static User createAdminUser(CreateUserCommand adminCreateUserCommand) {
        return User.builder()
                .userGuid(adminCreateUserCommand.userGuid())
                .userRole(UserRole.ADMIN)
                .username(adminCreateUserCommand.username())
                .blocked(false)
                .deleted(false)
                .lastLoginDateTime(null)
                .auditInfo(AuditInfo.empty())
                .build();
    }

    public static User createGeneralUser(CreateUserCommand generalCreateUserCommand) {
        return User.builder()
                .userGuid(generalCreateUserCommand.userGuid())
                .userRole(UserRole.USER)
                .username(generalCreateUserCommand.username())
                .introduction(generalCreateUserCommand.introduction())
                .mannerDegree(36.5)
                .blocked(false)
                .deleted(false)
                .lastLoginDateTime(null)
                .auditInfo(AuditInfo.empty())
                .build();
    }

    public static User createOauthUser(CreateUserCommand oauthCreateUserCommand) {
        return User.builder()
                .userGuid(oauthCreateUserCommand.userGuid())
                .userRole(UserRole.USER)
                .username(oauthCreateUserCommand.username())
                .introduction(oauthCreateUserCommand.introduction())
                .mannerDegree(36.5)
                .blocked(false)
                .deleted(false)
                .lastLoginDateTime(null)
                .auditInfo(AuditInfo.empty())
                .build();
    }

    public static User of(
            String userGuid,
            UserRole userRole,
            String username,
            String introduction,
            String fileGuid,
            double mannerDegree,
            boolean blocked,
            LocalDateTime blockEndDate,
            boolean deleted,
            LocalDateTime lastLoginDateTime,
            AuditInfo auditInfo
    ) {
        return User.builder()
                .userGuid(userGuid)
                .userRole(userRole)
                .username(username)
                .introduction(introduction)
                .fileGuid(fileGuid)
                .mannerDegree(mannerDegree)
                .blocked(blocked)
                .blockEndDate(blockEndDate)
                .deleted(deleted)
                .lastLoginDateTime(lastLoginDateTime)
                .auditInfo(auditInfo)
                .build();
    }

    public void updateLastLoginDateTime(LocalDateTime lastLoginDateTime) {
        this.lastLoginDateTime = lastLoginDateTime;
    }

    public void withdraw() {
        if (this.deleted) {
            throw DomainRuleException.of(ErrorCode.ALREADY_DELETED);
        }
        this.deleted = true;
        this.blocked = false;
    }

    public void updateProfileImage(UpdateProfileImageCommand updateProfileImageCommand) {
        this.fileGuid = updateProfileImageCommand.fileGuid();
    }

    public void updateBasicProfile(UpdateUserCommand updateUserCommand) {
        if (hasText(updateUserCommand.username()) && !updateUserCommand.username().equals(this.username)) {
            this.username = updateUserCommand.username();
        }

        if (hasText(updateUserCommand.introduction()) && !updateUserCommand.introduction().equals(this.introduction)) {
            this.introduction = updateUserCommand.introduction();
        }
    }

    public UserPositionChangeResult changePositions(Set<UserPosition> newPositions) {
        if (hasInvalidItems(newPositions, UserPosition::positionCd)) {
            return UserPositionChangeResult.unchanged(this.positions);
        }
        if (!this.positions.equals(newPositions)) {
            Set<UserPosition> oldPositions = Set.copyOf(positions);
            this.positions.clear();
            this.positions.addAll(newPositions);
            return UserPositionChangeResult.changed(oldPositions, this.positions);
        }
        return UserPositionChangeResult.unchanged(this.positions);
    }

    public UserSkillChangeResult changeSkills(Set<UserSkill> newSkills) {
        if (hasInvalidItems(newSkills, UserSkill::skillCd)) {
            return UserSkillChangeResult.unchanged(this.skills);
        }
        if (!this.skills.equals(newSkills)) {
            Set<UserSkill> oldSkills = Set.copyOf(this.skills);
            this.skills.clear();
            this.skills.addAll(newSkills);
            return UserSkillChangeResult.changed(oldSkills, this.skills);
        }
        return UserSkillChangeResult.unchanged(this.skills);
    }

    public void loadPositionsAndSkills(Set<UserPosition> positions, Set<UserSkill> skills) {
        this.positions = new HashSet<>(positions);
        this.skills = new HashSet<>(skills);
    }

    public void ban(LocalDateTime blockEndDate) {
        if (this.deleted) {
            throw DomainRuleException.of(ErrorCode.USER_WITHDRAWN);
        }
        if (this.blocked) {
            throw DomainRuleException.of(ErrorCode.USER_ALREADY_BANNED);
        }
        this.blocked = true;
        this.blockEndDate = blockEndDate;
    }

    public void unban() {
        if (!this.blocked) {
            throw DomainRuleException.of(ErrorCode.USER_NOT_BANNED);
        }
        this.blocked = false;
        this.blockEndDate = null;
    }

    public void assertActive() {
        if (this.deleted) {
            throw DomainRuleException.of(ErrorCode.USER_WITHDRAWN);
        }

        if (this.blocked) {
            throw DomainRuleException.of(ErrorCode.USER_BLOCKED);
        }
    }

    public void applyReviewScore(double score) {
        this.mannerDegree += (score - 3.0);
    }

    private <T> boolean hasInvalidItems(Set<T> items, Function<T, String> codeExtractor) {
        if (items == null || items.isEmpty()) {
            return true;
        }
        return items.stream().anyMatch(item -> {
            String code = codeExtractor.apply(item);
            return code == null || code.isBlank();
        });
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}