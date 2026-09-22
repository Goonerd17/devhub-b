package teamdevhub.devhub.member.http.internal;

import java.util.List;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.member.api.profile.MemberCommunityProfileQuery;
import teamdevhub.devhub.member.api.profile.MemberPublicProfileQuery;
import teamdevhub.devhub.member.api.profile.MemberApplicationProfileQuery;
import teamdevhub.devhub.member.api.profile.MemberProjectOwnerQuery;
import teamdevhub.devhub.member.api.review.MemberReviewScoreQuery;
import teamdevhub.devhub.shared.member.MemberCommunityProfile;
import teamdevhub.devhub.shared.member.MemberPublicProfile;
import teamdevhub.devhub.shared.member.MemberApplicationProfile;
import teamdevhub.devhub.shared.member.MemberProjectOwner;
import teamdevhub.devhub.shared.member.MemberModerationProfile;

@RestController
@RequestMapping("/internal/members")
@RequiredArgsConstructor
public class MemberProfileInternalController {
    private final MemberPublicProfileQuery publicProfileQuery;
    private final MemberCommunityProfileQuery communityProfileQuery;
    private final MemberApplicationProfileQuery applicationProfileQuery;
    private final MemberProjectOwnerQuery projectOwnerQuery;
    private final MemberReviewScoreQuery reviewScoreQuery;
    private final EntityManager entityManager;

    @GetMapping("/public-profiles")
    public List<MemberPublicProfile> publicProfiles(@RequestParam List<String> memberGuids) {
        return publicProfileQuery.findPublicProfilesByMemberGuids(memberGuids).stream()
                .map(profile -> new MemberPublicProfile(profile.memberGuid(), profile.displayName()))
                .toList();
    }

    @GetMapping("/community-profiles")
    public List<MemberCommunityProfile> communityProfiles(@RequestParam List<String> memberGuids) {
        return communityProfileQuery.findCommunityProfiles(memberGuids).stream()
                .map(profile -> new MemberCommunityProfile(
                        profile.memberGuid(), profile.displayName(), profile.profileImageGuid()))
                .toList();
    }

    @GetMapping("/moderation-profiles")
    public List<MemberModerationProfile> moderationProfiles(@RequestParam List<String> memberGuids) {
        if (memberGuids.isEmpty()) return List.of();
        return entityManager.createQuery(
                        "SELECT u.userGuid, u.username, u.deleted, u.blocked FROM UserEntity u WHERE u.userGuid IN :memberGuids",
                        Object[].class)
                .setParameter("memberGuids", memberGuids)
                .getResultList().stream()
                .map(row -> new MemberModerationProfile(
                        (String) row[0], (String) row[1], (Boolean) row[2], (Boolean) row[3]))
                .toList();
    }

    @GetMapping("/moderation-status/{statusCode}")
    public List<String> memberGuidsByStatus(@PathVariable String statusCode) {
        String condition = switch (statusCode) {
            case "7001" -> "u.deleted = false AND u.blocked = false";
            case "7002" -> "u.deleted = true";
            case "7003" -> "u.blocked = true";
            default -> throw new IllegalArgumentException("Unsupported member status: " + statusCode);
        };
        return entityManager.createQuery("SELECT u.userGuid FROM UserEntity u WHERE " + condition, String.class)
                .getResultList();
    }

    @GetMapping("/{memberGuid}/project-owner")
    public MemberProjectOwner projectOwner(@PathVariable String memberGuid) {
        var owner = projectOwnerQuery.findProjectOwner(memberGuid);
        return new MemberProjectOwner(owner.memberGuid(), owner.displayName(), owner.profileImageGuid());
    }

    @GetMapping("/application-profiles")
    public java.util.Map<String, MemberApplicationProfile> applicationProfiles(
            @RequestParam List<String> memberGuids) {
        return applicationProfileQuery.findApplicationProfiles(memberGuids).entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(java.util.Map.Entry::getKey, entry -> {
                    var profile = entry.getValue();
                    return new MemberApplicationProfile(profile.memberGuid(), profile.displayName(),
                            profile.introduction(), profile.mannerDegree(), profile.skills());
                }));
    }

    @GetMapping("/{revieweeGuid}/review-score")
    public org.springframework.http.ResponseEntity<Double> reviewScore(
            @PathVariable String revieweeGuid, @RequestParam String projectGuid) {
        return org.springframework.http.ResponseEntity.of(reviewScoreQuery.findScore(projectGuid, revieweeGuid));
    }
}
