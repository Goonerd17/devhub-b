package teamdevhub.devhub.shared.member;

import java.util.List;

public record MemberApplicationProfile(String memberGuid, String displayName, String introduction,
                                       double mannerDegree, List<String> skills) {
}
