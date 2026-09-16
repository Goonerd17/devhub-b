package teamdevhub.devhub.member.api;

public interface CurrentMemberRoleQuery {
    MemberRole findCurrentRole(String memberGuid);
}
