package teamdevhub.devhub.member.api.profile;

import java.util.List;

/** Project 지원자 화면에 필요한 회원 공개 정보만 담는 계약 모델. */
public record MemberApplicationProfile(String memberGuid, String displayName, String introduction,
                                       double mannerDegree, List<String> skills) {
}
