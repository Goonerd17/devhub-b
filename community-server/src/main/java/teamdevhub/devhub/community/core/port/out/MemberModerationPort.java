package teamdevhub.devhub.community.core.port.out;
import java.util.List; import teamdevhub.devhub.shared.member.MemberModerationProfile;
public interface MemberModerationPort { List<String> findMemberGuidsByStatus(String statusCode); List<MemberModerationProfile> findModerationProfiles(List<String> ids); }
