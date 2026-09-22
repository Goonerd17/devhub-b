package teamdevhub.devhub.shared.member;

public record MemberModerationProfile(String memberGuid, String displayName, boolean deleted, boolean blocked) {
    public String statusCode() {
        if (deleted) return "7002";
        if (blocked) return "7003";
        return "7001";
    }
}
